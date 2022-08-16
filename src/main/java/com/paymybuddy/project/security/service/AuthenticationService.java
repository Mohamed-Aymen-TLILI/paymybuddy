package com.paymybuddy.project.security.service;

import com.paymybuddy.project.exception.NoSuchUserException;
import com.paymybuddy.project.exception.UserAlreadyExistException;
import com.paymybuddy.project.model.User;
import com.paymybuddy.project.repository.UserRepository;
import com.paymybuddy.project.security.config.JwtTokenUtil;
import com.paymybuddy.project.security.model.request.JwtRequest;
import com.paymybuddy.project.security.model.response.JwtResponse;
import org.apache.commons.validator.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;


@Service()
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Override
    @Transactional
    public UserDetailsImpl loadUserByUsername(String email) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NoSuchUserException(email);
        }
        return UserDetailsImpl.build(user);
    }

    public JwtResponse createAuthenticationToken(@RequestBody JwtRequest jwtRequest) {
        checkValidMail(jwtRequest.getEmail());
        if (authenticate(jwtRequest.getEmail(), jwtRequest.getPassword())) {
            final UserDetailsImpl userDetails = loadUserByUsername(jwtRequest.getEmail());
            final String token = jwtTokenUtil.generateToken(userDetails);
            logger.info("Request login successful");
            return new JwtResponse(token, userDetails.getUsername(), userDetails.getId());
        }
        return null;
    }

    @Transactional
    public String save(JwtRequest registerRequest) {
        checkValidMail(registerRequest.getEmail());
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistException(registerRequest.getEmail());
        }
        User user = new User(registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()), registerRequest.getNickname());

        userRepository.save(user);
        logger.info("Request register successful");
        return "User registered successfully!";
    }

    private boolean authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new NoSuchUserException(username);
        }
        return true;
    }

    private void checkValidMail(String email) {
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new NoSuchUserException(email);
        }
    }
}

