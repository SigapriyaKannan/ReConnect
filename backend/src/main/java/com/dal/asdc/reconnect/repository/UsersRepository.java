package com.dal.asdc.reconnect.repository;
import com.dal.asdc.reconnect.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Integer>
{
    Users findByUserEmail(String email);

    Users findByResetToken(String resetToken);

}
