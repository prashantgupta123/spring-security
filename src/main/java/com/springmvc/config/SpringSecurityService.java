package com.springmvc.config;

import com.springmvc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SpringSecurityService {

    @Autowired
    private UserRepository userRepository;

    public UserDetails getCurrentUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String getUserAuthority() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
    }

}