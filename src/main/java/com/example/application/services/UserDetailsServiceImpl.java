package com.example.application.services;
import com.example.application.views.list.UserForm;
import com.example.application.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String DB_URL = "jdbc:sqlite:db/vaadin_app.db";

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username FROM users")) {

            while (rs.next()) {
                names.add(rs.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }

    public List<UserForm> findAllUsers(){
        return userRepository.findAll();
    }

    // Define function that creates test users to add to the users table
    public void createTestUsers() {
        try (Connection conn = DriverManager.getConnection(DB_URL);

            Statement stmt = conn.createStatement()) {
            //add variable to stmt execute function to add an encoded password
            stmt.execute("INSERT INTO users (username, password, email) VALUES ('user1', '"+passwordEncoder.encode("password1")+"','email1')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveUserDetails(UserForm user){
        if(user == null){
            System.err.println("User is null");
            return;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserForm user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .disabled(!user.isEnabled())
                .roles("USER") // You can set roles here if you have a role management system
                .build();
    }



}


