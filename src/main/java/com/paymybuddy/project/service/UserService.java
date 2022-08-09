package com.paymybuddy.project.service;

import com.paymybuddy.project.dto.UserDTO;
import com.paymybuddy.project.exception.NoSuchUserException;
import com.paymybuddy.project.exception.UserAlreadyExistException;
import com.paymybuddy.project.model.User;
import com.paymybuddy.project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public User  saveUser(User user) throws UserAlreadyExistException {
        LOGGER.info("Processing to save a new user in database");
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException("User already exist in database");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setListFriend(new ArrayList<>());
         return userRepository.save(user);
    }

    /**
     * Update a user's nickname
     * @param UserDto updated infos
     */
    public void updateNickname (UserDTO UserDto){
        Optional<User> opt = userRepository.findByEmail(UserDto.getEmail());
        if(opt.isPresent()){
            User inDB = opt.get();
            if(!inDB.getNickname().equals(UserDto.getNickname())){
                inDB.setNickname(UserDto.getNickname());
                userRepository.save(inDB);
            }
        }
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
    public Optional<User> getUserById(Long id) throws NoSuchUserException {

        LOGGER.info("Processing to find a user by id");

        if(userRepository.findById(id).isEmpty()) {
            throw new NoSuchUserException("User not found");
        }

        return userRepository.findById(id);
    }

    /**
     * Find a user by email
     * @param email the email
     * @return optional of a user
     */
    public Optional<User> getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    /**
     * Add a friend in the list
     * @param owner user owning the connection
     * @param target target of the connection
     */
    public void addFriend(User owner, User target){
        owner.getListFriend().add(target);
        userRepository.save(owner);
    }

    /**
     * Delete a friend in the list
     * @param owner user owning the connection
     * @param target target of the connection
     */
    public void removeFriend(User owner, User target){
        owner.getListFriend().remove(target);
        userRepository.save(owner);
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
