package com.springmvc.controller;

import com.springmvc.service.RunAsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/runAs")
public class RunAsController {

    @Autowired
    private RunAsService runAsService;

    @RequestMapping("/one")
    @Secured({"ROLE_ADMIN", "RUN_AS_CUSTOM"})
    public String runAs() {
        return runAsService.display();
    }

    @RequestMapping("/two")
    @Secured({"ROLE_ADMIN"})
    public String runAs2() {
        return runAsService.display();
    }

}