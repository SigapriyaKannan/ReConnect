package com.dal.asdc.reconnect.controller;

import com.dal.asdc.reconnect.dto.Response;
import com.dal.asdc.reconnect.dto.userdetails.UserDetailsRequest;
import com.dal.asdc.reconnect.dto.userdetails.UserDetailsResponse;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.service.FileService;
import com.dal.asdc.reconnect.service.ProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dal.asdc.reconnect.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private FileService fileService;

    @Autowired
    private RequestService requestService;

    @GetMapping
    public ResponseEntity<UserDetailsResponse> getUserDetails(@RequestParam String userId) {
        UserDetailsResponse userDetails = profileService.getUserDetailsByUserID(Integer.parseInt(userId));
        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/updateUserDetails")
    public ResponseEntity<UserDetailsResponse> updateUserDetails(
            @RequestPart("userDetails") String userDetailsJson,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            @RequestPart(value = "resume", required = false) MultipartFile resume) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        UserDetailsRequest userDetailsRequest = objectMapper.readValue(userDetailsJson, UserDetailsRequest.class);
        int userId = Integer.parseInt(userDetailsRequest.getUserId());

        UserDetailsResponse updatedUserDetails = profileService.updateUserDetails(userDetailsRequest);

        if (profilePicture != null) {
            fileService.uploadProfilePicture(userId, profilePicture);
        }
        if (resume != null) {
            fileService.uploadResume(userId, resume);
        }

        return ResponseEntity.ok(updatedUserDetails);
    }

    @GetMapping("/resume")
    public ResponseEntity<byte[]> getResume(@RequestParam String userId) throws IOException {
        byte[] resume = fileService.getResume(Integer.parseInt(userId));
        return ResponseEntity.ok(resume);
    }

    @GetMapping("/profilePicture")
    public ResponseEntity<byte[]> getProfilePicture(@RequestParam String userId) throws IOException {
        byte[] profilePicture = fileService.getProfilePicture(Integer.parseInt(userId));
        return ResponseEntity.ok(profilePicture);
    }

    @GetMapping("/sendRequest")
    public ResponseEntity<?> sendRequest(@RequestParam Integer userID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users sender = (Users) authentication.getPrincipal();
        boolean response =  requestService.sendRequest(sender.getUserID(), userID);
        if(response)
        {
            return ResponseEntity.ok(new Response<>(HttpStatus.OK.value(), "Request Sent Successfully", response));
        }else
        {
            return ResponseEntity.ok(new Response<>(HttpStatus.OK.value(), "Request Already Sent", response));
        }
    }
}
