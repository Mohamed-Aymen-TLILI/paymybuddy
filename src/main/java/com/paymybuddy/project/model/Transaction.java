package com.paymybuddy.project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    private String description;

    @Column(nullable = false, precision = 18, scale = 2)
    private double amount;

    @Column(nullable = false)
    private Date date;

    public Transaction(User sender, User receiver, String description, double amount, Date date) {
        this.sender = sender;
        this.receiver = receiver;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

}
