package com.dal.asdc.reconnect.controller;

import com.dal.asdc.reconnect.DTO.City.CityDTO;
import com.dal.asdc.reconnect.DTO.City.CityRequestDTO;
import com.dal.asdc.reconnect.DTO.Mappers.CityMapper;
import com.dal.asdc.reconnect.DTO.Response;
import com.dal.asdc.reconnect.model.City;
import com.dal.asdc.reconnect.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cities")
public class CitiesController {
    @Autowired
    CityService cityService;
    @Autowired
    private CityMapper cityMapper;

    @GetMapping("/getAllCities")
    public ResponseEntity<?> getAllCities() {
        List<CityDTO> listOfAllCities = cityService.getAllCities();
        Response<List<CityDTO>> response = new Response<>(HttpStatus.OK.value(), "Fetched all cities", listOfAllCities);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllCities/{countryId}")
    public ResponseEntity<?> getAllCitiesByCountryId(@PathVariable int countryId) {
        List<CityDTO> listOfAllCities = cityService.getAllCitiesByCountryId(countryId);
        Response<List<CityDTO>> response = new Response<>(HttpStatus.OK.value(), "Fetched all cities", listOfAllCities);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCity/{cityId}")
    public ResponseEntity<?> getCityByCityId(@PathVariable int cityId) {
        City city = cityService.getCityById(cityId);
        if(city != null) {
            CityDTO cityDTO = cityMapper.mapCityToDTO(city);
            Response<CityDTO> response = new Response<>(HttpStatus.OK.value(), "Fetched City", cityDTO);
            return ResponseEntity.ok(response);
        } else {
            Response<?> response = new Response<>(HttpStatus.CONFLICT.value(), "City not found!", null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @PostMapping("/addCity")
    public ResponseEntity<?> addCity(@RequestBody CityRequestDTO cityRequestDTO){
        City existingCity = cityService.getCityByCityNameAndCountryId(cityRequestDTO.getCityName(), cityRequestDTO.getCountryId());
        if (existingCity != null) {
            Response<?> response = new Response<>(HttpStatus.CONFLICT.value(), "City already exists in the country", null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else {
            City newCity = cityService.addCity(cityRequestDTO.getCityName(), cityRequestDTO.getCountryId());
            Map<String, Integer> responseMap = new HashMap<>();
            responseMap.put("cityId", newCity.getCountryId());
            Response<Map<String, Integer>> response = new Response<>(HttpStatus.CREATED.value(), "City saved successfully", responseMap);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }
}
