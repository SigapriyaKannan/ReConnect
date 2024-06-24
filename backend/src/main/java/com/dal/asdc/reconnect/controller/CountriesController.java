package com.dal.asdc.reconnect.controller;

import com.dal.asdc.reconnect.DTO.Helper.CountryDTO;
import com.dal.asdc.reconnect.DTO.Mappers.CountryMapper;
import com.dal.asdc.reconnect.DTO.Response;
import com.dal.asdc.reconnect.model.Country;
import com.dal.asdc.reconnect.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/countries")
public class CountriesController {
    @Autowired
    CountryService countryService;

    @Autowired
    private CountryMapper countryMapper;

    /**
     * Retrieves the list of all countries.
     * Returns a response containing the list of countries along with status and message.
     * @return Response object containing the list of countries.
     */
    @GetMapping("/getAllCountries")
    public ResponseEntity<?> getAllCountries() {
        List<CountryDTO> listOfCountries = countryService.getCountryList();
        Response<List<CountryDTO>> response = new Response<>(HttpStatus.OK.value(), "Fetched all countries", listOfCountries);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCountry/{countryId}")
    public ResponseEntity<?> getAllCountries(@PathVariable int countryId) {
        Country country = countryService.getCountryById(countryId);
        if(country != null) {
            CountryDTO countryDTO = countryMapper.mapCountryToDTO(country);
            Response<CountryDTO> response = new Response<>(HttpStatus.OK.value(), "Fetched country", countryDTO);
            return ResponseEntity.ok(response);
        } else {
            Response<?> response = new Response<>(HttpStatus.CONFLICT.value(), "Country not found!", null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @PostMapping("/addCountry")
    public ResponseEntity<?> addCountry(@RequestBody CountryDTO countryDTO){
        Country existingCountry = countryService.getCountryByName(countryDTO.getCountryName());
        if (existingCountry != null) {
            Response<?> response = new Response<>(HttpStatus.CONFLICT.value(), "Country already exists", null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else {
            Country newCountry = countryService.addCountry(countryDTO.getCountryName());
            Map<String, Integer> responseMap = new HashMap<>();
            responseMap.put("countryId", newCountry.getCountryId());
            Response<Map<String, Integer>> response = new Response<>(HttpStatus.CREATED.value(), "Country saved successfully", responseMap);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }
}
