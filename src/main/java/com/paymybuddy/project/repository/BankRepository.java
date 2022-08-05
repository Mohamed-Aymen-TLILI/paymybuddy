package com.paymybuddy.project.repository;

import com.paymybuddy.project.model.Bank;
import com.paymybuddy.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface BankRepository extends JpaRepository<Bank, Long> {
    List<Bank> findAllByUser(User user);
}
