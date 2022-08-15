package com.paymybuddy.project.data;

import com.paymybuddy.project.model.User;
import com.paymybuddy.project.security.service.UserDetailsImpl;

public class TestData {

    public static User getTrueUserData() {
        User user = new User();
        user.setId(1L);
        user.setNickname("Test");
        user.setEmail("test@test.com");
        user.setPassword("abcd");
        user.setAmount(99.00);
        return user;
    }

    public static UserDetailsImpl getPrincipal() {
        return new UserDetailsImpl(1L, "test@test.com", "abcd");
    }
}
