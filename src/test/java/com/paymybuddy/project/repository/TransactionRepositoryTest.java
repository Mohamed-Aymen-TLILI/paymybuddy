package com.paymybuddy.project.repository;



import com.paymybuddy.project.model.Transaction;
import com.paymybuddy.project.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void findByIdEmailSenderTest() {
        User user = new User( "test@test.com", "abcd", "Test");
        User user1 = new User( "te@test.com", "abcd", "Test");
        testEntityManager.persist(user);
        testEntityManager.persist(user1);
        testEntityManager.persist(new Transaction( user, user1, "Test", 200.00, new Date()));
        testEntityManager.flush();
        assertThat(transactionRepository.findAllBySender(user).size()).isEqualTo(1);
    }

    @Test
    void findByIdEmailSenderTestError() {
        User user = new User( "test@test.com", "abcd", "Test");
        User user1 = new User( "te@test.com", "abcd", "Test");
        testEntityManager.persist(user);
        testEntityManager.persist(user1);
        testEntityManager.persist(new Transaction( user, user1, "Test", 200.00, new Date()));
        testEntityManager.flush();
        assertThat(transactionRepository.findAllBySender(user1).size()).isEqualTo(0);
    }

    @Test
    void findByIdEmailReceiverTest() {
        User user = new User( "test@test.com", "abcd", "Test");
        User user1 = new User( "te@test.com", "abcd", "Test");
        testEntityManager.persist(user);
        testEntityManager.persist(user1);
        testEntityManager.persist(new Transaction( user, user1, "Test", 200.00, new Date()));
        testEntityManager.flush();
        assertThat(transactionRepository.findAllByReceiver(user1).size()).isEqualTo(1);
    }

    @Test
    void findByIdEmailReceiverTestError() {
        User user = new User( "test@test.com", "abcd", "Test");
        User user1 = new User( "te@test.com", "abcd", "Test");
        testEntityManager.persist(user);
        testEntityManager.persist(user1);
        testEntityManager.persist(new Transaction( user, user1, "Test", 200.00, new Date()));
        testEntityManager.flush();
        assertThat(transactionRepository.findAllByReceiver(user).size()).isEqualTo(0);
    }
}
