package com.dal.asdc.reconnect.controller;

import com.dal.asdc.reconnect.DTO.Users.UserNameTypeIdDTO;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserService userService;


    @GetMapping("/getAllUsersByTypeId")
    public ResponseEntity<List<UserNameTypeIdDTO>> getUserNamesAndTypeIdsByUserType(@RequestParam int typeId) {
        List<Users> users = userService.getUsersByTypeId(typeId);
        if (users.size() == 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ArrayList<>());
        }
        List<UserNameTypeIdDTO> listOfUsers = new ArrayList<>();
        for (Users user : users) {
            UserNameTypeIdDTO tempUser = new UserNameTypeIdDTO(user.getUserID(), user.getUsername(), user.getUserType().getTypeID());
            listOfUsers.add(tempUser);
        }
        return ResponseEntity.ok(listOfUsers);
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
