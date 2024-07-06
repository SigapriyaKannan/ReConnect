package com.dal.asdc.reconnect.controller;

import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DashBoardController
{

    @Autowired
    JWTService jwtService;


    @GetMapping("/Manish")
    public String signUp()
    {
        Users User = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println(User.getUserType().getTypeID());

        System.out.println(User.getUserEmail());

        System.out.println(User.getUserID());

        var email =   SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(email);
        return  "I Should enter in the site";
    }




}
