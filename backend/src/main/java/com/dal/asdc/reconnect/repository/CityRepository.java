package com.dal.asdc.reconnect.repository;

import com.dal.asdc.reconnect.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Integer>
{

    List<City> findByCountryCountryId(int countryId);

}
