package com.dal.asdc.reconnect.repository;
import com.dal.asdc.reconnect.DTO.Users.User;
import com.dal.asdc.reconnect.model.UserType;
import com.dal.asdc.reconnect.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer>
{
    Optional<Users> findByUserEmail(String email);

    @Query(value = "SELECT ud.UserName, u.TypeID FROM Users u " +
            "JOIN UserDetails ud ON u.UserID = ud.UserID " +
            "JOIN UserType ut ON u.TypeID = ut.TypeID " +
            "WHERE ut.TypeName = :typeName", nativeQuery = true)
    List<Object[]> findUserNameAndTypeIdByUserType(@Param("typeName") String typeName);

    Users findByResetToken(String resetToken);

}
