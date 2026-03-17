package com.quiz.quizapp.service;

import com.quiz.quizapp.model.User;
import com.quiz.quizapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Register: hash password before saving
    public String registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Username already taken!";
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already registered!";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "success";
    }

    // Login: support both BCrypt hashed and legacy plain-text passwords
    public User loginUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) return null;

        User user = userOpt.get();
        String stored = user.getPassword();

        // BCrypt hash starts with $2a$ or $2b$
        boolean matches = stored.startsWith("$2")
                ? passwordEncoder.matches(password, stored)   // hashed
                : stored.equals(password);                    // legacy plain-text

        return matches ? user : null;
    }
}
