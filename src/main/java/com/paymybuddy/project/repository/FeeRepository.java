package com.paymybuddy.project.repository;

import com.paymybuddy.project.model.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Fee repository.
 */
@Repository
public interface FeeRepository extends JpaRepository<Fee,Integer> {
}
