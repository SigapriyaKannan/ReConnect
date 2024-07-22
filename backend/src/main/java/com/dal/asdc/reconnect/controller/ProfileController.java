package com.dal.asdc.reconnect.controller;

import com.dal.asdc.reconnect.dto.userdetails.UserDetailsRequest;
import com.dal.asdc.reconnect.dto.userdetails.UserDetailsResponse;
import com.dal.asdc.reconnect.service.FileService;
import com.dal.asdc.reconnect.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<UserDetailsResponse> getUserDetails() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetailsResponse userDetails = profileService.getUserDetailsByEmail(email);
        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/update")
    public ResponseEntity<UserDetailsResponse> updateUserDetails(@RequestBody UserDetailsRequest userDetails) {
        UserDetailsResponse updatedUserDetails = profileService.updateUserDetails(userDetails);
        return ResponseEntity.ok(updatedUserDetails);
    }

    @PostMapping("/uploadResume")
    public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int userId = profileService.getUserIdByEmail(username);
        fileService.uploadResume(userId, file);
        return ResponseEntity.ok("Resume uploaded successfully.");
    }

    @PostMapping("/uploadProfilePicture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int userId = profileService.getUserIdByEmail(username);
        fileService.uploadProfilePicture(userId, file);
        return ResponseEntity.ok("Profile picture uploaded successfully.");
    }

    @GetMapping("/resume")
    public ResponseEntity<byte[]> getResume() throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int userId = profileService.getUserIdByEmail(username);
        byte[] resume = fileService.getResume(userId);
        return ResponseEntity.ok(resume);
    }

    @GetMapping("/profilePicture")
    public ResponseEntity<byte[]> getProfilePicture() throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int userId = profileService.getUserIdByEmail(username);
        byte[] profilePicture = fileService.getProfilePicture(userId);
        return ResponseEntity.ok(profilePicture);
    }
}
