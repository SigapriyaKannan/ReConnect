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


    @GetMapping("/Test")
    public String Test()
    {
        Users User = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var senderEmail =   SecurityContextHolder.getContext().getAuthentication().getName();


        System.out.println(User.getUserType());


        var email =   SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(email);
        return  "I Should enter in the site";
    }




}
