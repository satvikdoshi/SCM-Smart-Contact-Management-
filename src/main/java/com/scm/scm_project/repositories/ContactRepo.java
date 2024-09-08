package com.scm.scm_project.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.scm_project.entities.Contact;
import com.scm.scm_project.entities.User;

@Repository
public interface ContactRepo extends JpaRepository<Contact,String> {
    
    // find the contact by user - custom finder method
    Page<Contact> findByUser(User user, Pageable pageable);

    // custom query method 
    @Query("select c from Contact c where c.user.id =: userId")
    List<Contact> findByUserId(@Param("userId")String userId);

    Page<Contact> findByUserAndNameContaining(User user, String name, Pageable pageable);
    Page<Contact> findByUserAndEmailContaining(User user, String email, Pageable pageable);
    Page<Contact> findByUserAndPhoneContaining(User user, String phone, Pageable pageable);

}
