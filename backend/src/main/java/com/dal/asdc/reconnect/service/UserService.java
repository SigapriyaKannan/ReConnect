package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;

    /**
     * This method is used to get all the users
     * @param typeId
     * @return List of users
     */

    public List<Users> getUsersByTypeId(int typeId) {
        return usersRepository.findAllUsersByUserTypeTypeID(typeId);
    }

    /**
     * This method is used to delete the user
     * @param userId
     */
    public void deleteUser(int userId) {
        usersRepository.deleteById(userId);
    }
}