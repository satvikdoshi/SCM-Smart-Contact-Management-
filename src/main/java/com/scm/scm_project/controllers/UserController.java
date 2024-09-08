package com.scm.scm_project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.scm_project.entities.User;
import com.scm.scm_project.helper.Helper;
import com.scm.scm_project.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // user dashboard page
    @RequestMapping(value = "/dashboard", method = { RequestMethod.GET, RequestMethod.POST })
    public String userDashboard() {
        return "user/dashboard";
    }

    // user profile page
    @RequestMapping(value = "/profile", method = { RequestMethod.GET, RequestMethod.POST })
    public String userProfile(Model model, Authentication authenticate) {

        String username = Helper.getEmailOfLoggedInUser(authenticate);
        // System.out.println(username);

        // fetch the data
        User user = userService.getUserByEmail(username);
        // System.out.println(user);
        model.addAttribute("loggedUser", user);

        return "user/profile";
    }

    // user add contact page

    // user view contact

    // user edit contact
}
