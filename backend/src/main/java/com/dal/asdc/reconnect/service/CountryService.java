package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.DTO.CountryResponseBody;
import com.dal.asdc.reconnect.model.Country;
import com.dal.asdc.reconnect.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CountryService {

    @Autowired
    CountryRepository countryRepository;


    public CountryResponseBody getCountryList()
    {
        CountryResponseBody countryResponseBody = new CountryResponseBody();

        List<Country> countryList = countryRepository.findAll();

        countryResponseBody.setCountries(countryList);

        return countryResponseBody;

    }
}
