package com.paymybuddy.project.projection;


import com.paymybuddy.project.model.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(
        name = "TransactionListSenderProjection ",
        types = { Transaction.class })
public interface TransactionListSenderProjection {

    Long getId();

    @Value("#{target.getReceiver().getNickname()}")
    String getNickNameContact();

    Double getAmount();

    String getDescription();


}


