package com.scm.scm_project.forms;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

public class UserForm {
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Min 3 char required")
    private String name;
    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank
    @Size(min = 6, message = "password should be more than 5 digit ")
    private String password;
    @NotBlank(message = "about is required")
    private String about; 
    @Size(min = 8, max = 12, message = "Invalid phone number")
    private String phone; 

}
