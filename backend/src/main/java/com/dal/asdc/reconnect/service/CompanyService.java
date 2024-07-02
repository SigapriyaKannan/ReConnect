package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.DTO.Company.CompanyDTO;
import com.dal.asdc.reconnect.model.Company;
import com.dal.asdc.reconnect.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    /**
     * Retrieves the list of all companies.
     *
     * @return a list of CompanyDTO objects containing the details of all companies.
     */
    public List<CompanyDTO> getAllCompanies() {
        List<CompanyDTO> listOfCompanies = new ArrayList<>();
        List<Company> listOfSkillsFromDatabase = companyRepository.findAll();

        for(Company company: listOfSkillsFromDatabase) {
            CompanyDTO companyDTO = new CompanyDTO(company.getCompanyId(), company.getCompanyName());
            listOfCompanies.add(companyDTO);
        }

        return listOfCompanies;
    }

    /**
     * Adds a new company with the given company name.
     *
     * @param companyName The name of the company to add.
     * @return The newly added Company object.
     */
    public Company addCompany(String companyName) {
        Company company = new Company();
        company.setCompanyName(companyName);
        companyRepository.save(company);
        return company;
    }

    /**
     * Modifies an existing company with the details from the provided CompanyDTO.
     *
     * @param company The CompanyDTO object containing the updated details of the company.
     * @return The modified Company object.
     */
    public Company modifyCompany(CompanyDTO company) {
        Optional<Company> companyFromDatabase = companyRepository.findById(company.getCompanyId());
        if(companyFromDatabase.isPresent()) {
            Company existingCompany = companyFromDatabase.get();
            existingCompany.setCompanyName(company.getCompanyName());
            companyRepository.save(existingCompany);
            return existingCompany;
        } else {
            return null;
        }
    }

    /**
     * Deletes a company by its ID.
     *
     * @param companyId The ID of the company to delete.
     * @return True if the company is successfully deleted, false otherwise.
     */
    public boolean deleteCompany(int companyId) {
        Optional<Company> companyFromDatabase = companyRepository.findById(companyId);
        if(companyFromDatabase.isPresent()) {
            companyRepository.deleteById(companyId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves a company by its name.
     *
     * @param companyName The name of the company to retrieve.
     * @return The Company object with the specified name.
     */
    public Company getCompanyByName(String companyName) {
        return companyRepository.findCompanyByCompanyName(companyName);
    }

    /**
     * Retrieves a company by its ID.
     *
     * @param companyId The ID of the company to retrieve.
     * @return The Company object with the specified ID, or null if not found.
     */
    public Company getCompanyById(int companyId) {
        Optional<Company> company = companyRepository.findById(companyId);
        return company.orElse(null);
    }

}