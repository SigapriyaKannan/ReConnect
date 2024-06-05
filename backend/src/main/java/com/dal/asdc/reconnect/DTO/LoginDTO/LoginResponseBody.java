package com.dal.asdc.reconnect.DTO.LoginDTO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponseBody
{

    private String token;

    private long expiresIn;

    private String UserEmail;

    private String refreshToken;
}
