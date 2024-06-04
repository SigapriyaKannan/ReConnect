package com.dal.asdc.reconnect.controller;


import com.dal.asdc.reconnect.DTO.*;
import com.dal.asdc.reconnect.model.RefreshToken;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.service.JWTService;
import com.dal.asdc.reconnect.service.RefreshTokenService;
import com.dal.asdc.reconnect.service.SignUpService;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenService refreshTokenService;




    @PostMapping("/signup")
    public SignUpFirstPhaseResponse signUp(@RequestBody SignUpRequest signUpRequest)
    {
        return signUpService.validateFirstPhase(signUpRequest);
    }


    @PostMapping("/signup-final")
//    @Transactional
    public Boolean signUpFinal(@RequestBody SignUpRequestFinal signUpRequestFinal)
    {

        SignUpRequest signUpRequest = convertIntoFirstPhase(signUpRequestFinal);

        SignUpFirstPhaseResponse signUpFirstPhaseResponse = signUpService.validateFirstPhase(signUpRequest);

        if (signUpFirstPhaseResponse.areAllValuesNull()) {
            boolean userAdded = signUpService.AddNewUser(signUpRequestFinal);
            if (userAdded) {

                return true;
            }
        }
        return false;
    }

    /**
     * In the second phase of signup, all the information of first phase will pass again which will converted into
     * first Phase and will check for first phase again.
     */
    private SignUpRequest convertIntoFirstPhase(SignUpRequestFinal signUpRequestFinal)
    {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setPassword(signUpRequestFinal.getPassword());
        signUpRequest.setUserEmail(signUpRequestFinal.getUserEmail());
        signUpRequest.setReenteredPassword(signUpRequestFinal.getReenteredPassword());
        signUpRequest.setUserType(signUpRequestFinal.getUserType());
        return signUpRequest;
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto)
    {
        Optional<Users> authenticatedUser = signUpService.authenticate(loginUserDto);

        if (authenticatedUser == null || authenticatedUser.isEmpty())
        {
            ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), "Bad credentials");
            errorDetail.setProperty("description", "The username or password is incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetail);
        }



        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginUserDto.getUserEmail());

        String jwtToken = jwtService.generateToken(authenticatedUser.get());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setRefreshToken(refreshToken.getToken());
        loginResponse.setUserEmail(loginUserDto.getUserEmail());


        return ResponseEntity.ok(loginResponse);
    }


    @PostMapping("/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUsers)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo);
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken()).build();
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
    }
}
