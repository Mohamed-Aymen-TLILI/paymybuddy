package com.paymybuddy.project.service;

import com.paymybuddy.project.exception.ContactException;
import com.paymybuddy.project.exception.NoSuchUserException;
import com.paymybuddy.project.model.Contact;
import com.paymybuddy.project.model.MyUserDetails;
import com.paymybuddy.project.model.User;
import com.paymybuddy.project.repository.ContactRepository;
import com.paymybuddy.project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ContactService {

    private final Logger LOGGER = LoggerFactory.getLogger(ContactService.class);

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Gets all my contact by id.
     *
     * @param id the id
     * @return the all my contact by id
     */
    public List<Contact> getAllMyContactById(int id) throws NoSuchUserException {
        LOGGER.info("Processing to get all my contact");
        List<Contact> result = new ArrayList<>();
        result.addAll(userService.getUserById(id).getContactList());
        return result;
    }

    /**
     * Save contact relationship.
     *
     * @param user        the user
     * @param userContact the user contact
     * @throws NoSuchUserException the user not found exception
     * @throws ContactException      the contact exception
     */
    public void saveContactRelationship(MyUserDetails user, User userContact) throws NoSuchUserException, ContactException {
        LOGGER.info("Processing to save a new relationship");


    }

    /**
     * find contact relationship.
     *
     * @param userContact the user contact
     * @throws NoSuchUserException the user not found exception
     * @throws ContactException      the contact exception
     */
    public boolean findContactIsInMyRelationship(int userContact) throws NoSuchUserException, ContactException {
        LOGGER.info("Processing to find if user exist in my list relationship");
        User secondUser = userService.getUserById(userContact);
        return contactRepository.findContactByAmi_Email(secondUser) == null ? false : true;
    }
}
