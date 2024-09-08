package com.scm.scm_project.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authenticate) {

        if (authenticate instanceof OAuth2AuthenticationToken) {

            var aAuth2AuthenticationToken = (OAuth2AuthenticationToken) authenticate;
            var clientId = aAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauthUser = (OAuth2User) authenticate.getPrincipal();
            String username = "";

            if (clientId.equalsIgnoreCase("google")) {
                username = oauthUser.getAttribute("email").toString();
                System.out.println("google login");
            } else if (clientId.equalsIgnoreCase("github")) {
                username = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email")
                        : oauthUser.getAttribute("login").toString() + "@gmail.com";
                        System.out.println("github login");
            }

            return username;

        } else {
            System.out.println("self login");
            return authenticate.getName();
        }

    }

    public static String getLinkForEmailVerification(String emailToken){

        String link = "http://localhost:8080/auth/verify-email?token=" + emailToken;

        return link;
    }
}
