package com.paymybuddy.project.repository;

import com.paymybuddy.project.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,String> {


    /**
     * Update balance by iban.
     *
     * @param iban   the iban
     * @param amount the amount
     */
    @Modifying
    @Query("UPDATE BankAccount b SET b.balance = :amount WHERE b.iban = :iban")
    public void updateBalanceByIban(String iban, double amount);

    BankAccount findByAccountOwner_Id(int id);
}
