package com.paymybuddy.project.controller;

import com.paymybuddy.project.dto.BankDTO;
import com.paymybuddy.project.model.Bank;
import com.paymybuddy.project.model.User;
import com.paymybuddy.project.security.service.UserDetailsImpl;
import com.paymybuddy.project.service.BankService;
import com.paymybuddy.project.service.UserService;
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

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class BankController {

    @Autowired
    private BankService bankTransac;

    @Autowired
    private UserService userService;

    Logger LOGGER = LoggerFactory.getLogger(BankController.class);

    /**
     * Process bank transaction
     * @param bankDTO transaction infos
     * @param authentication
     * @return Status 200 if transaction OK, status 400 if failed
     */
    @PostMapping(value = "api/processTransactionBank")
    public ResponseEntity<BankDTO> processTransaction (@RequestBody BankDTO bankDTO, Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.getUserById(userDetails.getId()).get();
        Bank bank = bankTransac.createBankTransaction(user, bankDTO.getIban(), bankDTO.getAmount());
        if(bankTransac.processTransaction(bank)){
            bankTransac.save(bank);
            LOGGER.info("Process Bank Transaction success");
            return ResponseEntity.ok(bankDTO);
        } else {
            LOGGER.info("Invalid ID, transaction failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * List of bank transactions by user
     * @param authentication the userID
     * @return list of bank transaction, 400 if the user doesn't exist
     */
    @GetMapping(value = "api/bankTransaction")
    public ResponseEntity<List<Bank>> getTransaction(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.getUserById(userDetails.getId()).get();
        List<Bank> listTransac = bankTransac.findByUser(user);
        return ResponseEntity.ok(listTransac);
    }
}
