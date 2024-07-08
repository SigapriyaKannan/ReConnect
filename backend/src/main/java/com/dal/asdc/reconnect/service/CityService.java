package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.DTO.City.CityDTO;
import com.dal.asdc.reconnect.DTO.City.CityRequestDTO;
import com.dal.asdc.reconnect.exception.CityNotFoundException;
import com.dal.asdc.reconnect.exception.CountryNotFoundException;
import com.dal.asdc.reconnect.model.City;
import com.dal.asdc.reconnect.model.Country;
import com.dal.asdc.reconnect.repository.CityRepository;
import com.dal.asdc.reconnect.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;


    /**
     * Retrieves the cities associated with a given country ID.
     * @param country the country for which cities are to be retrieved.
     * @return a list of CityDTO objects containing for the specified country.
     */
    public List<CityDTO> getAllCitiesByCountry(Country country) {
        List<CityDTO> listOfCities = new ArrayList<>();
        List<City> listOfCitiesFromDatabase = cityRepository.findCitiesByCountryCountryId(country.getCountryId());
        for(City city: listOfCitiesFromDatabase) {
            CityDTO cityDTO = new CityDTO(city.getCityId(), city.getCityName(), country);
            listOfCities.add(cityDTO);
        }
        return listOfCities;
    }

    /**
     * Retrieves a list of CityDTO objects representing all cities.
     *
     * @return List of CityDTO objects containing city information.
     */
    public List<CityDTO> getAllCities() {
        List<CityDTO> listOfCities = new ArrayList<>();
        List<City> listOfCitiesFromDatabase = cityRepository.findAll();
        for(City city: listOfCitiesFromDatabase) {
            CityDTO cityDTO = new CityDTO(city.getCityId(), city.getCityName(), city.getCountry());
            listOfCities.add(cityDTO);
        }
        return  listOfCities;
    }

    /**
     * Retrieves a city by its ID.
     *
     * @param cityId The ID of the city to retrieve.
     * @return City object if found, otherwise null.
     */
    public City getCityById(int cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() -> new CityNotFoundException("City not found with ID: " + cityId));
    }

    /**
     * Retrieves a city by its name.
     *
     * @param cityName The name of the city to retrieve.
     * @return City object if found, otherwise null.
     */
    public City getCityByCityName(String cityName) {
        Optional<City> city = cityRepository.findCityByCityName(cityName);
        return city.orElse(null);
    }

    /**
     * Retrieves a city by its name and country ID.
     *
     * @param cityName The name of the city to retrieve.
     * @param countryId The ID of the country the city belongs to.
     * @return City object if found, otherwise null.
     */
    public City getCityByCityNameAndCountryId(String cityName, int countryId) {
        return cityRepository.findCityByCityNameAndCountryCountryId(cityName, countryId);
    }

    /**
     * Adds a new city to the database.
     *
     * @param cityName The name of the city to add.
     * @param country The Country object representing the country the city belongs to.
     * @return The newly added City object.
     */
    public City addCity(String cityName, Country country) {
        City newCity = new City();
        newCity.setCityName(cityName);
        newCity.setCountry(country);
        return cityRepository.save(newCity);
    }


    /**
     * Modifies an existing city based on the provided CityRequestDTO.
     *
     * @param cityDTO The CityRequestDTO object containing the updated city information.
     */
    public void modifyCity(CityRequestDTO cityDTO) {
        City existingCity = getCityById(cityDTO.getCityId());
        existingCity.setCityName(cityDTO.getCityName());

        Country newCountry = countryRepository.findById(cityDTO.getCountryId())
                .orElseThrow(() -> new CountryNotFoundException("Country not found with ID: " + cityDTO.getCountryId()));

        existingCity.setCountry(newCountry);
        cityRepository.save(existingCity);
    }
    /**
     * Deletes a city by its ID.
     *
     * @param cityId The ID of the city to delete.
     * @return True if the city is successfully deleted, false otherwise.
     */
    public boolean deleteCity(int cityId) {
        Optional<City> cityFromDatabase = cityRepository.findById(cityId);
        if(cityFromDatabase.isPresent()) {
            cityRepository.deleteById(cityId);
            return true;
        } else {
            return false;
        }
    }
}
