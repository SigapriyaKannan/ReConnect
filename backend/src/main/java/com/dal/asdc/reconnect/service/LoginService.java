package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    UsersRepository usersRepository;

    public boolean checkUser(String email, String password) {
        Optional<Users> optionalUser = usersRepository.findByUserEmail(email);

        if (!optionalUser.isPresent()) {
            return false;
        }

        Users user = optionalUser.get();

        return user.getPassword().equals(password);
    }
}
