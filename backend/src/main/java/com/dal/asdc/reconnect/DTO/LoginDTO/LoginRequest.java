package com.dal.asdc.reconnect.DTO.LoginDTO;


import lombok.Data;

@Data
public class LoginRequest
{
    private String UserEmail;

    private String password;

}
