package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.DTO.Company.CompanyDTO;
import com.dal.asdc.reconnect.DTO.Helper.SkillsDTO;
import com.dal.asdc.reconnect.model.Company;
import com.dal.asdc.reconnect.model.Skills;
import com.dal.asdc.reconnect.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    public List<CompanyDTO> getAllCompanies() {
        List<CompanyDTO> listOfCompanies = new ArrayList<>();
        List<Company> listOfSkillsFromDatabase = companyRepository.findAll();

        for(Company company: listOfSkillsFromDatabase) {
            CompanyDTO companyDTO = new CompanyDTO(company.getCompanyId(), company.getCompanyName());
            listOfCompanies.add(companyDTO);
        }

        return listOfCompanies;
    }
}
