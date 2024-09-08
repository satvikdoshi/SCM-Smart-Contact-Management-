package com.scm.scm_project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SocialLink {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id; 
    private String link; 
    private String title;

    @ManyToOne
    private Contact contact;
}
