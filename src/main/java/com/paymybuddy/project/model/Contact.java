package com.paymybuddy.project.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@IdClass(ContactId.class)
@Table(name = "contact")
public class Contact {

    @NotNull
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;


    @NotNull
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_friend_id", nullable = false)
    private User ami;
}
