package com.dal.asdc.reconnect.controller;

import com.dal.asdc.reconnect.DTO.Users.UserNameTypeIdDTO;
import com.dal.asdc.reconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserService userService;


    @GetMapping("/usernames-and-typeids")
    public ResponseEntity<List<UserNameTypeIdDTO>> getUserNamesAndTypeIdsByUserType(@RequestParam String typeName) {
        List<UserNameTypeIdDTO> userNameTypeIdDTOList = userService.getUserNameAndTypeIdByUserType(typeName);
        return ResponseEntity.ok(userNameTypeIdDTOList);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
