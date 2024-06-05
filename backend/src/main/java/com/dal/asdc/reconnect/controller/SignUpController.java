package com.dal.asdc.reconnect.controller;


import com.dal.asdc.reconnect.DTO.*;
import com.dal.asdc.reconnect.model.RefreshToken;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.service.*;
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
public class SignUpController {

    @Autowired
    SignUpService signUpService;

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




    @PostMapping("/signup")
    public SignUpFirstPhaseResponse signUp(@RequestBody SignUpFirstPhaseRequest signUpFirstPhaseRequest)
    {
        return signUpService.validateFirstPhase(signUpFirstPhaseRequest);
    }


    @PostMapping("/signup-final")
//    @Transactional
    public SignUpSecondPhaseResponse signUpFinal(@RequestBody SignUpSecondPhaseRequest signUpSecondPhaseRequest)
    {
        SignUpSecondPhaseResponse signUpSecondPhaseResponse = new SignUpSecondPhaseResponse();
        SignUpFirstPhaseRequest signUpFirstPhaseRequest = convertIntoFirstPhase(signUpSecondPhaseRequest);

        SignUpFirstPhaseResponse signUpFirstPhaseResponse = signUpService.validateFirstPhase(signUpFirstPhaseRequest);

        if (signUpFirstPhaseResponse.getBody().areAllValuesNull()) {
            boolean userAdded = signUpService.AddNewUser(signUpSecondPhaseRequest);
            if (userAdded)
            {

                signUpSecondPhaseResponse.setStatus(200);
                signUpSecondPhaseResponse.setMessage("User added successfully");
            }
        }
        if(signUpSecondPhaseResponse.getStatus() != 200)
        {
            signUpSecondPhaseResponse.setStatus(400);
            signUpSecondPhaseResponse.setMessage("User not added successfully");
        }
        return signUpSecondPhaseResponse;
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


    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest)
    {
        Optional<Users> authenticatedUser = signUpService.authenticate(loginRequest);

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


    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUsers)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo);
                    return JwtResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken()).build();
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
    }


    @GetMapping("/GetCountry")
    public Response<CountryResponseBody> getCountry()
    {

        CountryResponseBody countryResponseBody = new CountryResponseBody();

        countryResponseBody = countryService.getCountryList();


        String message = (countryResponseBody != null) ? "Success" : "Unsuccessful";

        int status = (countryResponseBody != null) ? 200 : 400;

        return new Response<>(status, message, countryResponseBody);

    }


    @GetMapping("/GetCities")
    public Response<CityResponseBody> getCities(@RequestParam int countryID)
    {

        CityResponseBody cityResponseBody = new CityResponseBody();

        cityResponseBody =  cityService.getCitiesByCountryId(countryID);

        String message = (cityResponseBody != null) ? "Success" : "Unsuccessful";

        int status = (cityResponseBody != null) ? 200 : 400;

        return new Response<>(status, message, cityResponseBody);

    }



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
