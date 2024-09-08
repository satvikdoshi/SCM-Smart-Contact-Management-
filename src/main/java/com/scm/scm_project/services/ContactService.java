package com.scm.scm_project.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.scm.scm_project.entities.Contact;
import com.scm.scm_project.entities.User;

public interface ContactService {
    
    // save contact
    Contact save(Contact contact);
    // update contact
    Contact update(Contact contact);
    // get contacts 
    List<Contact> getAll();
    // get contact by id
    Contact getById(String id);
    // delete Contact 
    void delete(String id);
    // search contact 
    Page<Contact> searchByName(String name, int size, int page, String sortBy, String direction, User user);
    Page<Contact> searchByEmail(String email, int size, int page, String sortBy, String direction, User user);
    Page<Contact> searchByPhone(String phone, int size, int page, String sortBy, String direction, User user);

    // get contacts by user id 
    List<Contact> getByUserId(String uid);

    Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction);


}
