package com.paymybuddy.project.service;


import com.paymybuddy.project.data.TestData;
import com.paymybuddy.project.dto.UserDTO;
import com.paymybuddy.project.model.User;
import com.paymybuddy.project.repository.UserRepository;

import org.junit.jupiter.api.Test;


import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@SpringBootTest()
public class UserServiceTest {

    @Configuration
    static class ContextConfiguration {

        // this bean will be injected into the OrderServiceTest class
        @Bean
        public UserService userService() {
            UserService userService = new UserService(userRepositoryMock);
            // set properties, etc.
            return userService;
        }

        private UserRepository userRepositoryMock = mock(UserRepository.class);


        private UserService userService = mock(UserService.class);

        private User createUser(String email) {
            User user = new User();
            user.setNickname("Test");
            user.setEmail(email);
            user.setPassword("password");
            return user;
        }


        @Test
        void findByIdEmailTest() {
            String email = "test@test.com";
            User person = new User();
            person.setEmail("test@test.com");
            person.setNickname("Test");
            List<User> personList = new ArrayList<>();
            personList.add(person);
            when(userService.getByEmail(email)).thenReturn(person);
            assertThat(person.getNickname()).isEqualTo(TestData.getTrueUserData().getNickname());
        }

        @Test
        void findAllTest() {
            String email = "test@test.com";
            User person = new User();
            person.setEmail(email);
            person.setNickname("Test");
            List<User> personList = new ArrayList<>();
            personList.add(person);
            when(userService.getAll()).thenReturn(personList);
            assertEquals(1, userService.getAll().size());
        }

        @Test
        void updateUserTest() {
            String email = "test@test.com";
            UserDTO userDTO = new UserDTO("test@test.com", "Hello", 20.00);
            User test = createUser(email);
            List<User> personList = new ArrayList<>();
            personList.add(test);
            when(userService.getAll()).thenReturn(personList);
            assertEquals(1, userService.getAll().size());
            assertThat(test.getNickname()).isEqualTo(TestData.getTrueUserData().getNickname());
            Mockito.doNothing().when(userService).updateNickname(any());
            assertThat(test.getNickname()).isEqualTo(TestData.getTrueUserData().getNickname());
        }

        @Test
        void addUserTest() {
            String email = "test@test.com";
            String email2 = "te@test.com";
            UserDTO userDTO = new UserDTO("test@test.com", "Hello", 20.00);
            User test = createUser(email);
            User test2 = createUser(email2);
            ArgumentCaptor<User> idCapture = ArgumentCaptor.forClass(User.class);
            ArgumentCaptor<User> nameCapture = ArgumentCaptor.forClass(User.class);
            doNothing().when(userService).addFriend(idCapture.capture(),nameCapture.capture());
            userService.addFriend(test, test2);
            assertEquals(test, idCapture.getValue());
            assertEquals(test2, nameCapture.getValue());
        }

        @Test
        void removeUserTest() {
            String email = "test@test.com";
            String email2 = "te@test.com";
            UserDTO userDTO = new UserDTO("test@test.com", "Hello", 20.00);
            User test = createUser(email);
            User test2 = createUser(email2);
            ArgumentCaptor<User> idCapture = ArgumentCaptor.forClass(User.class);
            ArgumentCaptor<User> nameCapture = ArgumentCaptor.forClass(User.class);
            doNothing().when(userService).removeFriend(idCapture.capture(),nameCapture.capture());
            userService.removeFriend(test, test2);
            assertEquals(test, idCapture.getValue());
            assertEquals(test2, nameCapture.getValue());
        }

    }

}
