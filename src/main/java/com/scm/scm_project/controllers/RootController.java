package com.scm.scm_project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.scm_project.entities.User;
import com.scm.scm_project.helper.Helper;
import com.scm.scm_project.services.UserService;

// this is run for each request 
@ControllerAdvice
public class RootController {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authenticate) {
        if(authenticate == null) return;
        String username = Helper.getEmailOfLoggedInUser(authenticate);
        // System.out.println(username);

        // fetch the data
        User user = userService.getUserByEmail(username);
        // System.out.println(user);
        model.addAttribute("loggedUser", user);
    }
}
