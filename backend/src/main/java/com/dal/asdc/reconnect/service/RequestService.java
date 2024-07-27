package com.dal.asdc.reconnect.service;


import com.dal.asdc.reconnect.dto.Request.Requests;
import com.dal.asdc.reconnect.enums.RequestStatus;
import com.dal.asdc.reconnect.model.ReferralRequests;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.repository.RequestRepository;
import com.dal.asdc.reconnect.repository.UserDetailsRepository;
import com.dal.asdc.reconnect.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RequestService {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    UsersRepository usersRepository;


    public List<Requests> getPendingRequestForReferent(String Sender) {
        Optional<Users> users = usersRepository.findByUserEmail(Sender);
        if (!users.isPresent()) {
            log.error("User not found with email");
        }
        int userID = users.get().getUserID();
        List<Integer> pendingRequestsID = requestRepository.findReferrerIdsByReferentIdAndStatusPending(userID);
        return userDetailsRepository.findRequestsByReferrerIds(pendingRequestsID);

    }


    public List<ReferralRequests> getAcceptedRequestForReferent(int userId) {
        return requestRepository.findByReferent_UserIDAndStatus(userId, RequestStatus.ACCEPTED);
    }


    public List<Requests> getPendingRequestForReferrer(String Sender) {
        Optional<Users> users = usersRepository.findByUserEmail(Sender);
        int userID = users.get().getUserID();

        List<Integer> pendingRequestsID = requestRepository.findReferentIdsByReferrerIdAndStatusPending(userID);

        return userDetailsRepository.findRequestsByReferrerIds(pendingRequestsID);
    }


    @Transactional
    public void acceptRequest(String Sender, int referentID) {
        Optional<Users> users = usersRepository.findByUserEmail(Sender);
        int refereeID = users.get().getUserID();
        requestRepository.updateStatusAndResponseDate(RequestStatus.ACCEPTED, LocalDateTime.now(), referentID, refereeID);
    }

    @Transactional
    public void requestRejected(String Sender, int referentID) {
        Optional<Users> users = usersRepository.findByUserEmail(Sender);
        int refereeID = users.get().getUserID();
        requestRepository.updateStatusAndResponseDate(RequestStatus.REJECTED, LocalDateTime.now(), referentID, refereeID);
    }

    @Transactional
    public boolean sendRequest(Integer referentId, Integer referrerId)
    {
        Users referent = usersRepository.findById(referentId).orElseThrow(() -> new IllegalArgumentException("Invalid referent ID"));
        Users referrer = usersRepository.findById(referrerId).orElseThrow(() -> new IllegalArgumentException("Invalid referrer ID"));

        Optional<ReferralRequests> existingRequest = requestRepository.findByReferrerAndReferent(referrer, referent);
        if (existingRequest.isPresent())
        {
            return false;
        }
        ReferralRequests referralRequest = new ReferralRequests();
        referralRequest.setReferent(referent);
        referralRequest.setReferrer(referrer);
        referralRequest.setStatus(RequestStatus.PENDING);
        referralRequest.setRequestDate(LocalDateTime.now());
        ReferralRequests savedRequest = requestRepository.save(referralRequest);
        return savedRequest != null;
    }

    public List<ReferralRequests> getAcceptedRequestForReferrer(int userID) {
        return requestRepository.findByReferrer_UserIDAndStatus(userID, RequestStatus.ACCEPTED);
    }
}
