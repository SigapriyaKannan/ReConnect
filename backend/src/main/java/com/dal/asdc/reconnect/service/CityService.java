package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.DTO.City.CityDTO;
import com.dal.asdc.reconnect.model.City;
import com.dal.asdc.reconnect.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;


    /**
     * Retrieves the cities associated with a given country ID.
     * @param countryId the ID of the country for which cities are to be retrieved.
     * @return a CityResponseBody object containing the list of cities for the specified country.
     */

    public List<CityDTO> getAllCitiesByCountryId(int countryId) {
        List<CityDTO> listOfCities = new ArrayList<>();
        List<City> listOfCitiesFromDatabase = cityRepository.findCitiesByCountryId(countryId);
        for(City city: listOfCitiesFromDatabase) {
            CityDTO cityDTO = new CityDTO(city.getCityId(), city.getCityName(), countryId);
            listOfCities.add(cityDTO);
        }
        return listOfCities;
    }

    public List<CityDTO> getAllCities() {
        List<CityDTO> listOfCities = new ArrayList<>();
        List<City> listOfCitiesFromDatabase = cityRepository.findAll();
        for(City city: listOfCitiesFromDatabase) {
            CityDTO cityDTO = new CityDTO(city.getCityId(), city.getCityName(), city.getCountryId());
            listOfCities.add(cityDTO);
        }
        return  listOfCities;
    }

    public City getCityById(int cityId) {
        Optional<City> city = cityRepository.findById(cityId);
        return city.orElse(null);
    }

    public City getCityByCityName(String cityName) {
        return cityRepository.findCityByCityName(cityName);
    }

    public City getCityByCityNameAndCountryId(String cityName, int countryId) {
        return cityRepository.findCityByCityNameAndCountryId(cityName, countryId);
    }

    public City addCity(String cityName, int countryId) {
        City newCity = new City();
        newCity.setCityName(cityName);
        newCity.setCountryId(countryId);
        return cityRepository.save(newCity);
    }
}
