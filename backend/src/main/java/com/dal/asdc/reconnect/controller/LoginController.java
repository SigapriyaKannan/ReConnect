package com.dal.asdc.reconnect.controller;

import com.dal.asdc.reconnect.DTO.LoginResponse;
import com.dal.asdc.reconnect.repository.UsersRepository;
import com.dal.asdc.reconnect.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {


    @Autowired
    private UsersRepository repo;

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginResponse loginResponse) {
        boolean isLoginSuccessful = loginService.checkUser(loginResponse.getEmail(), loginResponse.getPassword());

        if (isLoginSuccessful) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
