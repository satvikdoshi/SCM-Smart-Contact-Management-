package com.scm.scm_project.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.scm_project.validators.ValidFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ContactForm {

    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid Phone Number")
    @NotBlank(message = "Phone number is required")
    private String phone;
    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "Description is required")
    private String description;
    private boolean favorite;
    private String linkdenLink;
    private String githubLink;
    @ValidFile(message = "Invalid file")
    private MultipartFile profile;

    private String picture;

}
