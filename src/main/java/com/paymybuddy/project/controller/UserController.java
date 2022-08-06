package com.paymybuddy.project.controller;

import com.paymybuddy.project.dto.UserDTO;
import com.paymybuddy.project.model.User;
import com.paymybuddy.project.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

public class UserController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    /**
     * Create new user in DB
     * @param user the user to be create in DB
     * @return 200 successful | 400 failed
     */
    @PostMapping(value = "/user")
    public ResponseEntity createUser(@RequestBody User user){
        // Create if user email isn't in DB
        if(userService.getByEmail(user.getEmail()).isEmpty()){
            userService.saveUser(user);
            LOGGER.info("Success create User");
            return ResponseEntity.ok(modelMapper.map(user, UserDTO.class));
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Get user by mail
     * @param email the user's email
     * @return a userDto, BAD_REQUEST if the user doesn't exist.
     */
    @GetMapping(value ="/getUser")
    public ResponseEntity<UserDTO> getByEmail(@RequestParam("email") String email){
        Optional<User> user = userService.getByEmail(email);
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        LOGGER.info("Success find user by email");
        return ResponseEntity.ok(modelMapper.map(user.get(), UserDTO.class));
    }

    /**
     * Update user pseudo
     * @param userDTO user's email and new nickname
     * @return 200 success | 400 failed
     */
    @PutMapping(value = "/user")
    public ResponseEntity<UserDTO> updatePseudo(@RequestBody UserDTO userDTO){
        if(userService.getByEmail(userDTO.getEmail()).isPresent()){
            userService.updateNickname(userDTO);
            LOGGER.info("Success update user pseudo");
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Add a friend, require two users
     * @param fromUser the owner's id
     * @param toUser the target's id
     * @return 201 success | 400 otherwise
     */
    @PutMapping(value = "/addFriend/{fromUser}/{toUser}")
    public ResponseEntity addFriend(@PathVariable Long fromUser, @PathVariable Long toUser){
        // If one user don't exist, return bad request
        if (userService.getUserById(fromUser).isEmpty() || userService.getUserById(toUser).isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User owner = userService.getUserById(fromUser).get();
        User target = userService.getUserById(toUser).get();
        // Save connection in DB
        if (!owner.getListFriend().contains(target)){
            userService.addFriend(owner, target);
            LOGGER.info("Add friend success");
            return ResponseEntity.status(201).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
