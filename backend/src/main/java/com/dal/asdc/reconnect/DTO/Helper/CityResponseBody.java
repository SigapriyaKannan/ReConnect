package com.dal.asdc.reconnect.DTO.Helper;

import com.dal.asdc.reconnect.model.City;
import lombok.Data;

import java.util.List;


@Data
public class CityResponseBody
{
    List<CityDTO> cities;
}
