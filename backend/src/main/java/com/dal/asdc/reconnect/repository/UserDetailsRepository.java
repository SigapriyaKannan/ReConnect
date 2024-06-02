package com.dal.asdc.reconnect.repository;
import com.dal.asdc.reconnect.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer>
{

}
