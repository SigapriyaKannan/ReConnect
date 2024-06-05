package com.dal.asdc.reconnect.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DashBoardController
{


    @GetMapping("/Manish")
    public String signUp()
    {
        return  "I Should Not enter in the site";
    }


}
