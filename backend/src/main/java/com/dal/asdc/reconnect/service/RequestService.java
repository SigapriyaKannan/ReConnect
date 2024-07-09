package com.dal.asdc.reconnect.service;


import com.dal.asdc.reconnect.dto.Request.Requests;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.repository.RequestRepository;
import com.dal.asdc.reconnect.repository.UserDetailsRepository;
import com.dal.asdc.reconnect.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    UsersRepository usersRepository;


    public List<Requests> getPendingRequestForReferent(String Sender)
    {
        Optional<Users> users = usersRepository.findByUserEmail(Sender);
        int userID = users.get().getUserID();
        List<Integer> pendingRequestsID = requestRepository.findReferrerIdsByReferentIdAndStatusPending(userID);

        List<Requests> pendingRequests = userDetailsRepository.findRequestsByReferrerIds(pendingRequestsID);

        return pendingRequests;

    }


    public List<Requests> getAcceptedRequestForReferent(String Sender)
    {
        Optional<Users> users = usersRepository.findByUserEmail(Sender);
        int userID = users.get().getUserID();

        List<Integer> pendingRequestsID = requestRepository.findReferrerIdsByReferentIdAndStatusAccepted(userID);

        List<Requests> pendingRequests = userDetailsRepository.findRequestsByReferrerIds(pendingRequestsID);

        return pendingRequests;
    }

    public List<Requests> getPendingRequestForReferrer(String Sender)
    {
        Optional<Users> users = usersRepository.findByUserEmail(Sender);
        int userID = users.get().getUserID();

        List<Integer> pendingRequestsID = requestRepository.findReferentIdsByReferrerIdAndStatusPending(userID);

        List<Requests> pendingRequests = userDetailsRepository.findRequestsByReferrerIds(pendingRequestsID);

        return pendingRequests;
    }
}
