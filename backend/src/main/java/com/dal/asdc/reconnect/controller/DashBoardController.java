package com.dal.asdc.reconnect.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DashBoardController
{


    @GetMapping("/Manish")
    public String signUp()
    {
        var email =   SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println(email);

        return  "I Should enter in the site";
    }


}
