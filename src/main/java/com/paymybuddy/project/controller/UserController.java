package com.paymybuddy.project.controller;

import com.paymybuddy.project.dto.UserDTO;
import com.paymybuddy.project.dto.UserRequestDTO;
import com.paymybuddy.project.exception.NoSuchUserException;
import com.paymybuddy.project.model.User;
import com.paymybuddy.project.security.model.request.JwtRequest;
import com.paymybuddy.project.security.model.response.JwtResponse;
import com.paymybuddy.project.security.service.AuthenticationService;
import com.paymybuddy.project.security.service.UserDetailsImpl;
import com.paymybuddy.project.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserController {

    @Autowired
    private AuthenticationService userDetailsService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    /**
     * Create new user in DB
     * @return 200 successful | 400 failed
     */
    @PostMapping(value = "/paymybuddy/register")
    public ResponseEntity<String> Register(@RequestBody JwtRequest registerRequest){
        return ResponseEntity.ok(userDetailsService.save(registerRequest));
    }

    /**
     * login user in app
     * @return 200 successful | 400 failed
     */
    @PostMapping(value = "/paymybuddy/login")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest jwtRequest) {
        JwtResponse authenticationToken = userDetailsService.createAuthenticationToken(jwtRequest);
        return authenticationToken == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(authenticationToken);
    }

    /**
     * Get user by mail
     * @param email the user's email
     * @return a userDto, BAD_REQUEST if the user doesn't exist.
     */
    @GetMapping(value ="api/getuserbyemail")
    public UserRequestDTO getByEmail(@RequestParam String email, Authentication authentication){
        User user = userService.getByEmail(email);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if(user == null || user.getEmail().equals(userDetails.getUsername()) ) {
            LOGGER.info("fail find user by email");
            return null;
        }
        LOGGER.info("Success find user by email");
        return modelMapper.map(user, UserRequestDTO.class);
    }

    /**
     * Get user by mail
     * @return a userDto, BAD_REQUEST if the user doesn't exist.
     */
    @GetMapping(value ="api/getuserbyid")
    public ResponseEntity<UserRequestDTO> getById( Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.findUserById(userDetails.getId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        LOGGER.info("Success find user by id");
        return ResponseEntity.ok(modelMapper.map(user, UserRequestDTO.class));
    }

    /**
     * Get users by authentication
     * @return list User, BAD_REQUEST if the user doesn't exist.
     */
    @GetMapping(value ="api/getallcontactbyuser")
    public List<UserRequestDTO> getAllContact(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long fromUser = userDetails.getId();
        Optional<User> user = userService.getUserById(fromUser);
        if(user.isPresent()) {

        LOGGER.info("Success find users");
        return user.get().getListFriend().stream().map(u -> new UserRequestDTO(u.getId(), u.getEmail(), u.getNickname(), u.getAmount())).collect(Collectors.toList());}
        throw new NoSuchUserException("User contacts not found");
    }

    /**
     * Update user pseudo
     * @param userDTO user's email and new nickname
     * @return 200 success | 400 failed
     */
    @PutMapping(value = "api/update/user")
    public ResponseEntity<UserDTO> updatePseudo(@RequestBody UserDTO userDTO, Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String fromUser = userDetails.getUsername();
        if(userService.getByEmail(fromUser) != null){
            userService.updateNickname(userDTO);
            LOGGER.info("Success update user pseudo");
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Add a friend, require two users
     * @param authentication the owner's id
     * @param toUser the target's id
     * @return 201 success | 400 otherwise
     */
    @PutMapping(value = "api/addFriend/{toUser}")
    public ResponseEntity addFriend(@PathVariable Long toUser, Authentication authentication){
        // If one user don't exist, return bad request
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long fromUser = userDetails.getId();
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

    /** Delete a friend, require two users
     *
     * @param authentication the owner's id
     * @param toUser the target's id
     * @return 200 success | 400 otherwise
     */
    @PutMapping(value = "api/removeFriend/{toUser}")
    public ResponseEntity removeFriend(@PathVariable Long toUser, Authentication authentication){
        // If one user don't exist, return bad request
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long fromUser = userDetails.getId();
        if (userService.getUserById(fromUser).isEmpty() || userService.getUserById(toUser).isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User owner = userService.getUserById(fromUser).get();
        User target = userService.getUserById(toUser).get();
        // Delete connection
        if (owner.getListFriend().contains(target)){
            userService.removeFriend(owner, target);
            LOGGER.info("Remove friend success");
            return ResponseEntity.ok().build();
        }
        LOGGER.info(owner.getListFriend().toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
