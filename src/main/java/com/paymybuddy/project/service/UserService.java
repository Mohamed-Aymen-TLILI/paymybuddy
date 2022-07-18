package com.paymybuddy.project.service;

import com.paymybuddy.project.exception.NoSuchUserException;
import com.paymybuddy.project.exception.UserAlreadyExistException;
import com.paymybuddy.project.model.BankAccount;
import com.paymybuddy.project.model.User;
import com.paymybuddy.project.repository.BankAccountRepository;
import com.paymybuddy.project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The type User service.
 */
@Service
public class UserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    /**
     * Save user.
     *
     * @param user the user
     * @throws UserAlreadyExistException the user already exist exception
     */
    public void saveUser(User user) throws UserAlreadyExistException {
        LOGGER.info("Processing to save a new user in database");
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException("User already exist in database");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole("ROLE_USER");
        userRepository.save(user);
    }

    /**
     * Find user by id optional.
     *
     * @param id the id
     * @return the optional
     * @throws NoSuchUserException the user not found exception
     */
    public Optional<User> findUserById(int id) throws NoSuchUserException {
        LOGGER.info("Processing to find a user by id");

        if(!userRepository.existsById(id)) {
            throw new NoSuchUserException("User not found");
        }

        return userRepository.findById(id);
    }


    /**
     * Get total account balance by user id atomic reference.
     *
     * @param id the id
     * @return the atomic reference
     */
    public Double getTotalAccountBalanceByUserId(int id){
        LOGGER.info("Processing to get total account balance by user Id");

        BankAccount bankAccount = bankAccountRepository.findByAccountOwner_Id(id);
        return bankAccount.getBalance();
        }

}
