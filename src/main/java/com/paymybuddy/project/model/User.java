package com.paymybuddy.project.model;

import com.sun.istack.NotNull;
import lombok.Data;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name="user", indexes = @Index(name="user_email_idx",columnList = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 45)
    private String nickname;

    @Column(nullable = false, precision = 18, scale = 2)
    private double amount;

    @JoinTable(name="connections", joinColumns = {
            @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    }, inverseJoinColumns = {
            @JoinColumn(name = "target_id", referencedColumnName = "id", nullable = false)
    })
    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> listFriend;

}
