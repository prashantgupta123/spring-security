package com.springmvc.controller;

import com.springmvc.config.SpringSecurityService;
import com.springmvc.entity.Privilege;
import com.springmvc.entity.Role;
import com.springmvc.entity.User;
import com.springmvc.entity.VerificationToken;
import com.springmvc.enums.UserRoleEnum;
import com.springmvc.repositories.PrivilegeRepository;
import com.springmvc.repositories.RoleRepository;
import com.springmvc.repositories.UserRepository;
import com.springmvc.repositories.VerificationTokenRepository;
import com.springmvc.service.ActiveUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private SpringSecurityService springSecurityService;
    @Autowired
    private ActiveUserService activeUserService;

    @RequestMapping(value = "/")
    public String home() {
        return "home";
    }

    @RequestMapping(value = "/user")
    @ResponseBody
    public String userHome() {
        return "User Home";
    }

    @RequestMapping(value = "/admin")
    @ResponseBody
    public String adminHome() {
        return "Admin Home";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    @ResponseBody
    public String accessDenied() {
        return "Access Deny";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    @RequestMapping("/register/user")
    @ResponseBody
    public String registerUser(User user, HttpServletRequest httpServletRequest) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setEnabled(false);
        Role userRole = roleRepository.findByAuthority(UserRoleEnum.ROLE_USER.getValue());
        user.getRoles().add(userRole);
        userRepository.save(user);

        VerificationToken verificationToken = new VerificationToken();
        String token = UUID.randomUUID().toString();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);

        String authUrl = "http://"
                + httpServletRequest.getServerName()
                + ":"
                + httpServletRequest.getServerPort()
                + httpServletRequest.getContextPath()
                + "/register/verifyToken"
                + "?token=" + token;
        System.out.println(authUrl);

        return "User Registered Successfully";
    }

    @RequestMapping("/register/role")
    @ResponseBody
    public String registerRole() {
        if (roleRepository.count() == 0) {
            Role userRole = new Role();
            userRole.setAuthority(UserRoleEnum.ROLE_USER.getValue());
            roleRepository.save(userRole);
            Role adminRole = new Role();
            adminRole.setAuthority(UserRoleEnum.ROLE_ADMIN.getValue());
            roleRepository.save(adminRole);
            return "Roles Registered Successfully";
        } else {
            return "Roles has been Registered Already";
        }
    }

    @RequestMapping("/register/privilege")
    @ResponseBody
    public String registerPrivilege() {
        if (privilegeRepository.count() == 0) {

            Privilege readPrivilege = new Privilege();
            readPrivilege.setName("ROLE_READ");
            privilegeRepository.save(readPrivilege);

            Privilege writePrivilege = new Privilege();
            writePrivilege.setName("ROLE_WRITE");
            privilegeRepository.save(writePrivilege);

            Privilege updatePrivilege = new Privilege();
            updatePrivilege.setName("ROLE_UPDATE");
            privilegeRepository.save(updatePrivilege);

            Privilege deletePrivilege = new Privilege();
            deletePrivilege.setName("ROLE_UPDATE");
            privilegeRepository.save(deletePrivilege);

            return "Privileges Registered Successfully";
        } else {
            return "Privileges has been Registered Already";
        }
    }

    @RequestMapping("/register/verifyToken")
    @ResponseBody
    public String verifyUserToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return "User verify successfully";
    }

    @RequestMapping(value = "/user/current")
    @ResponseBody
    public String userName() {
        UserDetails userDetails = springSecurityService.getCurrentUser();
        return "Hello " + userDetails.getUsername();
    }

    @RequestMapping(value = "/user/authority")
    @ResponseBody
    public String userAuthority() {
        return springSecurityService.getUserAuthority();
    }

    @RequestMapping("/active/users")
    @ResponseBody
    public String activeUsers() {
        return activeUserService.getAllActiveUsers().toString();
    }
}
