package com.dal.asdc.reconnect.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpResponse
{
    private Boolean emailIncorrect;
    private Boolean passwordError;
    private Boolean repeatPasswordError;
    private Boolean emailAlreadyPresent;

    public boolean areAllValuesNull() {
        return emailIncorrect == null &&
                passwordError == null &&
                repeatPasswordError == null &&
                emailAlreadyPresent == null;
    }
}
