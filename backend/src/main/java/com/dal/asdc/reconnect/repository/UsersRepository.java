package com.dal.asdc.reconnect.repository;

import com.dal.asdc.reconnect.dto.Users.User;
import com.dal.asdc.reconnect.model.Company;
import com.dal.asdc.reconnect.model.UserDetails;
import com.dal.asdc.reconnect.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUserEmail(String email);

    Users findByResetToken(String resetToken);

    List<Users> findAllUsersByUserTypeTypeID(int typeId);


    @Query("SELECT ud.userName FROM Users u JOIN UserDetails ud WHERE ud.company = :company AND u.userType.typeID = :userTypeId")
    List<String> findUsernamesByCompanyAndUserType(@Param("company") Company company, @Param("userTypeId") int userTypeId);

    @Query("SELECT new com.dal.asdc.reconnect.dto.Users.User(ud.userName, ud.company.companyName) " + "FROM UserDetails ud " + "JOIN Users u " + "WHERE LOWER(ud.userName) LIKE LOWER(CONCAT('%', :username, '%')) AND u.userType.typeID = :userTypeId")
    List<User> findUsernamesByUsernameAndUserType(@Param("username") String username, @Param("userTypeId") int userTypeId);


}
