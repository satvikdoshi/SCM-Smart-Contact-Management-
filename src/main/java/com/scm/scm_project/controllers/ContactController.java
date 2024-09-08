package com.scm.scm_project.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.scm_project.entities.Contact;
import com.scm.scm_project.entities.User;
import com.scm.scm_project.forms.ContactForm;
import com.scm.scm_project.forms.ContactSearchForm;
import com.scm.scm_project.helper.AppConstants;
import com.scm.scm_project.helper.Helper;
import com.scm.scm_project.helper.Message;
import com.scm.scm_project.helper.MessageType;
import com.scm.scm_project.services.ContactService;
import com.scm.scm_project.services.ImageService;
import com.scm.scm_project.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageSerivce;

    // add contact page : handler
    @RequestMapping("/add")
    public String addContactView(Model model) {
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        // contactForm.setFavorite(true);

        return "user/add_contact";
    }

    @PostMapping("/add")
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult rBindingResult,
            Authentication authentication, HttpSession httpSession) {

        // process the form data
        // extract the current user
        String username = Helper.getEmailOfLoggedInUser(authentication);

        // validate the form

        if (rBindingResult.hasErrors()) {
            // List<ObjectError> list = rBindingResult.getAllErrors();
            // System.out.println(list);
            // System.out.println("inside");
            httpSession.setAttribute("message", Message.builder()
                    .content("Invalid data filled! Try again...")
                    .messageType(MessageType.red)
                    .build());
            return "/user/add_contact";
        }

        // profile image processing
        System.out.println("fileName: " + contactForm.getProfile().getOriginalFilename());

        // upload the file


        // convert contactform to contact
        User user = userService.getUserByEmail(username);
        Contact contact = new Contact();

        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setFavorite(contactForm.isFavorite());
        contact.setLinkdenInLInk(contactForm.getLinkdenLink());
        contact.setGithubLink(contactForm.getGithubLink());
        contact.setUser(user);
        contact.setPhone(contactForm.getPhone());


        if(contactForm.getPicture() != null && !contactForm.getProfile().isEmpty()){
            String fileName = UUID.randomUUID().toString();
            String fileUrl = imageSerivce.uploadImage(contactForm.getProfile(), fileName);
            contact.setPicture(fileUrl);
            contact.setCloudinaryImagePublicId(fileName);

        }

        // System.out.println("Url: " + fileUrl);

        contactService.save(contact);
        System.out.println(contactForm);

        httpSession.setAttribute("message", Message.builder()
                .content("Contact added successfully!")
                .messageType(MessageType.green)
                .build());

        return "redirect:/user/contacts/add";

    }

    // view contacts
    @RequestMapping
    public String viewContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model, Authentication authentication) {
        // load all the user contacts
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        if(sortBy == null) sortBy = "name";
        Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contactSearchForm", new ContactSearchForm());

        return "user/contacts";
    }

    // search hanlder
    @RequestMapping("/search")
    public String searchHandler(
            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,
            Authentication authentication) {

        Page<Contact> pageContact = null;
        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        String field = contactSearchForm.getField();
        String keyword = contactSearchForm.getKeyword();

        if (field.equalsIgnoreCase("name")) {
            pageContact = contactService.searchByName(keyword, size, page, sortBy, direction, user);
        } else if (field.equalsIgnoreCase("email")) {
            pageContact = contactService.searchByEmail(keyword, size, page, sortBy, direction, user);
        } else if (field.equalsIgnoreCase("phone")) {
            pageContact = contactService.searchByPhone(keyword, size, page, sortBy, direction, user);
        }
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("contactSearchForm", contactSearchForm);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        System.out.println("data: " + pageContact);
        return "user/search";
    }

    // delete contact
    @RequestMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable("contactId") String contactId, HttpSession session) {

        contactService.delete(contactId);
        session.setAttribute("message", Message.builder()
                .content("Contact deleted successfully!")
                .messageType(MessageType.green)
                .build());

        return "redirect:/user/contacts";
    }

    // update contact form view
    @GetMapping("/view/{contactId}")
    public String updateContactFormView(@PathVariable("contactId") String contactId, Model model) {

        var contact = contactService.getById(contactId);
        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhone(contact.getPhone());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setLinkdenLink(contact.getLinkdenInLInk());
        contactForm.setGithubLink(contact.getGithubLink());
        contactForm.setFavorite(contactForm.isFavorite());
        contactForm.setPicture(contact.getPicture());

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);

        return "user/update_contact_view";
    }

    @PostMapping("/update/{contactId}")
    public String updateContact(@PathVariable("contactId") String contactId, @Valid @ModelAttribute ContactForm contactForm,
            BindingResult rBindingResult,
            Model model) {

                if(rBindingResult.hasErrors()){
                    return "user/update_contact_view";
                }

        // update the contact
        var contact = contactService.getById(contactId);
        contact.setId(contactId);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setAddress(contactForm.getAddress());
        contact.setPhone(contact.getPhone());
        contact.setDescription(contact.getDescription());
        contact.setFavorite(contactForm.isFavorite());
        contact.setLinkdenInLInk(contactForm.getLinkdenLink());
        contact.setGithubLink(contactForm.getGithubLink());

        // process image
        if (contactForm.getProfile() != null && !contactForm.getProfile().isEmpty()) {
            String fileName = UUID.randomUUID().toString();
            String imageUrl = imageSerivce.uploadImage(contactForm.getProfile(), fileName);

            contact.setCloudinaryImagePublicId(fileName);
            contact.setPicture(imageUrl);
            contact.setPicture(imageUrl);
        }

        contactService.update(contact);
        model.addAttribute("message", Message.builder()
                .content("Contact updated successfully!")
                .messageType(MessageType.green)
                .build());
        return "redirect:/user/contacts/view/" + contactId;
    }

}
