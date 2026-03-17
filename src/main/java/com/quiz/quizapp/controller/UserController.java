package com.quiz.quizapp.controller;

import com.quiz.quizapp.model.User;
import com.quiz.quizapp.service.QuizService;
import com.quiz.quizapp.service.ResultService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private ResultService resultService;

    // Home page redirect
    @GetMapping("/")
    public String home(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";
        if ("ADMIN".equals(user.getRole())) return "redirect:/admin";
        return "redirect:/dashboard";
    }

    // User dashboard: show all available quizzes
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        model.addAttribute("quizzes", quizService.getAllQuizzes());
        model.addAttribute("attemptedQuizIds", resultService.getAttemptedQuizIds(user));
        return "dashboard";
    }

    // Progress page: show all past quiz attempts
    @GetMapping("/progress")
    public String progress(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        model.addAttribute("results", resultService.getResultsByUser(user));
        return "progress";
    }
}
