package com.dal.asdc.reconnect.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Users")
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private int userID;

    @Column(name = "UserEmail", unique = true, nullable = false)
    private String userEmail;

    @Column(name = "Password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "TypeID", nullable = false)
    private UserType userType;

}
