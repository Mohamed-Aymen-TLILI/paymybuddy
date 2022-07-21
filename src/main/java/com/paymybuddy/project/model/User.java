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
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Email
    @Size(max = 100)
    @Column(length = 100,nullable = false)
    private String email;

    @NotNull
    @Size(max=30)
    @Column(length = 30,nullable = false)
    private String firstname;

    @NotNull
    @Size(max=60)
    @Column(length = 60,nullable = false)
    private String lastname;

    @NotNull
    @Size(max=80)
    @Column(length = 80,nullable = false)
    private String password;

    @Column(name = "user_role")
    private String userRole;

    @OneToMany(mappedBy = "user")
    private List<Contact> contactList = new ArrayList<>();

}
