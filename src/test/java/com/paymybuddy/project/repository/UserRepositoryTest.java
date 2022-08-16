package com.paymybuddy.project.repository;

import com.paymybuddy.project.data.TestData;
import com.paymybuddy.project.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void findByIdEmailTest() {
        testEntityManager.persist(new User( "test@test.com", "abcd", "Test"));
        testEntityManager.flush();
        assertThat(userRepository.findByEmail("test@test.com").getNickname()).isEqualTo(TestData.getTrueUserData().getNickname());
    }

    @Test
    void existsByEmailTest() {
        testEntityManager.persist(new User( "test@test.com", "abcd", "Test"));
        testEntityManager.flush();
        assertThat(userRepository.existsByEmail("test@test.com")).isTrue();
    }

    @Test
    void findByIdTest() {
        testEntityManager.persist(new User( "test@test.com", "abcd", "Test"));
        testEntityManager.flush();
        assertThat(userRepository.findById(1L)).isEmpty();
    }
}
