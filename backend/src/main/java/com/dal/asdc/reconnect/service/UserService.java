package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.DTO.Users.UserNameTypeIdDTO;
import com.dal.asdc.reconnect.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;

    public List<UserNameTypeIdDTO> getUserNameAndTypeIdByUserType(String typeName) {
        List<Object[]> results = usersRepository.findUserNameAndTypeIdByUserType(typeName);
        return results.stream()
                .map(result -> new UserNameTypeIdDTO((String) result[0], (Integer) result[1]))
                .collect(Collectors.toList());
    }

    public void deleteUser(int userId) {
        usersRepository.deleteById(userId);
    }
}
