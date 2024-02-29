package com.marjo.giftyfactoryback.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marjo.giftyfactoryback.entity.User;
import com.marjo.giftyfactoryback.error.exception.NotAuthorizedActionException;
import com.marjo.giftyfactoryback.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        if (!user.isEmailConfirmed()) {
            throw new NotAuthorizedActionException("Email is not confirmed : " + username);
        }

        if (!user.isHimselfOwner()) {
            throw new NotAuthorizedActionException("Account locked until actual owner of person " + user.getPerson().getName() + " transfer ownership to user "  + username);
        }

        return UserDetailsImpl.build(user);
    }

}