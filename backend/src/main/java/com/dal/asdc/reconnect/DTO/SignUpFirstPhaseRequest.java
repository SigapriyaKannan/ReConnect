package com.dal.asdc.reconnect.DTO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class SignUpFirstPhaseRequest
{
    private int userType;
    private String userEmail;
    private String password;
    private String reenteredPassword;
}
