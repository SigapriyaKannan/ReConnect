package com.dal.asdc.reconnect.DTO;

import com.dal.asdc.reconnect.model.Country;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class CountryResponseBody
{
    List<Country> countries;
}
