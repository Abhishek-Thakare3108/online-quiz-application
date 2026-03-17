package com.quiz.quizapp.controller;

import com.quiz.quizapp.model.User;
import com.quiz.quizapp.repository.ResultRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LeaderboardController {

    @Autowired
    private ResultRepository resultRepository;

    @GetMapping("/leaderboard")
    public String leaderboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("results", resultRepository.findTop10ByOrderByScoreDesc());
        return "leaderboard";
    }
}
