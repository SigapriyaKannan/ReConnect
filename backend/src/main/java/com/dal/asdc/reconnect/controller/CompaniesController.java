package com.dal.asdc.reconnect.controller;

import com.dal.asdc.reconnect.DTO.Company.CompanyDTO;
import com.dal.asdc.reconnect.DTO.Helper.SkillsDTO;
import com.dal.asdc.reconnect.DTO.Response;
import com.dal.asdc.reconnect.service.CompanyService;
import com.dal.asdc.reconnect.service.SkillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "http://localhost:4200")
public class CompaniesController {
    @Autowired
    CompanyService companyService;

    @GetMapping("/getAllCompanies")
    public ResponseEntity<?> getAllCompanies() {
        List<CompanyDTO> listOfCompanies = companyService.getAllCompanies();
        Response<List<CompanyDTO>> response = new Response<>(HttpStatus.OK.value(), "Fetched all companies", listOfCompanies);
        return ResponseEntity.ok(response);
    }
}
