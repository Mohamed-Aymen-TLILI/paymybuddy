package com.paymybuddy.project.service;

import com.paymybuddy.project.model.Bank;
import com.paymybuddy.project.model.User;
import com.paymybuddy.project.repository.BankRepository;
import com.paymybuddy.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional( propagation = Propagation.REQUIRES_NEW )
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Create an object BankTransaction
     * @param user user for the transaction
     * @param iban account number of bank account
     * @param amount amount of money transferred, positive or negative
     * @return the object BankTransaction
     */
    public Bank createBankTransaction(User user, String iban, double amount){
        Bank transac = new Bank();
        transac.setUser(user);
        transac.setIban(iban);
        transac.setAmount(amount);
        transac.setDate(new Date());
        return transac;
    }

    /**
     * Save the transaction in dB
     * @param transac the transaction
     * @return the transaction in dB
     */
    public Bank save(Bank transac){
        return bankRepository.save(transac);
    }

    /**
     * Process the bankTransaction
     * @param bankTransac the bankTransaction
     * @return true success | false failed
     */
    public boolean processTransaction(Bank bankTransac){
        // If amount 0 return false
        if(bankTransac.getAmount() == 0){
            return false;
        }

        User user = bankTransac.getUser();
        double absAmount = Math.abs(bankTransac.getAmount());

        // If amount > 0 add money to account
        // If < 0 withdraw money
        // If user don't have enough money return false
        if(bankTransac.getAmount() > 0){
            user.setAmount(user.getAmount() + absAmount);
        } else if (bankTransac.getAmount() < 0 && absAmount <= user.getAmount()) {
            user.setAmount(user.getAmount() - absAmount);
        } else {
            return false;
        }

        userRepository.save(user);
        return true;
    }

    /**
     * Find all transactions by user
     * @param user the user
     * @return a list of transactions
     */
    public List<Bank> findByUser(User user){
        return bankRepository.findAllByUser(user);
    }
}
