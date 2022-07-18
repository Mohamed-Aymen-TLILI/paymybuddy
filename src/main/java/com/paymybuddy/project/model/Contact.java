package com.paymybuddy.project.model;

import javax.persistence.*;

@Entity
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @ManyToOne
    @JoinColumn(name = "contact_user_id")
    private User contactUserId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;
}
