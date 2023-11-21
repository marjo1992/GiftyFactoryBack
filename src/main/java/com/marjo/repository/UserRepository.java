package com.marjo.repository;

import com.marjo.entity.Person;
import com.marjo.entity.User;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

   public User findByMail(String mail){
       return find("mail", mail).firstResult();
   }

   public void deleteTestUser(){
       delete("mail", "test@mail.com");
  }
}