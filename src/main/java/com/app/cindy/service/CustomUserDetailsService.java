package com.app.cindy.service;

import com.app.cindy.domain.user.User;
import org.springframework.security.core.userdetails.UserDetails;


public interface CustomUserDetailsService {


    public UserDetails loadUserByUsername(final String username);


    public org.springframework.security.core.userdetails.User createUser(String username, User user);

}

