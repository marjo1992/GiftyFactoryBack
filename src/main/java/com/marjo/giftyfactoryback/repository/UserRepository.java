package com.marjo.giftyfactoryback.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.marjo.giftyfactoryback.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("""
            from User u 
            where :personId = :personId
            """)
    Optional<User> findByPersonId(@Param("personId") long personId);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}