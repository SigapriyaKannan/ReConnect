package com.dal.asdc.reconnect.repository;
import com.dal.asdc.reconnect.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer>
{
    Optional<Users> findByUserEmail(String email);

}
