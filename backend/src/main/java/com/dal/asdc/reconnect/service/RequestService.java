package com.dal.asdc.reconnect.service;


import com.dal.asdc.reconnect.DTO.Request.Requests;
import com.dal.asdc.reconnect.repository.RequestRepository;
import com.dal.asdc.reconnect.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    UserDetailsRepository userDetailsRepository;


    public List<Requests> getPendingRequestForReferent(int userID)
    {
        List<Integer> pendingRequestsID = requestRepository.findReferrerIdsByReferentIdAndStatusPending(userID);

        List<Requests> pendingRequests = userDetailsRepository.findRequestsByReferrerIds(pendingRequestsID);

        return pendingRequests;

    }


    public List<Requests> getAcceptedRequestForReferent(int userID)
    {
        List<Integer> pendingRequestsID = requestRepository.findReferrerIdsByReferentIdAndStatusAccepted(userID);

        List<Requests> pendingRequests = userDetailsRepository.findRequestsByReferrerIds(pendingRequestsID);

        return pendingRequests;
    }

    public List<Requests> getPendingRequestForReferrer(int userID)
    {
        List<Integer> pendingRequestsID = requestRepository.findReferentIdsByReferrerIdAndStatusPending(userID);

        List<Requests> pendingRequests = userDetailsRepository.findRequestsByReferrerIds(pendingRequestsID);

        return pendingRequests;
    }
}
