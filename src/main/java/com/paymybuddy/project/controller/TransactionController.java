package com.paymybuddy.project.controller;

import com.paymybuddy.project.dto.TransactionDTO;
import com.paymybuddy.project.model.Transaction;
import com.paymybuddy.project.model.User;
import com.paymybuddy.project.projection.TransactionListReceiverProjection;
import com.paymybuddy.project.projection.TransactionListSenderProjection;
import com.paymybuddy.project.security.service.UserDetailsImpl;
import com.paymybuddy.project.service.TransactionService;
import com.paymybuddy.project.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TransactionController {

    @Autowired
    private TransactionService transacService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectionFactory projectionFactory;

    Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    /**
     * Payment between users
     * @param transac the transaction infos
     * @return 200 if successful | 400 if failed
     */
    @PostMapping(value = "/transaction")
    public ResponseEntity makeTransaction(@RequestBody TransactionDTO transac){
        if(transacService.makePayment(transac)){
            LOGGER.info("Transaction success");
            return ResponseEntity.ok(transac);
        } else {
            return ResponseEntity.badRequest().body(transac);
        }
    }

    @GetMapping(value = "api/transactionlistbyusersender")
    public List<TransactionListSenderProjection> listTransactionByUserSender(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.getUserById(userDetails.getId()).get();
        List<Transaction> listByUser = transacService.findAllByUserSender(user);
        return listByUser != null ? listByUser.stream().map(list -> projectionFactory.createProjection( TransactionListSenderProjection.class, list)).collect(Collectors.toList()) : null;
    }

    @GetMapping(value = "api/transactionlistbyuserreceiver")
    public List<TransactionListReceiverProjection> listTransactionByUserReceiver(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.getUserById(userDetails.getId()).get();
        List<Transaction> listByUser = transacService.findAllByUserReceiver(user);
        return listByUser != null ? listByUser.stream().map(list -> projectionFactory.createProjection( TransactionListReceiverProjection.class, list)).collect(Collectors.toList()) : null;
    }
}
