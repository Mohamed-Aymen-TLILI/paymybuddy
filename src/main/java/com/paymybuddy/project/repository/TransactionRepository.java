package com.paymybuddy.project.repository;

import com.paymybuddy.project.model.Transaction;
import com.paymybuddy.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Transaction repository.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    List<Transaction> findAllBySender(User user);

    List<Transaction> findAllByReceiver(User user);
}
