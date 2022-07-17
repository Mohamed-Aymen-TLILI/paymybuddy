package com.paymybuddy.project.model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * The type Fee.
 */
@Entity
@Table(name = "fee")
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fee_id")
    private int feeId;
    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "amount")
    private double amount;
    @Column(name = "rate100")
    private double rate100;

    @Column(name = "reference_transaction")
    private int transactionReference;

    @Column(name = "iban_account")
    private String account;
}
