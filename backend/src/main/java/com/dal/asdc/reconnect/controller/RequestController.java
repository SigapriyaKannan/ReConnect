package com.dal.asdc.reconnect.controller;


import com.dal.asdc.reconnect.dto.Request.Requests;
import com.dal.asdc.reconnect.dto.Request.UpdateRequest;
import com.dal.asdc.reconnect.dto.Response;
import com.dal.asdc.reconnect.model.ReferralRequests;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.repository.UsersRepository;
import com.dal.asdc.reconnect.service.RequestService;
import com.dal.asdc.reconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class RequestController
{

    @Autowired
    RequestService requestService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    UserService userService;

    @GetMapping("/getPendingRequest")
    public ResponseEntity<?> getPendingRequest()
    {
        Users users = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var senderEmail =   users.getUserEmail();

        int userRole = users.getUserType().getTypeID();

        List<Requests> requestDTO;

        if(userRole == 1)
        {
            requestDTO = requestService.getPendingRequestForReferent(senderEmail);
        }else
        {
            requestDTO =  requestService.getPendingRequestForReferrer(senderEmail);
        }

        if(requestDTO != null)
        {
            Response<List<Requests>> response = new Response<>(HttpStatus.OK.value(), "Fetched Requests", requestDTO);
            return ResponseEntity.ok(response);
        } else {
            Response<?> response = new Response<>(HttpStatus.NOT_FOUND.value(), "Request Not Found!", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }



    @GetMapping("/getAcceptedConnections")
    public ResponseEntity<?> getAcceptedRequestForReferent()
    {
        var senderEmail =   SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> user = usersRepository.findByUserEmail(senderEmail);
        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else {
        List<ReferralRequests> referralRequests = requestService.getAcceptedRequestForReferent(user.get().getUserID());
            if (referralRequests.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ArrayList<>());
            } else {
                List<Requests> requestDTO = new ArrayList<>();
                for(ReferralRequests referralRequest: referralRequests) {
                    Requests tempRequest = new Requests();
                    tempRequest.setUserId(referralRequest.getReferrer().getUserID());
                    tempRequest.setName(referralRequest.getReferrer().getUsername());
                    tempRequest.setProfile("profilePicture.png");
                    requestDTO.add(tempRequest);
                }
                Response<List<Requests>> response = new Response<>(HttpStatus.OK.value(), "Fetched Requests", requestDTO);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        }
    }


    @PostMapping("/updateRequestStatus")
    public ResponseEntity<?> updateRequestStatus(@RequestBody UpdateRequest updateRequest)
    {
        var senderEmail =   SecurityContextHolder.getContext().getAuthentication().getName();
        int referentID = updateRequest.getUserId();
        if(updateRequest.isStatus())
        {
            requestService.acceptRequest(senderEmail,referentID);
        }else
        {
            requestService.requestRejected(senderEmail,referentID);
        }
        Response<String> response = new Response<>(HttpStatus.OK.value(), "Request Status Updated", null);
        return ResponseEntity.ok(response);
    }




}
