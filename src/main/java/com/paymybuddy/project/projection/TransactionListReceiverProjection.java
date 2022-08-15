package com.paymybuddy.project.projection;


import com.paymybuddy.project.model.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(
        name = "TransactionListReceiverProjection ",
        types = { Transaction.class })
public interface TransactionListReceiverProjection {

    Long getId();

    @Value("#{target.getSender().getNickname()}")
    String getNickNameContact();

    Double getAmount();

    String getDescription();
}
