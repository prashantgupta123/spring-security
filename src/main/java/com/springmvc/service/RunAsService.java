package com.springmvc.service;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
public class RunAsService {

    @Secured({"ROLE_RUN_AS_CUSTOM"})
    public String display() {
        return "Run as successful";
    }

}