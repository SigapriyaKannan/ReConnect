package com.dal.asdc.reconnect.DTO.City;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CityDTO {
    private int cityId;
    private String cityName;
    private int countryId;
}
