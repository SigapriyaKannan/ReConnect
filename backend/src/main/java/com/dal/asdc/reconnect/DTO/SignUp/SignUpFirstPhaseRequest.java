package com.dal.asdc.reconnect.DTO.SignUp;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class SignUpFirstPhaseRequest
{
    private int userType;
    private String email;
    private String password;
    private String reenteredPassword;
}
