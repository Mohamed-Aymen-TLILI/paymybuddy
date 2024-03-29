package com.paymybuddy.project.service;

import com.paymybuddy.project.dto.TransactionDTO;
import com.paymybuddy.project.model.Transaction;
import com.paymybuddy.project.model.User;
import com.paymybuddy.project.repository.TransactionRepository;
import com.paymybuddy.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TransactionService {


    private UserRepository userRepository;


    private TransactionRepository transactionRepository;

    public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository ) {
        this.userRepository = userRepository;
        this.transactionRepository= transactionRepository;

    }

    /**
     * Check if payment authorized or not
     *
     * @param fromUser the user sending the money
     * @param toUser   the user receiving the money
     * @param total    the amount of money sent + fee
     * @return true authorized | false not authorized
     */
    private boolean authorizedPayment(User fromUser, User toUser, double total) {
        return fromUser.getAmount() > total && fromUser.getListFriend().contains(toUser);
    }

    /**
     * Process a payment
     *
     * @param transacDTO the transaction infos
     * @return true success | false failed
     */
    public boolean makePayment(TransactionDTO transacDTO) {
        // Verify if users exists and amount > 0
        if (userRepository.findById(transacDTO.getSenderId()).isEmpty() || userRepository.findById(transacDTO.getReceiverId()).isEmpty() ||
                transacDTO.getAmount() <= 0) {
            return false;
        }
        double amount = transacDTO.getAmount();
        double fare = amount * 0.05;
        double total = amount + fare;

        User fromUser = userRepository.findById(transacDTO.getSenderId()).get();
        User toUser = userRepository.findById(transacDTO.getReceiverId()).get();

        if (authorizedPayment(fromUser, toUser, total)) {
            fromUser.setAmount(fromUser.getAmount() - total);
            toUser.setAmount(toUser.getAmount() + amount);
            Transaction transaction = new Transaction();
            transaction.setSender(fromUser);
            transaction.setReceiver(toUser);
            transaction.setAmount(amount);
            transaction.setDate(new Date());
            transaction.setDescription(transacDTO.getDescription());
            transactionRepository.save(transaction);
            userRepository.save(fromUser);
            userRepository.save(toUser);
            return true;
        }
        return false;
    }

    /**
     * Find all transactions by user sender
     *
     * @param user the user
     * @return List of transactions
     */
    public List<Transaction> findAllByUserSender(User user) {
        return transactionRepository.findAllBySender(user);
    }

    /**
     * Find all transactions by user receiver
     *
     * @param user the user
     * @return List of transactions
     */
    public List<Transaction> findAllByUserReceiver(User user) {
        return transactionRepository.findAllByReceiver(user);
    }
}
