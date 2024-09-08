package com.scm.scm_project.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.scm_project.entities.User;
import com.scm.scm_project.helper.AppConstants;
import com.scm.scm_project.helper.Helper;
import com.scm.scm_project.helper.ResourceNotFoundException;
import com.scm.scm_project.repositories.UserRepo;
import com.scm.scm_project.services.UserService;
import com.scm.scm_project.services.EmailService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User user) {
        // user id have to generate 
        String userId = UUID.randomUUID().toString(); 
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        logger.info(user.getProvider().toString());

        // set role 
        user.setRoleList(List.of(AppConstants.ROLE_USER)); 

        String emailToken = UUID.randomUUID().toString();
        String emailLink = Helper.
        getLinkForEmailVerification(emailToken);
        user.setEmailToken(emailToken);
        User savedUser = userRepo.save(user);

        emailService.sendEmail(savedUser.getEmail(), "Verify Account", emailLink);

        return savedUser;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User oldUser = userRepo.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        // update old user with new user
        BeanUtils.copyProperties(user, oldUser);
        return Optional.ofNullable(userRepo.save(oldUser));

    }

    @Override
    public void deleteUser(String id) {

        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        userRepo.delete(user);
    }

    @Override
    public boolean isUserExist(String id) {
        
        User user = userRepo.findById(id).orElse(null); 
        return user != null; 
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        
        User user = userRepo.findByEmail(email).orElse(null);
        return user != null; 

    }

    @Override
    public List<User> getAllUsers() {
        
        List<User> userList = userRepo.findAll(); 
        return userList;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

}
