package com.quiz.quizapp.controller;

import com.quiz.quizapp.model.User;
import com.quiz.quizapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Show login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Handle login form submission
    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username,
                              @RequestParam String password,
                              HttpSession session, Model model) {
        User user = userService.loginUser(username, password);
        if (user == null) {
            model.addAttribute("error", "Invalid username or password!");
            return "login";
        }
        // Store logged-in user in session
        session.setAttribute("loggedUser", user);
        if ("ADMIN".equals(user.getRole())) {
            return "redirect:/admin";
        }
        return "redirect:/dashboard";
    }

    // Show registration page
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Handle registration form submission
    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute User user, Model model) {
        String result = userService.registerUser(user);
        if (!"success".equals(result)) {
            model.addAttribute("error", result);
            return "register";
        }
        return "redirect:/login?registered";
    }

    // Logout: clear session
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
