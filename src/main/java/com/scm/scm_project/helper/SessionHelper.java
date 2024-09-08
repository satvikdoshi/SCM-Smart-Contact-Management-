package com.scm.scm_project.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {

    public static void removeMessage() {
        @SuppressWarnings("null")
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                .getSession();

        try {
            System.out.println("remove messasge from session");
            session.removeAttribute("message");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
