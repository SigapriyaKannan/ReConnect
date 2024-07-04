package com.dal.asdc.reconnect.repository;
import com.dal.asdc.reconnect.model.Company;
import com.dal.asdc.reconnect.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer>
{
    @Query("SELECT userName FROM UserDetails WHERE company = :company")
    List<String> findUsernamesByCompany(Company company);

}
