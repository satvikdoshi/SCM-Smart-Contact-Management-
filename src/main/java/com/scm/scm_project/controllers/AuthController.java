package com.scm.scm_project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.scm_project.entities.User;
import com.scm.scm_project.helper.Message;
import com.scm.scm_project.helper.MessageType;
import com.scm.scm_project.repositories.UserRepo;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepo;
    
    // verify email 
    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token")String token, HttpSession session){

        User user = userRepo.findByEmailToken(token).orElse(null);
        if(user != null){

            if(user.getEmailToken().equals(token)){
                user.setEmailVerified(true);
                user.setEnabled(true);
                userRepo.save(user);

                session.setAttribute("message", 
                Message.builder()
                .content("Email has been Verified!")
                .messageType(MessageType.green)
                .build());

                return "success_page";
            }
        }
        session.setAttribute("message", 
        Message.builder()
        .content("Something Went Wrong!")
        .messageType(MessageType.red)
        .build());

        return "error_page";
    }

}
