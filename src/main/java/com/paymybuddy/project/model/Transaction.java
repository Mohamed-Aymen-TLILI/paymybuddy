package com.paymybuddy.project.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reference")
    private int reference;

    @NotNull
    @Positive(message = "Amount should be positive")
    @Column(name = "amount")
    private double amount;

    @Size(max = 50,message = "Message should be maximum 50 characters")
    @Column(name = "message")
    private String message;

    @Column(name = "date")
    private LocalDateTime date;

    @NotNull(message = "Debtor is mandatory")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "debtor")
    private BankAccount debtor;

    @NotNull(message = "Creditor is mandatory")
    @OneToOne
    @JoinColumn(name = "creditor")
    private BankAccount creditor;
}
