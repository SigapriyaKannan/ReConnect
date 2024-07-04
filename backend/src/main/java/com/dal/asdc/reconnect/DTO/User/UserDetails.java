package com.dal.asdc.reconnect.DTO.User;

import lombok.Data;

@Data
public class UserDetails {
    String username;
    String email;
    int userId;
    int role;
}
