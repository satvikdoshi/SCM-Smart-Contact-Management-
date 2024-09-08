package com.scm.scm_project.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.scm_project.entities.User;
import com.scm.scm_project.forms.UserForm;
import com.scm.scm_project.helper.Message;
import com.scm.scm_project.helper.MessageType;
import com.scm.scm_project.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(){
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(){
        return "home";
    }

    @RequestMapping("/about")
    public String about(){
        return "about";
    }

    
    @RequestMapping("/service")
    public String service(){
        return "service";
    }

    @RequestMapping("/contact")
    public String contact(){
        return "contact";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        // sending blank object to register form 
        model.addAttribute("userForm",new UserForm());
        return "register";
    }

    // processing register form
    @PostMapping("/do-register")
    public String processRegisteredForm(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult ,HttpSession session){
        System.out.println(userForm);

        // validate the data
        if(rBindingResult.hasErrors()){
            // System.out.println("errors");
            return "register";
        }

        User user = new User();
        BeanUtils.copyProperties(userForm, user);
        user.setEnabled(false);
        // System.out.println(user);
        // System.out.println("saved");
        userService.saveUser(user);

        Message msg = Message.builder().content("Registration Successful").messageType(MessageType.green).build();
        // add message to register page 
        session.setAttribute("message",msg);
        
        // redirecting to the /register route
        return "redirect:/register";
    }



}
