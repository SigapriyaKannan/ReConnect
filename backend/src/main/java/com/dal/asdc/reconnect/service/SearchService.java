package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.model.Company;
import com.dal.asdc.reconnect.model.UserDetails;
import com.dal.asdc.reconnect.repository.CompanyRepository;
import com.dal.asdc.reconnect.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final CompanyRepository companyRepository;


    private final UserDetailsRepository userDetailsRepository;

    public List<String> findUsernamesByCompanyName(String companyName) {
        Optional<Company> company = companyRepository.findByCompanyName(companyName);
        if (company.isPresent()) {
            return userDetailsRepository.findUsernamesByCompany(company.get());
        }
        return List.of(); // Return an empty list if the company is not found
    }
}
