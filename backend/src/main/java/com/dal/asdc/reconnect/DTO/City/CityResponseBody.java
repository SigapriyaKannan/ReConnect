package com.dal.asdc.reconnect.DTO.City;

import com.dal.asdc.reconnect.model.City;
import lombok.Data;

import java.util.List;


@Data
public class CityResponseBody
{
    private List<City> listOfCities;
    private City city;
}
