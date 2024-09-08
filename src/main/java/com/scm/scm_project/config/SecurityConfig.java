package com.scm.scm_project.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.scm.scm_project.helper.Message;
import com.scm.scm_project.helper.MessageType;
import com.scm.scm_project.services.impl.SecurityCustomUserDetailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Configuration
public class SecurityConfig {

    // user create and login using java with memoery service
    // @Bean
    // public UserDetailsService userDetailsService(){

    // // creating users

    // UserDetails user1 = User
    // .withDefaultPasswordEncoder()
    // .username("admin123")
    // .password("admin123")
    // .roles("ADMIN","USER")
    // .build();

    // UserDetails user2 = User
    // .withDefaultPasswordEncoder()
    // .username ("user2")
    // .password("user2")
    // .build();

    // return new InMemoryUserDetailsManager(user1,user2);
    // }

    @Autowired
    private SecurityCustomUserDetailService securityCustomUserDetailService;

    @Autowired
    private OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;

    // configuration of authentication provider 
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(securityCustomUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        // configuration 
        // urls configuration 
        httpSecurity.authorizeHttpRequests(authorize ->{
            // authorize.requestMatchers("/home","/register","/login").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });


        // form default login 
        httpSecurity.formLogin(formLogin->{
            // our login page 
            formLogin.loginPage("/login")
            .loginProcessingUrl("/authenticate")
            .successForwardUrl("/user/profile")
            // .failureForwardUrl("/login?error=true")
            .usernameParameter("email")
            .passwordParameter("password")
            .failureHandler(new AuthenticationFailureHandler() {

                @Override
                public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException exception) throws IOException, ServletException {
                    
                            if(exception instanceof DisabledException){
                                // user is disabled 
                                HttpSession session = request.getSession();
                                session.setAttribute("message", 
                                Message.builder()
                                .content("User is disabled!")
                                .messageType(MessageType.red)
                                .build());
                                response.sendRedirect("/login");

                            }else{
                                response.sendRedirect("/login?error=true");
                            }
                }
                
            });
        //     .successHandler(new AuthenticationSuccessHandler() {

        //         @Override
        //         public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        //                 Authentication authentication) throws IOException, ServletException {
        //             // TODO Auto-generated method stub
        //             throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'");
        //         }
                
        //     })
        //     .failureHandler(new AuthenticationFailureHandler() {

        //         @Override
        //         public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        //                 AuthenticationException exception) throws IOException, ServletException {
        //             // TODO Auto-generated method stub
        //             throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'");
        //         }
                
        //     });
            
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.logout(logoutForm->{
            logoutForm.logoutUrl("/logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        // oauth configuration
        httpSecurity.oauth2Login(oauth->{
            oauth.loginPage("/login");
            oauth.successHandler(oAuthAuthenticationSuccessHandler);
        });
        
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
