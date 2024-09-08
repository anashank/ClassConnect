package com.example.application.views.list;

import com.example.application.repositories.UserRepository;
import com.example.application.services.UserDetailsServiceImpl;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration

public class Security extends VaadinWebSecurity {

    private final UserRepository userRepository;

    @Autowired
    public Security(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        super.configure(http);
        http.formLogin(formLogin ->
                formLogin
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            // Redirect to /user-form after successful login
                            response.sendRedirect("/home");
                        })
                        .failureHandler((request, response, authentication) -> {
                                    // Redirect to /user-form after successful login
                            request.getSession().setAttribute("loginError", "Invalid username or password.");
                            response.sendRedirect("/login?error");
                        })
                        .permitAll()
        );
        //setLoginView(http, LoginView.class);
    }

    @Bean
    public UserDetailsServiceImpl userDetailsServiceImpl() {
        return new UserDetailsServiceImpl(userRepository, passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

//    @Bean
//    UserDetailsManager userDetailsManager(){
//        return new InMemoryUserDetailsManager(
//                User.withUsername("marcus")
//                        .password("{noop}test1234")
//                        .roles("USER").build()
//        );
//        //return new InMemoryUserDetailsManager();
//    }

