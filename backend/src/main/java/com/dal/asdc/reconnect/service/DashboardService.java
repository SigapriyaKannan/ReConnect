package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.dto.Dashboard.UsersPerCompanyDTO;
import com.dal.asdc.reconnect.dto.Dashboard.UsersPerCountryDTO;
import com.dal.asdc.reconnect.dto.Dashboard.UsersPerTypeDTO;
import com.dal.asdc.reconnect.repository.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {
    @Autowired
    private DashboardRepository dashboardRepository;

    /**
     * Get all users per country
     * @return List of UsersPerCountryDTO
     */
    public List<UsersPerCountryDTO> getAllUsersPerCountry() {
        return dashboardRepository.getAllUsersPerCountry();
    }
    /**
     * Get all users per type
     * @return List of UsersPerTypeDTO
     */
    public List<UsersPerTypeDTO> getAllUsersPerType() {
        return dashboardRepository.getAllUsersPerType();
    }
    /**
     * Get all users per company
     * @return List of UsersPerCompanyDTO
     */
    public List<UsersPerCompanyDTO> getAllUsersPerCompany() {
        return dashboardRepository.getAllUsersPerCompany(PageRequest.of(0, Integer.MAX_VALUE));
    }
    /**
     * Get top five companies
     * @return List of UsersPerCompanyDTO
     */
    public List<UsersPerCompanyDTO> getTopFiveCompanies() {
        return dashboardRepository.getAllUsersPerCompany(PageRequest.of(0, 5));
    }
}
