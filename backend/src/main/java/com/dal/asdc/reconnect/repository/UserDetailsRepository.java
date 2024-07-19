package com.dal.asdc.reconnect.repository;
import com.dal.asdc.reconnect.dto.Request.Requests;
import com.dal.asdc.reconnect.model.Company;
import com.dal.asdc.reconnect.model.UserDetails;
import com.dal.asdc.reconnect.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer>
{
    @Query("SELECT userName FROM UserDetails WHERE company = :company")
    List<String> findUsernamesByCompany(Company company);

    @Query("SELECT new com.dal.asdc.reconnect.dto.Request.Requests(ud.userName, ud.profilePicture, ud.users.userID) " +
            "FROM UserDetails ud " +
            "WHERE ud.users.userID IN :referrerIds")
    List<Requests> findRequestsByReferrerIds(@Param("referrerIds") List<Integer> referrerIds);

    UserDetails findByUsers(Optional<Users> user);

}
