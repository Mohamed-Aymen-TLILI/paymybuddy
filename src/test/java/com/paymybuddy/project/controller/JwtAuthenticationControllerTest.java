package com.paymybuddy.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.project.config.TestConfig;
import com.paymybuddy.project.data.TestData;
import com.paymybuddy.project.exception.NoSuchUserException;
import com.paymybuddy.project.exception.UserAlreadyExistException;
import com.paymybuddy.project.security.config.JwtAuthenticationEntryPoint;
import com.paymybuddy.project.security.config.JwtTokenUtil;
import com.paymybuddy.project.security.model.request.JwtRequest;
import com.paymybuddy.project.security.model.response.JwtResponse;
import com.paymybuddy.project.security.service.AuthenticationService;
import com.paymybuddy.project.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@Import({TestConfig.class, JwtAuthenticationEntryPoint.class})
public class JwtAuthenticationControllerTest {

    @MockBean
    AuthenticationService authenticationService;


    @MockBean
    private UserService userService;


    @MockBean
    JwtTokenUtil jwtTokenUtil;

    @MockBean
    AuthenticationManager authenticationManager;

    @Autowired
    MockMvc mvc;

    /**
     * Important pour tester le JSON de la réponse HTTP
     */
    private JacksonTester<JwtResponse> jsonTester;


    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(new TestingAuthenticationToken(TestData.getPrincipal(), null, Collections.emptyList()));
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createAuthenticationTokenShouldReturnOk() throws Exception {
        // GIVEN
        JwtResponse response = new JwtResponse("accesToken", "email", 1L);
        Mockito.when(authenticationService.createAuthenticationToken(Mockito.any(JwtRequest.class))).thenReturn(response);
        // WHEN
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/paymybuddy/login").contentType(MediaType.APPLICATION_JSON).content("{\"email\": \"test@test.fr\", \"password\": \"pwd\"}")
                .with(csrf()).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // THEN
        Assertions.assertThat(result.getResponse().getContentAsString())
                .isEqualTo(jsonTester.write(response).getJson());
    }

    @Test
    void createAuthenticationTokenShouldReturnNotContent() throws Exception {
        // GIVEN
        JwtResponse response = new JwtResponse("accesToken", "email", 1L);
        Mockito.when(authenticationService.createAuthenticationToken(Mockito.any(JwtRequest.class))).thenReturn(null);
        // WHEN
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/paymybuddy/login").contentType(MediaType.APPLICATION_JSON).content("{\"email\": \"test@test.fr\", \"password\": \"pwd\"}")
                .with(csrf()).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();
    }

    @Test
    void registerAUserTest() throws Exception {
        // GIVEN
        Mockito.when(authenticationService.save(Mockito.any(JwtRequest.class))).thenAnswer(a -> {
            JwtRequest jwtRequest = a.getArgument(0);
            if (jwtRequest.getEmail().equals("test.com")) {
                throw new NoSuchUserException(jwtRequest.getEmail());
            }
            if (jwtRequest.getEmail().equals("exist@gmail.com")) {
                throw new UserAlreadyExistException(jwtRequest.getEmail());
            }
            return "Request register successful";
        });
        // WHEN
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/paymybuddy/register").contentType(MediaType.APPLICATION_JSON).content("{\"email\": \"test@test.fr\", \"password\": \"pwd\"}")
                .with(csrf()).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // THEN
        Assertions.assertThat(result.getResponse().getContentAsString())
                .isEqualTo("Request register successful");

        // Test errors
        mvc.perform(MockMvcRequestBuilders.post("/paymybuddy/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test.com\", \"password\": \"pwd\"}")
                .with(csrf()).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        mvc.perform(MockMvcRequestBuilders.post("/paymybuddy/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"exist@gmail.com\", \"password\": \"pwd\"}")
                .with(csrf()).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}

