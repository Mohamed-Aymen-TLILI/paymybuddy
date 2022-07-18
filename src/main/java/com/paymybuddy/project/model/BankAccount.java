package com.paymybuddy.project.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * The type Bank account.
 */
@Entity
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @Column(name = "iban")
    @NotEmpty(message = "Iban is mandatory")
    @Size(min = 27, max = 27, message = "Iban contains 27 characters")
    private String iban;
    @Column(name = "bank_establishment")
    private String bankEstablishment;
    @Column(name = "bic")
    @NotEmpty(message = "Bic is mandatory")
    @Size(min = 11, max = 11, message = "Bic contains 11 characters")
    private String bic;
    @Column(name = "balance")
    private double balance;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User accountOwner;

    public Double getBalance() {
        return balance;
    }
}
