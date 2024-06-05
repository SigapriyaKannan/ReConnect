package com.dal.asdc.reconnect.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpSecondPhaseResponse
{


    private int status;
    private String message;
    private SignUpFirstPhaseBody body;

}
