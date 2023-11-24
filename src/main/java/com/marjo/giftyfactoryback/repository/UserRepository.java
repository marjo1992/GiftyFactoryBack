package com.marjo.giftyfactoryback.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.marjo.giftyfactoryback.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByMail(String mail);

    public void deleteByMail(String mail);
}