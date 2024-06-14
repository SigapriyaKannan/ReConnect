package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.DTO.Helper.CityDTO;
import com.dal.asdc.reconnect.DTO.Helper.CountryDTO;
import com.dal.asdc.reconnect.DTO.Helper.CountryResponseBody;
import com.dal.asdc.reconnect.model.City;
import com.dal.asdc.reconnect.model.Country;
import com.dal.asdc.reconnect.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CountryService {

    @Autowired
    CountryRepository countryRepository;

    /**
     * Retrieves the list of all countries.
     * @return a CountryResponseBody object containing the list of all countries.
     */
    public List<CountryDTO> getCountryList()
    {
        List<CountryDTO> listOfCountries = new ArrayList<>();
        List<Country> listOfCountriesFromDatabase = countryRepository.findAll();
        for(Country country: listOfCountriesFromDatabase) {
            CountryDTO countryDTO = new CountryDTO(country.getCountryId(), country.getCountryName());
            listOfCountries.add(countryDTO);
        }
        return listOfCountries;
    }

    public Country addCountry(String countryName) {
        Country country = new Country();
        country.setCountryName(countryName);
        countryRepository.save(country);
        return country;
    }

    public Country getCountryByName(String countryName) {
        return countryRepository.findCountryByCountryName(countryName);
    }

    public Country getCountryById(int countryId) {
        Optional<Country> country = countryRepository.findById(countryId);
        return country.orElse(null);
    }
}
