package com.dal.asdc.reconnect.dto.LoginDto;


import lombok.Data;

@Data
public class LoginRequest
{
    private String UserEmail;

    private String password;

}
