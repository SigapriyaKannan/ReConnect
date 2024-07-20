package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.dto.Users.User;
import com.dal.asdc.reconnect.model.Company;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.repository.CompanyRepository;
import com.dal.asdc.reconnect.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
            Users currentUser = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            int userTypeToShow = currentUser.getUserType().getTypeID() == 1 ? 2 : 1;
            return userDetailsRepository.findUsernamesByCompanyAndUserType(company.get(), userTypeToShow);
        }
        return List.of();
    }

    public List<User> findAllUsernames(String username) {
        Users currentUser = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userTypeToShow = currentUser.getUserType().getTypeID() == 1 ? 2 : 1;
        return userDetailsRepository.findUsernamesByUsernameAndUserType(username, userTypeToShow);
    }
}