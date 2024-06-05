package com.dal.asdc.reconnect.controller;


import com.dal.asdc.reconnect.DTO.*;
import com.dal.asdc.reconnect.DTO.LoginDTO.LoginRequest;
import com.dal.asdc.reconnect.DTO.LoginDTO.LoginResponse;
import com.dal.asdc.reconnect.DTO.LoginDTO.LoginResponseBody;
import com.dal.asdc.reconnect.DTO.RefreshToken.RefreshTokenRequest;
import com.dal.asdc.reconnect.DTO.RefreshToken.RefreshTokenResponse;
import com.dal.asdc.reconnect.DTO.SignUp.SignUpFirstPhaseBody;
import com.dal.asdc.reconnect.DTO.SignUp.SignUpFirstPhaseRequest;
import com.dal.asdc.reconnect.DTO.SignUp.SignUpSecondPhaseRequest;
import com.dal.asdc.reconnect.model.RefreshToken;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    JWTService jwtService;

    @Autowired
    CountryService countryService;

    @Autowired
    CityService cityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private SkillsService  skillsService;



    /**
     * Handles the first phase of the signup process.
     * Validates the provided signup request and returns a response
     * indicating success or failure along with the validation results.
     * @param signUpFirstPhaseRequest The SignUpFirstPhaseRequest object containing signup details.
     * @return Response object containing the validation results along with status and message.
     */
    @PostMapping("/signup")
    public Response<SignUpFirstPhaseBody> signUp(@RequestBody SignUpFirstPhaseRequest signUpFirstPhaseRequest)
    {
        SignUpFirstPhaseBody signUpFirstPhaseBody = new SignUpFirstPhaseBody();

        signUpFirstPhaseBody = authenticationService.validateFirstPhase(signUpFirstPhaseRequest);

        String message = (signUpFirstPhaseBody.areAllValuesNull()) ? "Success" : "Unsuccessful";

        int status = (signUpFirstPhaseBody.areAllValuesNull()) ? 200 : 400;

        return new Response<>(status, message, signUpFirstPhaseBody);

    }


    /**
     * Handles the final phase of the signup process.
     * Converts the provided second phase signup request into first phase,
     * validates it, and adds a new user if validation is successful.
     * @param signUpSecondPhaseRequest The SignUpSecondPhaseRequest object containing signup details.
     * @return Response object indicating success or failure of signup process along with validation results.
     */
    @PostMapping("/signup-final")
    @Transactional
    public Response<SignUpFirstPhaseBody> signUpFinal(@RequestBody SignUpSecondPhaseRequest signUpSecondPhaseRequest)
    {
        SignUpFirstPhaseRequest signUpFirstPhaseRequest = convertIntoFirstPhase(signUpSecondPhaseRequest);

        SignUpFirstPhaseBody signUpFirstPhaseBody = authenticationService.validateFirstPhase(signUpFirstPhaseRequest);

        String message = "Unsuccessful";
        int status = 400;

        if(signUpFirstPhaseBody.areAllValuesNull())
        {
            if(authenticationService.AddNewUser(signUpSecondPhaseRequest))
            {
                message = "Success";

                status = 200;

            }
        }
        return new Response<>(status, message, signUpFirstPhaseBody);
    }


    /**
     * In the second phase of signup, all the information of first phase will pass again which will converted into
     * first Phase and will check for first phase again.
     */
    private SignUpFirstPhaseRequest convertIntoFirstPhase(SignUpSecondPhaseRequest signUpSecondPhaseRequest)
    {
        SignUpFirstPhaseRequest signUpFirstPhaseRequest = new SignUpFirstPhaseRequest();
        signUpFirstPhaseRequest.setPassword(signUpSecondPhaseRequest.getPassword());
        signUpFirstPhaseRequest.setUserEmail(signUpSecondPhaseRequest.getUserEmail());
        signUpFirstPhaseRequest.setReenteredPassword(signUpSecondPhaseRequest.getReenteredPassword());
        signUpFirstPhaseRequest.setUserType(signUpSecondPhaseRequest.getUserType());
        return signUpFirstPhaseRequest;
    }


    /**
     * Authenticates a user by validating the provided login credentials.
     * If authentication is successful, generates a JWT token and returns it along with a refresh token.
     * If authentication fails, returns an unauthorized response with an appropriate error message.
     * @param loginRequest The LoginRequest object containing user credentials.
     * @return ResponseEntity containing either a successful login response or an error response.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest)
    {
        Optional<Users> authenticatedUser = authenticationService.authenticate(loginRequest);

        if (authenticatedUser == null || authenticatedUser.isEmpty())
        {
            ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), "Bad credentials");
            errorDetail.setProperty("description", "The username or password is incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetail);
        }


        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.getUserEmail());

        String jwtToken = jwtService.generateToken(authenticatedUser.get());
        LoginResponse loginResponse = getLoginResponse(loginRequest, jwtToken, refreshToken);


        return ResponseEntity.ok(loginResponse);
    }


    /**
     * Generates a login response containing the JWT token, expiration time, and refresh token.
     * @param loginRequest The LoginRequest object containing user credentials.
     * @param jwtToken     The JWT token generated for the authenticated user.
     * @param refreshToken The refresh token generated for the authenticated user.
     * @return LoginResponse object containing the login details.
     */
    private LoginResponse getLoginResponse(LoginRequest loginRequest, String jwtToken, RefreshToken refreshToken) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setStatus(200);
        loginResponse.setDetail("Login Successful");

        LoginResponseBody loginResponseBody = new LoginResponseBody();
        loginResponseBody.setToken(jwtToken);
        loginResponseBody.setExpiresIn(jwtService.getExpirationTime());
        loginResponseBody.setRefreshToken(refreshToken.getToken());
        loginResponseBody.setUserEmail(loginRequest.getUserEmail());

        loginResponse.setBody(loginResponseBody);
        return loginResponse;
    }


    /**
     * Refreshes the JWT token using the provided refresh token.
     * If the refresh token is valid, generates a new JWT token and returns it.
     * If the refresh token is invalid or expired, throws a RuntimeException.
     * @param refreshTokenRequest The RefreshTokenRequest object containing the refresh token.
     * @return JwtResponse object containing the new JWT token.
     */
    @PostMapping("/refreshToken")
    public RefreshTokenResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUsers)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo);
                    return RefreshTokenResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken()).build();
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
    }


    /**
     * Retrieves the list of all countries.
     * Returns a response containing the list of countries along with status and message.
     * @return Response object containing the list of countries.
     */
    @GetMapping("/GetCountry")
    public Response<CountryResponseBody> getCountry()
    {

        CountryResponseBody countryResponseBody = new CountryResponseBody();

        countryResponseBody = countryService.getCountryList();

        String message = (countryResponseBody != null) ? "Success" : "Unsuccessful";

        int status = (countryResponseBody != null) ? 200 : 400;

        return new Response<>(status, message, countryResponseBody);

    }


    /**
     * Retrieves the list of cities for a given country ID.
     * Returns a response containing the list of cities along with status and message.
     * @param countryID The ID of the country for which cities are to be retrieved.
     * @return Response object containing the list of cities.
     */
    @GetMapping("/GetCities")
    public Response<CityResponseBody> getCities(@RequestParam int countryID)
    {

        CityResponseBody cityResponseBody = new CityResponseBody();

        cityResponseBody =  cityService.getCitiesByCountryId(countryID);

        String message = (cityResponseBody != null) ? "Success" : "Unsuccessful";

        int status = (cityResponseBody != null) ? 200 : 400;

        return new Response<>(status, message, cityResponseBody);

    }


    /**
     * Retrieves the list of all skills.
     * Returns a response containing the list of skills along with status and message.
     * @return Response object containing the list of skills.
     */
    @GetMapping("/GetSkills")
    public Response<SkillsResponseBody> getSkills()
    {
        SkillsResponseBody skillsResponseBody = new SkillsResponseBody();

        skillsResponseBody = skillsService.getSkills();

        String message = (skillsResponseBody != null) ? "Success" : "Unsuccessful";

        int status = (skillsResponseBody != null) ? 200 : 400;

        return new Response<>(status, message, skillsResponseBody);


    }
}
