package com.paymybuddy.project.service;


import com.paymybuddy.project.data.TestData;
import com.paymybuddy.project.dto.TransactionDTO;
import com.paymybuddy.project.model.Transaction;
import com.paymybuddy.project.model.User;
import com.paymybuddy.project.repository.TransactionRepository;
import com.paymybuddy.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest()
public class TransactionServiceTest {


    @Configuration
    static class ContextConfiguration {

        // this bean will be injected into the OrderServiceTest class
        @Bean
        public TransactionService userService() {
            TransactionService transactionService = new TransactionService(userRepository, transactionRepository);
            // set properties, etc.
            return transactionService;
        }

        private UserRepository userRepository = mock(UserRepository.class);

        private TransactionRepository transactionRepository = mock(TransactionRepository.class);

        private TransactionService transactionService = mock(TransactionService.class);

        private User createUser(String email) {
            User user = new User();
            user.setNickname("Test");
            user.setEmail(email);
            user.setPassword("password");
            user.setAmount(200.00);
            return user;
        }

        private Transaction createTransaction(User sender, User receiver, String description, double amount){
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setDate(new Date());
            transaction.setSender(sender);
            transaction.setReceiver(receiver);
            return transaction;
        }

        @Test
        void findBySenderTest() {
            String email = "test@test.com";
            String email2 = "te@test.com";
            User test = createUser(email);
            User test2 = createUser(email2);
            Transaction transaction = createTransaction(test, test2, "ceci est test", 200.00 );
            List<Transaction> listTransaction = new ArrayList<>();
            listTransaction.add(transaction);
            when(transactionService.findAllByUserSender(test)).thenReturn(listTransaction);
            assertEquals(1, listTransaction.size());
        }

        @Test
        void findByReceveirTest() {
            String email = "test@test.com";
            String email2 = "te@test.com";
            User test = createUser(email);
            User test2 = createUser(email2);
            Transaction transaction = createTransaction(test, test2, "ceci est test", 200.00 );
            List<Transaction> listTransaction = new ArrayList<>();
            listTransaction.add(transaction);
            when(transactionService.findAllByUserReceiver(test2)).thenReturn(listTransaction);
            assertEquals(1, listTransaction.size());
        }

        @Test
        void authorizedPayment(){
            String email = "test@test.com";
            String email2 = "te@test.com";
            User test = createUser(email);
            User test2 = createUser(email2);
            TransactionDTO transactionDTO = new TransactionDTO(test.getId(), test2.getId(), 100.00, "ceci est test");
            when(transactionService.makePayment(transactionDTO)).thenReturn(true);
            assertEquals(200, test.getAmount());
        }
    }
}
