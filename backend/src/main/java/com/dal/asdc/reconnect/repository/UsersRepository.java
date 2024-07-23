package com.dal.asdc.reconnect.repository;

import com.dal.asdc.reconnect.dto.Users.User;
import com.dal.asdc.reconnect.dto.Users.UserCompanySearch;
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

    @Query("SELECT new com.dal.asdc.reconnect.dto.Users.UserCompanySearch(u.userID, ud.userName, ud.profilePicture, " +
            "(SELECT rr.status FROM ReferralRequests rr WHERE rr.referrer = u AND rr.referent = :currentUser)) " +
            "FROM Users u " +
            "JOIN u.userDetails ud " +
            "WHERE LOWER(ud.company.companyName) LIKE LOWER(CONCAT('%', :company, '%')) " +
            "AND u.userType.typeID = :userTypeId")
    List<UserCompanySearch> findUsernamesByCompanyAndUserType(@Param("company") String company,
                                                              @Param("userTypeId") int userTypeId,
                                                              @Param("currentUser") Users currentUser);

    @Query("SELECT new com.dal.asdc.reconnect.dto.Users.User(u.userID, ud.userName, ud.company.companyName, ud.profilePicture, " +
            "(SELECT rr.status FROM ReferralRequests rr WHERE rr.referrer = u AND rr.referent = :currentUser)) " +
            "FROM Users u " +
            "JOIN u.userDetails ud " +
            "WHERE LOWER(ud.userName) LIKE LOWER(CONCAT('%', :username, '%')) " +
            "AND u.userType.typeID = :userTypeId")
    List<User> findUsernamesByUsernameAndUserType(@Param("username") String username,
                                                  @Param("userTypeId") int userTypeId,
                                                  @Param("currentUser") Users currentUser);
    
    Optional<Users> findByUserID(int userID);
}
