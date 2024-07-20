package com.dal.asdc.reconnect.controller;

import com.dal.asdc.reconnect.dto.Response;
import com.dal.asdc.reconnect.dto.Users.User;
import com.dal.asdc.reconnect.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/companies/users")
    public ResponseEntity<?> getUsernamesByCompany(@RequestParam String companyName) {
        try {
            List<String> usernames = searchService.findUsernamesByCompanyName(companyName);
            if (usernames.isEmpty()) {
                Response<?> response = new Response<>(HttpStatus.NOT_FOUND.value(), "No users found for the given company", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else {
                Response<List<String>> response = new Response<>(HttpStatus.OK.value(), "Users fetched successfully", usernames);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Response<?> response = new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while processing your request", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> searchUsernames(@RequestParam(required = false) String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                Response<?> response = new Response<>(HttpStatus.NOT_FOUND.value(), "No users found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            List<User> users = searchService.findAllUsernames(username);

            if (users.isEmpty()) {
                Response<?> response = new Response<>(HttpStatus.NOT_FOUND.value(), "No users found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else {
                Response<List<User>> response = new Response<>(HttpStatus.OK.value(), "Users fetched successfully", users);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Response<?> response = new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while processing your request", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}