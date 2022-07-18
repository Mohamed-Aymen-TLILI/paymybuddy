package com.paymybuddy.project.repository;

import com.paymybuddy.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface User repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    /**
     * Find by email optional.
     *
     * @param email the email
     * @return the optional
     */
    Optional<User> findByEmail(String email);

    /**
     * Exists by email boolean.
     *
     * @param email the email
     * @return the boolean
     */
    boolean existsByEmail(String email);


    /**
     * Update role.
     *
     * @param id   the id
     * @param role the role
     */
    @Modifying
    @Query("UPDATE User u SET u.userRole = :role WHERE u.id = :id")
    public void updateRole(int id, String role);
}
