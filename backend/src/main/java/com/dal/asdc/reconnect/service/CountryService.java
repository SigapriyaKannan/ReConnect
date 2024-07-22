package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.dto.Helper.CountryDTO;
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
     *
     * @return a CountryResponseBody object containing the list of all countries.
     */
    public List<CountryDTO> getCountryList() {
        List<CountryDTO> listOfCountries = new ArrayList<>();
        List<Country> listOfCountriesFromDatabase = countryRepository.findAll();
        for (Country country : listOfCountriesFromDatabase) {
            CountryDTO countryDTO = new CountryDTO(country.getCountryId(), country.getCountryName());
            listOfCountries.add(countryDTO);
        }
        return listOfCountries;
    }

    /**
     * Adds a new country with the given country name.
     *
     * @param countryName The name of the country to add.
     * @return The newly added Country object.
     */
    public Country addCountry(String countryName) {
        Country country = new Country();
        country.setCountryName(countryName);
        countryRepository.save(country);
        return country;
    }

    /**
     * Modifies an existing country with the details from the provided CountryDTO.
     *
     * @param country The CountryDTO object containing the updated details of the country.
     * @return The modified Country object.
     */
    public Country modifyCountry(CountryDTO country) {
        Optional<Country> countryFromDatabase = countryRepository.findById(country.getCountryId());
        if (countryFromDatabase.isPresent()) {
            Country existingCountry = countryFromDatabase.get();
            existingCountry.setCountryName(country.getCountryName());
            countryRepository.save(existingCountry);
            return existingCountry;
        } else {
            return null;
        }
    }

    /**
     * Deletes a country by its ID.
     *
     * @param countryId The ID of the country to delete.
     * @return True if the country is successfully deleted, false otherwise.
     */
    public boolean deleteCountry(int countryId) {
        Optional<Country> countryFromDatabase = countryRepository.findById(countryId);
        if (countryFromDatabase.isPresent()) {
            countryRepository.deleteById(countryId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves a country by its name.
     *
     * @param countryName The name of the country to retrieve.
     * @return The Country object with the specified name.
     */
    public Country getCountryByName(String countryName) {
        return countryRepository.findCountryByCountryName(countryName);
    }

    /**
     * Retrieves a country by its ID.
     *
     * @param countryId The ID of the country to retrieve.
     * @return The Country object with the specified ID, or null if not found.
     */
    public Country getCountryById(int countryId) {
        Optional<Country> country = countryRepository.findById(countryId);
        return country.orElse(null);
    }
}
