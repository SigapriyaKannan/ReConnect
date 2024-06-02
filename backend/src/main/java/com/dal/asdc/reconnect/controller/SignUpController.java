package com.dal.asdc.reconnect.controller;


import com.dal.asdc.reconnect.DTO.SignUpRequest;
import com.dal.asdc.reconnect.DTO.SignUpRequestFinal;
import com.dal.asdc.reconnect.DTO.SignUpResponse;
import com.dal.asdc.reconnect.service.SignUpService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SignUpController {

    @Autowired
    SignUpService signUpService;


    @PostMapping("/signup")
    public SignUpResponse signUp(@RequestBody SignUpRequest signUpRequest)
    {
        return signUpService.validateFirstPhase(signUpRequest);

    }


    @PostMapping("/signup-final")
    @Transactional
    public boolean signUpFinal(@RequestBody SignUpRequestFinal signUpRequestFinal)
    {

        SignUpRequest signUpRequest = convertIntoFirstPhase(signUpRequestFinal);

        SignUpResponse signUpResponse = signUpService.validateFirstPhase(signUpRequest);

        if(signUpResponse.areAllValuesNull())
        {
            return signUpService.AddNewUser(signUpRequestFinal);
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
}
