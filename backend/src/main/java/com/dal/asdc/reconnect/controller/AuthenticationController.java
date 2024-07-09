package com.dal.asdc.reconnect.controller;

import com.dal.asdc.reconnect.dto.User.UserDetails;
import com.dal.asdc.reconnect.model.RefreshToken;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

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

    @Value("${upload.images.directory}")
    private String uploadImagesDirectory;


    /**
     * Handles the first phase of the signup process.
     * Validates the provided signup request and returns a response
     * indicating success or failure along with the validation results.
     *
     * @param signUpFirstPhaseRequest The SignUpFirstPhaseRequest object containing signup details.
     * @return Response object containing the validation results along with status and message.
     */
    @PostMapping("/verify-email")
    public ResponseEntity<?> signUp(@RequestBody com.dal.asdc.reconnect.dto.SignUp.SignUpFirstPhaseRequest signUpFirstPhaseRequest) {
        Map<String, Boolean> responseMap = new HashMap<>();
        Users user = authenticationService.getUserByEmail(signUpFirstPhaseRequest.getEmail());

        if (user != null) {
            responseMap.put("emailAlreadyExists", true);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseMap);
        }

        String password = signUpFirstPhaseRequest.getPassword();
        String reEnteredPassword = signUpFirstPhaseRequest.getReenteredPassword();

        if (!password.equals(reEnteredPassword)) {
            responseMap.put("mismatchPasswords", true);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseMap);
        }

        com.dal.asdc.reconnect.dto.Response<?> response = new com.dal.asdc.reconnect.dto.Response<>(HttpStatus.OK.value(), "Verified", null);
        return ResponseEntity.ok(response);


    }


    /**
     * Handles the final phase of the signup process.
     * Converts the provided second phase signup request into first phase,
     * validates it, and adds a new user if validation is successful.
     *
     * @param signUpSecondPhaseRequest The SignUpSecondPhaseRequest object containing signup details.
     * @return Response object indicating success or failure of signup process along with validation results.
     */
    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<?> signUpFinal(@ModelAttribute com.dal.asdc.reconnect.dto.SignUp.SignUpSecondPhaseRequest signUpSecondPhaseRequest, @RequestParam("profile") MultipartFile file) throws IOException {
        com.dal.asdc.reconnect.dto.SignUp.SignUpFirstPhaseRequest signUpFirstPhaseRequest = convertIntoFirstPhase(signUpSecondPhaseRequest);

        com.dal.asdc.reconnect.dto.SignUp.SignUpFirstPhaseBody signUpFirstPhaseBody = authenticationService.validateFirstPhase(signUpFirstPhaseRequest);

        Path directory = Paths.get(uploadImagesDirectory);
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
                System.out.println("Directory created: " + directory);
            } catch (IOException e) {
                System.err.println("Failed to create directory: " + directory);
                e.printStackTrace();
            }
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path fileNameAndPath = Paths.get(uploadImagesDirectory, fileName);
        Files.write(fileNameAndPath, file.getBytes());

        if (signUpFirstPhaseBody.areAllValuesNull() && (authenticationService.addNewUser(signUpSecondPhaseRequest, String.valueOf(fileNameAndPath)))) {
            com.dal.asdc.reconnect.dto.Response<com.dal.asdc.reconnect.dto.SignUp.SignUpFirstPhaseBody> response = new com.dal.asdc.reconnect.dto.Response<>(200, "Success", signUpFirstPhaseBody);
            return ResponseEntity.ok(response);

        }

        com.dal.asdc.reconnect.dto.Response<Void> response = new com.dal.asdc.reconnect.dto.Response<>(403, "UnSuccessful", null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }


    /**
     * In the second phase of signup, all the information of first phase will pass again which will converted into
     * first Phase and will check for first phase again.
     */
    private com.dal.asdc.reconnect.dto.SignUp.SignUpFirstPhaseRequest convertIntoFirstPhase(com.dal.asdc.reconnect.dto.SignUp.SignUpSecondPhaseRequest signUpSecondPhaseRequest) {
        com.dal.asdc.reconnect.dto.SignUp.SignUpFirstPhaseRequest signUpFirstPhaseRequest = new com.dal.asdc.reconnect.dto.SignUp.SignUpFirstPhaseRequest();
        signUpFirstPhaseRequest.setPassword(signUpSecondPhaseRequest.getPassword());
        signUpFirstPhaseRequest.setEmail(signUpSecondPhaseRequest.getEmail());
        signUpFirstPhaseRequest.setReenteredPassword(signUpSecondPhaseRequest.getReenteredPassword());
        signUpFirstPhaseRequest.setUserType(signUpSecondPhaseRequest.getUserType());
        return signUpFirstPhaseRequest;
    }


    /**
     * Authenticates a user by validating the provided login credentials.
     * If authentication is successful, generates a JWT token and returns it along with a refresh token.
     * If authentication fails, returns an unauthorized response with an appropriate error message.
     *
     * @param loginRequest The LoginRequest object containing user credentials.
     * @return ResponseEntity containing either a successful login response or an error response.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody com.dal.asdc.reconnect.dto.LoginDto.LoginRequest loginRequest) {
        Optional<Users> authenticatedUser = authenticationService.authenticate(loginRequest);

        if (authenticatedUser.isEmpty()) {
            com.dal.asdc.reconnect.dto.Response<Void> response = new com.dal.asdc.reconnect.dto.Response<>(401, "UnSuccessful", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.getEmail());
        String jwtToken = jwtService.generateToken(authenticatedUser.get());
        com.dal.asdc.reconnect.dto.Response<com.dal.asdc.reconnect.dto.LoginDto.LoginResponseBody> response = getLoginResponse(loginRequest, jwtToken, refreshToken);

        return ResponseEntity.ok(response);
    }


    /**
     * Generates a login response containing the JWT token, expiration time, and refresh token.
     *
     * @param loginRequest The LoginRequest object containing user credentials.
     * @param jwtToken     The JWT token generated for the authenticated user.
     * @param refreshToken The refresh token generated for the authenticated user.
     * @return LoginResponse object containing the login details.
     */
    private com.dal.asdc.reconnect.dto.Response<com.dal.asdc.reconnect.dto.LoginDto.LoginResponseBody> getLoginResponse(com.dal.asdc.reconnect.dto.LoginDto.LoginRequest loginRequest, String jwtToken, RefreshToken refreshToken) {
        com.dal.asdc.reconnect.dto.LoginDto.LoginResponseBody loginResponseBody = new com.dal.asdc.reconnect.dto.LoginDto.LoginResponseBody();
        loginResponseBody.setToken(jwtToken);
        loginResponseBody.setExpiresIn(jwtService.getExpirationTime());
        loginResponseBody.setRefreshToken(refreshToken.getToken());
        loginResponseBody.setUserEmail(loginRequest.getEmail());
        return new com.dal.asdc.reconnect.dto.Response<>(HttpStatus.OK.value(), "Success", loginResponseBody);
    }


    /**
     * Refreshes the JWT token using the provided refresh token.
     * If the refresh token is valid, generates a new JWT token and returns it.
     * If the refresh token is invalid or expired, throws a RuntimeException.
     *
     * @param refreshTokenRequest The RefreshTokenRequest object containing the refresh token.
     * @return JwtResponse object containing the new JWT token.
     */
    @PostMapping("/refreshToken")
    public com.dal.asdc.reconnect.dto.RefreshToken.RefreshTokenResponse refreshToken(@RequestBody com.dal.asdc.reconnect.dto.RefreshToken.RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken()).map(refreshTokenService::verifyExpiration).map(RefreshToken::getUsers).map(userInfo -> {
            String accessToken = jwtService.generateToken(userInfo);
            return com.dal.asdc.reconnect.dto.RefreshToken.RefreshTokenResponse.builder().accessToken(accessToken).token(refreshTokenRequest.getToken()).build();
        }).orElseThrow(() -> new RuntimeException("Refresh Token is not in DB..!!"));
    }

    /**
     * Endpoint to initiate the password reset process.
     * Sends a password reset email to the user with the specified email address.
     *
     * @param emailRequest the email address of the user who requested a password reset.
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
    public ResponseEntity<String> resetPassword(@RequestBody com.dal.asdc.reconnect.dto.ResetPassword.ResetPasswordRequest request) {
        boolean result = forgotPasswordService.resetPassword(request.getToken(), request.getNewPassword());
        if (result) {
            return ResponseEntity.ok("Password reset successful.");
        } else {
            return ResponseEntity.badRequest().body("Invalid token.");
        }
    }

    @GetMapping("/getUserDetails")
    public  ResponseEntity<?> getUserDetails(@RequestParam String token) {
        if(jwtService.isTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Expired token");
        }

        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(jwtService.extractID(token));
        userDetails.setUsername(jwtService.extractUsername(token));
        userDetails.setEmail(jwtService.extractEmail(token));
        userDetails.setRole(jwtService.extractUserTypeFromToken(token));

        return ResponseEntity.status(HttpStatus.OK).body(userDetails);
    }
}
