package com.dal.asdc.reconnect.controller;


import com.dal.asdc.reconnect.DTO.Request.Requests;
import com.dal.asdc.reconnect.dto.Response;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class RequestController
{

    @Autowired
    RequestService requestService;

    @GetMapping("/getPendingRequestForReferent")
    public ResponseEntity<?> getPendingRequestForReferent()
    {
        Users User = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Requests> requestDTO = requestService.getPendingRequestForReferent(User.getUserID());
        if(requestDTO != null) {
            Response<List<Requests>> response = new Response<>(HttpStatus.OK.value(), "Fetched Requests", requestDTO);
            return ResponseEntity.ok(response);
        } else {
            Response<?> response = new Response<>(HttpStatus.NOT_FOUND.value(), "Request Not Found!", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/getAcceptedRequestForReferent")
    public ResponseEntity<?> getAcceptedRequestForReferent()
    {
        Users User = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Requests> requestDTO = requestService.getAcceptedRequestForReferent(User.getUserID());
        if(requestDTO != null) {
            Response<List<Requests>> response = new Response<>(HttpStatus.OK.value(), "Fetched Requests", requestDTO);
            return ResponseEntity.ok(response);
        } else {
            Response<?> response = new Response<>(HttpStatus.NOT_FOUND.value(), "Request Not Found!", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @GetMapping("/getPendingRequestForReferrer")
    public ResponseEntity<?> getPendingRequestForReferrer()
    {
        Users User = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Requests> requestDTO = requestService.getPendingRequestForReferrer(User.getUserID());
        if(requestDTO != null) {
            Response<List<Requests>> response = new Response<>(HttpStatus.OK.value(), "Fetched Requests", requestDTO);
            return ResponseEntity.ok(response);
        } else {
            Response<?> response = new Response<>(HttpStatus.NOT_FOUND.value(), "Request Not Found!", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }




}
