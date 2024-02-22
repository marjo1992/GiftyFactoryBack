package com.marjo.giftyfactoryback.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.marjo.giftyfactoryback.entity.Idea;

@Repository
public interface IdeaRepository extends CrudRepository<Idea, Long> {

        Optional<Idea> findById(@Param("id") long id);

}
