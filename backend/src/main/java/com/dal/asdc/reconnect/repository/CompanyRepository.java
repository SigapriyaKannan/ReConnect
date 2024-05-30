package com.dal.asdc.reconnect.repository;

import com.dal.asdc.reconnect.model.Comapany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Comapany,Integer>
{

}
