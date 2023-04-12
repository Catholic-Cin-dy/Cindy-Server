package com.app.cindy.service;

import com.app.cindy.domain.user.User;
import org.springframework.security.core.userdetails.UserDetails;


public interface CustomUserDetailsService {


    UserDetails loadUserByUsername(final String username);


    org.springframework.security.core.userdetails.User createUser(String username, User user);

}

