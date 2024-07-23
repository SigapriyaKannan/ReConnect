package com.dal.asdc.reconnect.repository;

import com.dal.asdc.reconnect.dto.Users.User;
import com.dal.asdc.reconnect.model.Company;
import com.dal.asdc.reconnect.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUserDetailsUserName(String username);

    Users findByResetToken(String resetToken);

    Optional<Users> findByUserEmail(String email);

    List<Users> findAllUsersByUserTypeTypeID(int typeId);

    @Query("SELECT ud.userName FROM Users u JOIN u.userDetails ud WHERE ud.company = :company AND u.userType.typeID = :userTypeId")
    List<String> findUsernamesByCompanyAndUserType(@Param("company") Company company, @Param("userTypeId") int userTypeId);

    @Query("SELECT new com.dal.asdc.reconnect.dto.Users.User(ud.userName, ud.company.companyName) " +
            "FROM Users u " +
            "JOIN u.userDetails ud " +
            "WHERE LOWER(ud.userName) LIKE LOWER(CONCAT('%', :username, '%')) AND u.userType.typeID = :userTypeId")
    List<User> findUsernamesByUsernameAndUserType(@Param("username") String username, @Param("userTypeId") int userTypeId);
    
    Optional<Users> findByUserID(int userID);
}
