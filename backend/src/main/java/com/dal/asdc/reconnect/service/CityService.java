package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.DTO.CityResponseBody;
import com.dal.asdc.reconnect.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public CityResponseBody getCitiesByCountryId(int countryId) {

        CityResponseBody cityResponseBody = new CityResponseBody();

        cityResponseBody.setCities(cityRepository.findByCountryCountryId(countryId));
        return cityResponseBody;
    }
}
