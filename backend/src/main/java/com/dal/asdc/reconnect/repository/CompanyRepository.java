package com.dal.asdc.reconnect.repository;

import com.dal.asdc.reconnect.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Integer>
{
Company findCompanyByCompanyName(String companyName);
}
