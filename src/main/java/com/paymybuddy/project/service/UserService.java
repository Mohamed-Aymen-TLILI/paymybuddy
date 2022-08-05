package com.paymybuddy.project.service;

import com.paymybuddy.project.exception.NoSuchUserException;
import com.paymybuddy.project.exception.UserAlreadyExistException;
import com.paymybuddy.project.model.User;
import com.paymybuddy.project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
     * Gets user by id.
     *
     * @param id the id
     * @return the user by id
     */
    public User getUserById(int id) throws NoSuchUserException {

        LOGGER.info("Processing to find a user by id");

        if(!userRepository.existsById(id)) {
            throw new NoSuchUserException("User not found");
        }

        return userRepository.getById(id);
    }

    /**
     * Find user by email user.
     *
     * @param email the email
     * @return the user
     */
    public User findUserByEmail(String email) {
        LOGGER.info("Processing to find a user by email");
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if(user.getEmail().equals(email)) {
                return user;
            }
        }

        return null;
    }


    /**
     * Remove user by id.
     *
     * @param id the id
     */
    public void removeUserById(int id) throws NoSuchUserException {
        LOGGER.info("Processing to remove user by id");

        if(!userRepository.existsById(id)) {
            throw new NoSuchUserException("User not found");
        }
        userRepository.deleteById(id);
    }

}
