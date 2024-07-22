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

    public List<UsersPerCountryDTO> getAllUsersPerCountry() {
        return dashboardRepository.getAllUsersPerCountry();
    }

    public List<UsersPerTypeDTO> getAllUsersPerType() {
        return dashboardRepository.getAllUsersPerType();
    }

    public List<UsersPerCompanyDTO> getAllUsersPerCompany() {
        return dashboardRepository.getAllUsersPerCompany(PageRequest.of(0, Integer.MAX_VALUE));
    }

    public List<UsersPerCompanyDTO> getTopFiveCompanies() {
        return dashboardRepository.getAllUsersPerCompany(PageRequest.of(0, 5));
    }
}
