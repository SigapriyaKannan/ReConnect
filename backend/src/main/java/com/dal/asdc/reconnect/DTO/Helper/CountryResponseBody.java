package com.dal.asdc.reconnect.DTO.Helper;

import com.dal.asdc.reconnect.model.Country;
import lombok.Data;

import java.util.List;


@Data
public class CountryResponseBody
{
    private List<Country> listOfCountries;
    private Country country;
}
