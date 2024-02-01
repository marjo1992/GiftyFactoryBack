package com.marjo.giftyfactoryback.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.marjo.giftyfactoryback.entity.User;
import com.marjo.giftyfactoryback.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Optional<User> findById(final long id) {
        Optional<User> user = userRepository.findByPersonId(id);
        return user;
    }
}