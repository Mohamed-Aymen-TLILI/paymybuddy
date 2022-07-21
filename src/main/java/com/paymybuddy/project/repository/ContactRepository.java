package com.paymybuddy.project.repository;

import com.paymybuddy.project.model.Contact;
import com.paymybuddy.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Contact repository.
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact,Integer> {

    Contact findContactByContactUserIdAndContactUserId(User user, User ami);
}
