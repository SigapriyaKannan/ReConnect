package com.dal.asdc.reconnect.controller;


import com.dal.asdc.reconnect.model.Company;
import com.dal.asdc.reconnect.model.UserDetails;
import com.dal.asdc.reconnect.service.CompanyService;
import com.dal.asdc.reconnect.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class SearchController {


    private final SearchService searchService;

    @GetMapping("/employees")
    public ResponseEntity<List<String>> getUsernamesByCompany(@RequestParam String companyName) {
        List<String> usernames = searchService.findUsernamesByCompanyName(companyName);
        return ResponseEntity.ok(usernames);
    }
}
