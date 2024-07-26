package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.dto.Users.SearchResult;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final UsersRepository usersRepository;

    /**
     * This method is used to find the usernames by company name
     * @param companyName
     * @return List of usernames
     */

    public List<SearchResult> findUsernamesByCompanyName(String companyName) {
        Users currentUser = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<SearchResult> users = new ArrayList<>();
        if(currentUser.getUserType().getTypeID() == 1)
        {
            users = usersRepository.findUsersWithDetailsAndReferralStatusWithCompany(currentUser.getUserID(),2,companyName);
        }else
        {
            users = usersRepository.findUsersWithDetailsAndReferentStatusWithCompany(currentUser.getUserID(),1,companyName);
        }
        return users;
    }

    /**
     * This method is used to find all usernames by username
     * @param username
     * @return List of usernames
     */
    public List<SearchResult> findAllUsernames(String username) {
        Users currentUser = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<SearchResult> users = new ArrayList<>();
        if(currentUser.getUserType().getTypeID() == 1)
        {
            users = usersRepository.findUsersWithDetailsAndReferralStatusWithUserName(currentUser.getUserID(),2,username);
        }else
        {
            users = usersRepository.findUsersWithDetailsAndReferentStatusWithUserName(currentUser.getUserID(),1,username);
        }
        return users;
    }
}