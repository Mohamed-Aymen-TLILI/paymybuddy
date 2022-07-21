package com.paymybuddy.project.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ContactId implements Serializable {
    private Integer user;
    private Integer ami;

}


