package com.scm.scm_project.config;

import java.io.IOException;
import java.util.UUID;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.scm_project.entities.Providers;
import com.scm.scm_project.entities.User;
import com.scm.scm_project.helper.AppConstants;
import com.scm.scm_project.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        // save data in database after oauth

        // first check from which provider user is authorized, so we can use that
        // provider properties, there might be different properties for each provider

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

        User u = new User();
        u.setUserId(UUID.randomUUID().toString());
        u.setEnabled(true);
        u.setEmailVerified(true);
        u.setRoleList(List.of(AppConstants.ROLE_USER));

        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {

            // fetch details of user
            String email = user.getAttribute("email").toString();
            String name = user.getAttribute("name").toString();
            String profile = user.getAttribute("picture").toString();

            u.setEmail(email);
            u.setName(name);
            u.setProfileLink(profile);
            u.setPassword("password");
            u.setProvider(Providers.GOOGLE);

            u.setProviderUserId(user.getName());
            u.setAbout("This account created using google");

        } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

            String email = user.getAttribute("email") != null? user.getAttribute("email")  : user.getAttribute("login").toString() + "@gmail.com";
            String name = user.getAttribute("login").toString();
            String profile = user.getAttribute("avatar_url").toString();

            u.setEmail(email);
            u.setName(name);
            u.setProfileLink(profile);
            u.setPassword("password");
            u.setProvider(Providers.GITHUB);

            u.setProviderUserId(user.getName());
            u.setAbout("This account created using github");

        }

        // save the user 
        User result = userRepo.findByEmail(u.getEmail()).orElse(null); 
        if(result == null){
            userRepo.save(u);
            System.out.println("user saved!");
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }

}
