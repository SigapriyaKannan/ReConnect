package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.DTO.Helper.CityDTO;
import com.dal.asdc.reconnect.DTO.Helper.CityResponseBody;
import com.dal.asdc.reconnect.model.City;
import com.dal.asdc.reconnect.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;


    /**
     * Retrieves the cities associated with a given country ID.
     * @param countryId the ID of the country for which cities are to be retrieved.
     * @return a CityResponseBody object containing the list of cities for the specified country.
     */

    public CityResponseBody getCitiesByCountryId(int countryId) {

        CityResponseBody cityResponseBody = new CityResponseBody();
        List<City> cities = cityRepository.findByCountryCountryId(countryId);

        List<CityDTO> cityDTOs = cities.stream()
                .map(city -> new CityDTO(city.getCityId(), city.getCityName()))
                .toList();

        cityResponseBody.setCities(cityDTOs);

        return cityResponseBody;
    }
}
