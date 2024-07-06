package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.exception.CityNotFoundException;
import com.dal.asdc.reconnect.model.City;
import com.dal.asdc.reconnect.model.Country;
import com.dal.asdc.reconnect.repository.CityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

class CityServiceTest {
    @Mock
    CityRepository cityRepository;
    @InjectMocks
    CityService cityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetCityById() {
        // Mock data
        Country country = new Country();
        country.setCountryName("USA");
        country.setCountryId(1);
        City city = new City();
        city.setCityName("New York");
        city.setCityId(1);
        city.setCountry(country);
        Optional<City> optionalCity = Optional.of(city);

        // Mocking the behavior of cityRepository.findById(cityId)
        when(cityRepository.findById(1)).thenReturn(optionalCity);


        // Calling the method to be tested
        City result = cityService.getCityById(1);

        // Asserting the result
        Assertions.assertEquals(city, result);
    }

    @Test
    void testCityNotFoundException() {
        when(cityRepository.findById(2)).thenReturn(Optional.empty());

        Assertions.assertThrows(CityNotFoundException.class, () -> {
            cityService.getCityById(2);
        });
    }

    @Test
    void testGetCityByCityNameAndCountryId() {
        when(cityRepository.findCityByCityNameAndCountryCountryId(anyString(), anyInt())).thenReturn(new City());

        City result = cityService.getCityByCityNameAndCountryId("cityName", 0);
        Assertions.assertEquals(new City(), result);
    }
}