package com.scm.scm_project.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.scm_project.entities.Contact;
import com.scm.scm_project.entities.User;
import com.scm.scm_project.helper.ResourceNotFoundException;
import com.scm.scm_project.repositories.ContactRepo;
import com.scm.scm_project.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public Contact save(Contact contact) {

        String contactId = UUID.randomUUID().toString(); 
        contact.setId(contactId);
        return contactRepo.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
        var oldContact = contactRepo.findById(contact.getId()).orElseThrow(()-> new ResourceNotFoundException("Contact not found!"));

        oldContact.setName(contact.getName());
        oldContact.setEmail(contact.getEmail());
        oldContact.setPhone(contact.getPhone());
        oldContact.setAddress(contact.getAddress());
        oldContact.setDescription(contact.getDescription());
        oldContact.setPicture(contact.getPicture());
        oldContact.setFavorite(contact.isFavorite());
        oldContact.setLinkdenInLInk(contact.getLinkdenInLInk());
        oldContact.setGithubLink(contact.getGithubLink());
        oldContact.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());
        
        return contactRepo.save(oldContact);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();

    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Contact not found!"));
    }

    @Override
    public void delete(String id) {
        contactRepo.deleteById(id);
    }


    @Override
    public List<Contact> getByUserId(String uid) {
 
        return contactRepo.findByUserId(uid);
    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUser(user,pageable);
    }

    @Override
    public Page<Contact> searchByName(String name, int size, int page, String sortBy, String direction, User user) {
        
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending(); 
        var pageable = PageRequest.of(page,size,sort);
        return contactRepo.findByUserAndNameContaining(user,name, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String email, int size, int page, String sortBy, String direction, User user) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending(); 
        var pageable = PageRequest.of(page,size,sort);
        return contactRepo.findByUserAndEmailContaining(user,email, pageable);
    }

    @Override
    public Page<Contact> searchByPhone(String phone, int size, int page, String sortBy, String direction, User user) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending(); 
        var pageable = PageRequest.of(page,size,sort);
        return contactRepo.findByUserAndPhoneContaining(user, phone, pageable);
    }


    
}
