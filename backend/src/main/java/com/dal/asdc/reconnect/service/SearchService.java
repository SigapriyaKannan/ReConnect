package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.dto.Users.User;
import com.dal.asdc.reconnect.dto.Users.UserCompanySearch;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final UsersRepository usersRepository;

    public List<UserCompanySearch> findUsernamesByCompanyName(String companyName) {
        Users currentUser = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userTypeToShow = currentUser.getUserType().getTypeID() == 1 ? 2 : 1;
        return usersRepository.findUsernamesByCompanyAndUserType(companyName, userTypeToShow, currentUser);
    }

    public List<User> findAllUsernames(String username) {
        Users currentUser = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userTypeToShow = currentUser.getUserType().getTypeID() == 1 ? 2 : 1;
        return usersRepository.findUsernamesByUsernameAndUserType(username, userTypeToShow, currentUser);
    }
}