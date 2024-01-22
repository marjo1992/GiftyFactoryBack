package com.marjo.giftyfactoryback.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.marjo.giftyfactoryback.entity.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

        @Query("""
                from Person p 
                where (:name is null or p.name = :name) 
                and (:firstname is null or p.firstname = :firstname) 
                and (:birthdate is null or p.birthdate = :birthdate)
                """)
        List<Person> findBy(
                        @Param("name") String name,
                        @Param("firstname") String firstname,
                        @Param("birthdate") LocalDate birthdate);

        Optional<Person> findById(@Param("id") long id);

}
