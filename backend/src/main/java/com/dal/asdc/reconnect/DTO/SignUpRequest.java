package com.dal.asdc.reconnect.DTO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class SignUpRequest
{
    private int userType;
    private String userEmail;
    private String password;
    private String reenteredPassword;
}
