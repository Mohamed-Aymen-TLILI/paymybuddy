package com.paymybuddy.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.project.data.TestData;
import com.paymybuddy.project.dto.UserDTO;
import com.paymybuddy.project.model.User;
import com.paymybuddy.project.repository.UserRepository;
import com.paymybuddy.project.security.model.request.JwtRequest;
import com.paymybuddy.project.security.service.AuthenticationService;
import com.paymybuddy.project.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserControllerTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @MockBean
    AuthenticationService authenticationService;

    @LocalServerPort
    private int port;

    final String CREATE_USER_URL = "api/addFriend/" + 2;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(new TestingAuthenticationToken(TestData.getPrincipal(), null, Collections.emptyList()));
        SecurityContextHolder.setContext(securityContext);
    }

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders httpHeaders = new HttpHeaders();

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private User createUser(String email){
        User user = new User();
        user.setNickname("Nicolas");
        user.setEmail(email);
        user.setPassword("password");
        return user;
    }

    @Test
    @DisplayName("Create user in DB with sold to zero")
    public void createUserInDBWithSoldToZero(){
        User newUser = createUser("test@nicolas.test");
        assertEquals(userService.getByEmail(newUser.getEmail()).getEmail(), "test@nicolas.test");

        HttpEntity<User> entity = new HttpEntity<>(newUser, httpHeaders);
        ResponseEntity response = restTemplate.exchange(
                createURLWithPort("/paymybuddy/register"), HttpMethod.POST, entity, String.class
        );

        assertEquals(0, userRepository.findByEmail(newUser.getEmail()).getAmount());
        // Verify if password is crypt in DB
        assertNotEquals(newUser.getPassword(),userService.getByEmail(newUser.getEmail()).getPassword() );
    }

    @Test
    @DisplayName("Create user return bad request is user email is already in DB")
    public void createUserReturnBadRequestIfEmailIsAlreadyInDB(){
        User newUser = createUser("test@nicolas.com");
        HttpEntity<User> entity = new HttpEntity<>(newUser, httpHeaders);
        restTemplate.exchange(
                createURLWithPort("/paymybuddy/register"), HttpMethod.POST, entity, String.class
        );
        assertEquals(2, userService.getAll().size());
        ResponseEntity response = restTemplate.exchange(
                createURLWithPort("/paymybuddy/register"), HttpMethod.POST, entity, String.class
        );
        // Verify that no other user was created
        assertEquals(2, userService.getAll().size());
    }

    @Test
    @DisplayName("Update user pseudo if user is in DB")
    public void updatePseudoIfUserAlreadyExistInDB(){
        User newUser = createUser("nicolas@test.com");
        HttpEntity<User> entity = new HttpEntity<>(newUser, httpHeaders);
        ResponseEntity response = restTemplate.exchange(
                createURLWithPort("/paymybuddy/register"), HttpMethod.POST, entity, String.class
        );

        User alreadyInDb = userService.getByEmail(newUser.getEmail());
        assertEquals("Nicolas",alreadyInDb.getNickname());
        UserDTO pseudoUser = new UserDTO();
        pseudoUser.setEmail(newUser.getEmail());
        pseudoUser.setNickname("Nicolas");

        HttpEntity<UserDTO> entity2 = new HttpEntity<>(pseudoUser, httpHeaders);
        response = restTemplate.exchange(
                createURLWithPort("api/update/user"), HttpMethod.PUT, entity2, String.class
        );
        assertEquals(pseudoUser.getNickname(), userService.getByEmail(newUser.getEmail()).getNickname());
    }


    @Test
    @DisplayName("Add friend OK if the two users exist in DB")
    public void addFriendOkIfTwoUsersExistInDb(){
        User user1 = createUser("nicolas@test.com");
        User user2 = createUser("jacques@test.com");

        HttpEntity<User> entity = new HttpEntity<>(user1, httpHeaders);
        restTemplate.exchange(
                createURLWithPort("/paymybuddy/register"), HttpMethod.POST, entity, String.class
        );

        entity = new HttpEntity<>(user2, httpHeaders);
        restTemplate.exchange(
                createURLWithPort("/paymybuddy/register"), HttpMethod.POST, entity, String.class
        );
        // Check if no connection exist
        user1 = userService.getByEmail("nicolas@test.com");
        assertTrue(user1.getListFriend().isEmpty());

        // Create connection between user1 and user2
        HttpEntity entity2 = new HttpEntity(httpHeaders);
        ResponseEntity response = restTemplate.exchange(
                createURLWithPort(CREATE_USER_URL), HttpMethod.PUT, entity, String.class
        );

        // Check if connection is created
        user1 = userService.getByEmail("nicolas@test.com");
        user2 = userService.getByEmail("jacques@test.com");
        assertTrue(user1.getListFriend().contains(user2));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
