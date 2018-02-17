package com.springmvc.config;

import com.springmvc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class SpringSecurityService {

    @Autowired
    private UserRepository userRepository;

    public com.springmvc.entity.User getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        com.springmvc.entity.User systemUser = userRepository.findByUsername(user.getUsername());
        return systemUser;
    }
}