package com.dal.asdc.reconnect.controller;

import com.dal.asdc.reconnect.DTO.LoginDTO.LoginRequest;
import com.dal.asdc.reconnect.DTO.LoginDTO.LoginResponseBody;
import com.dal.asdc.reconnect.DTO.RefreshToken.RefreshTokenRequest;
import com.dal.asdc.reconnect.DTO.RefreshToken.RefreshTokenResponse;
import com.dal.asdc.reconnect.DTO.ResetPassword.ResetPasswordRequest;
import com.dal.asdc.reconnect.DTO.Response;
import com.dal.asdc.reconnect.DTO.SignUp.SignUpFirstPhaseBody;
import com.dal.asdc.reconnect.DTO.SignUp.SignUpFirstPhaseRequest;
import com.dal.asdc.reconnect.DTO.SignUp.SignUpSecondPhaseRequest;
import com.dal.asdc.reconnect.model.RefreshToken;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthenticationController
{

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    JWTService jwtService;

    @Autowired
    CityService cityService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    /**
     * Handles the first phase of the signup process.
     * Validates the provided signup request and returns a response
     * indicating success or failure along with the validation results.
     * @param signUpFirstPhaseRequest The SignUpFirstPhaseRequest object containing signup details.
     * @return Response object containing the validation results along with status and message.
     */
    @PostMapping("/verify-email")
    public ResponseEntity<?> signUp(@RequestBody SignUpFirstPhaseRequest signUpFirstPhaseRequest)
    {
        SignUpFirstPhaseBody signUpFirstPhaseBody;

        signUpFirstPhaseBody = authenticationService.validateFirstPhase(signUpFirstPhaseRequest);

        if(signUpFirstPhaseBody.areAllValuesNull())
        {

            Response<SignUpFirstPhaseBody>  response = new Response<>(200, "Success", signUpFirstPhaseBody);
            return ResponseEntity.ok(response);
        }
        Response<Void>  response = new Response<>(403, "UnSuccessful", null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

    }


    /**
     * Handles the final phase of the signup process.
     * Converts the provided second phase signup request into first phase,
     * validates it, and adds a new user if validation is successful.
     * @param signUpSecondPhaseRequest The SignUpSecondPhaseRequest object containing signup details.
     * @return Response object indicating success or failure of signup process along with validation results.
     */
    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<?> signUpFinal(@RequestBody SignUpSecondPhaseRequest signUpSecondPhaseRequest)
    {
        SignUpFirstPhaseRequest signUpFirstPhaseRequest = convertIntoFirstPhase(signUpSecondPhaseRequest);

        SignUpFirstPhaseBody signUpFirstPhaseBody = authenticationService.validateFirstPhase(signUpFirstPhaseRequest);

        if(signUpFirstPhaseBody.areAllValuesNull())
        {
            if(Boolean.TRUE.equals(authenticationService.AddNewUser(signUpSecondPhaseRequest)))
            {
                Response<SignUpFirstPhaseBody>  response = new Response<>(200, "Success", signUpFirstPhaseBody);
                return ResponseEntity.ok(response);
            }
        }

        Response<Void>  response = new Response<>(403, "UnSuccessful", null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
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

        if (authenticatedUser.isEmpty())
        {
            Response<Void>  response = new Response<>(401, "UnSuccessful", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.getUserEmail());
        String jwtToken = jwtService.generateToken(authenticatedUser.get());

        Response<LoginResponseBody> response = getLoginResponse(loginRequest, jwtToken, refreshToken);

        return ResponseEntity.ok(response);
    }


    /**
     * Generates a login response containing the JWT token, expiration time, and refresh token.
     * @param loginRequest The LoginRequest object containing user credentials.
     * @param jwtToken     The JWT token generated for the authenticated user.
     * @param refreshToken The refresh token generated for the authenticated user.
     * @return LoginResponse object containing the login details.
     */
    private Response<LoginResponseBody> getLoginResponse(LoginRequest loginRequest, String jwtToken, RefreshToken refreshToken)
    {
        LoginResponseBody loginResponseBody = new LoginResponseBody();
        loginResponseBody.setToken(jwtToken);
        loginResponseBody.setExpiresIn(jwtService.getExpirationTime());
        loginResponseBody.setRefreshToken(refreshToken.getToken());
        loginResponseBody.setUserEmail(loginRequest.getUserEmail());
        return new Response<>(HttpStatus.OK.value(), "Success", loginResponseBody);
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
     * Endpoint to initiate the password reset process.
     * Sends a password reset email to the user with the specified email address.
     *
     * @param email the email address of the user who requested a password reset.
     * @return a ResponseEntity indicating the result of the operation.
     */
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> emailRequest) {
        String email = emailRequest.get("email");
        forgotPasswordService.sendResetEmail(email);
        return ResponseEntity.ok("Password reset email sent.");
    }

    /**
     * Endpoint to reset the password.
     * Resets the password for the user with the specified reset token.
     *
     * @param request the ResetPasswordRequest object containing the reset token and the new password.
     * @return a ResponseEntity indicating the result of the operation.
     */
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean result = forgotPasswordService.resetPassword(request.getToken(), request.getNewPassword());
        if (result) {
            return ResponseEntity.ok("Password reset successful.");
        } else {
            return ResponseEntity.badRequest().body("Invalid token.");
        }
    }
}
