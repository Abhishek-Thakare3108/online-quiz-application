package com.quiz.quizapp.controller;

import com.quiz.quizapp.model.Question;
import com.quiz.quizapp.model.Quiz;
import com.quiz.quizapp.model.Result;
import com.quiz.quizapp.model.User;
import com.quiz.quizapp.service.QuizService;
import com.quiz.quizapp.service.ResultService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private ResultService resultService;

    // Show quiz page with all questions
    @GetMapping("/{id}")
    public String startQuiz(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        Optional<Quiz> quizOpt = quizService.getQuizById(id);
        if (quizOpt.isEmpty()) return "redirect:/dashboard";

        // Block if already attempted
        if (resultService.hasAttempted(user.getId(), id)) {
            model.addAttribute("error", "You have already attempted this quiz.");
            model.addAttribute("user", user);
            model.addAttribute("quizzes", quizService.getAllQuizzes());
            model.addAttribute("attemptedQuizIds", resultService.getAttemptedQuizIds(user));
            return "dashboard";
        }

        Quiz quiz = quizOpt.get();
        List<Question> questions = quizService.getQuestionsByQuiz(quiz);

        if (questions.isEmpty()) {
            model.addAttribute("error", "This quiz has no questions yet!");
            model.addAttribute("user", user);
            model.addAttribute("quizzes", quizService.getAllQuizzes());
            model.addAttribute("attemptedQuizIds", resultService.getAttemptedQuizIds(user));
            return "dashboard";
        }

        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questions);
        return "quiz-page";
    }

    // Handle quiz submission: calculate score, save result, redirect to result page
    @PostMapping("/{id}/submit")
    public String submitQuiz(@PathVariable Long id,
                             @RequestParam Map<String, String> answers,
                             HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        Optional<Quiz> quizOpt = quizService.getQuizById(id);
        if (quizOpt.isEmpty()) return "redirect:/dashboard";

        // Guard against duplicate submission (e.g. back button + resubmit)
        if (resultService.hasAttempted(user.getId(), id)) {
            return "redirect:/dashboard";
        }

        Quiz quiz = quizOpt.get();
        List<Question> questions = quizService.getQuestionsByQuiz(quiz);

        int score = 0;
        for (Question q : questions) {
            String submitted = answers.get("answer_" + q.getId());
            if (q.getCorrectAnswer() != null && q.getCorrectAnswer().equals(submitted)) {
                score++;
            }
        }

        Result result = new Result();
        result.setUser(user);
        result.setQuiz(quiz);
        result.setScore(score);
        result.setTotalQuestions(questions.size());
        Result saved = resultService.saveResult(result);

        return "redirect:/quiz/result/" + saved.getId();
    }

    // Show result page
    @GetMapping("/result/{resultId}")
    public String showResult(@PathVariable Long resultId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        return resultService.getResultById(resultId).map(result -> {
            model.addAttribute("quiz", result.getQuiz());
            model.addAttribute("score", result.getScore());
            model.addAttribute("total", result.getTotalQuestions());
            return "result";
        }).orElse("redirect:/dashboard");
    }
}
