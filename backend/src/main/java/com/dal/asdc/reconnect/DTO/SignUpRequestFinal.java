package com.dal.asdc.reconnect.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUpRequestFinal
{
    private int userType;
    private String userEmail;
    private String password;
    private String reenteredPassword;
    private int company;
    private int experience;
    private List<Integer> skill;
    private int country;
    private int city;
    private String resume;
    private String profile;
}
