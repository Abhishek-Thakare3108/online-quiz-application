package com.quiz.quizapp.controller;

import com.quiz.quizapp.model.Question;
import com.quiz.quizapp.model.Quiz;
import com.quiz.quizapp.model.User;
import com.quiz.quizapp.service.QuizService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private com.quiz.quizapp.repository.ResultRepository resultRepository;

    // Helper: check if logged-in user is ADMIN
    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        return user != null && "ADMIN".equals(user.getRole());
    }

    // Admin panel: list all quizzes
    @GetMapping
    public String adminPanel(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("quizzes", quizService.getAllQuizzes());
        model.addAttribute("newQuiz", new Quiz());
        return "admin-panel";
    }

    // Create new quiz
    @PostMapping("/quiz/create")
    public String createQuiz(@ModelAttribute Quiz quiz, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        quizService.saveQuiz(quiz);
        return "redirect:/admin";
    }

    // Show edit quiz page - dedicated template, no conditional rendering
    @GetMapping("/quiz/edit/{id}")
    public String editQuizForm(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        Optional<Quiz> quizOpt = quizService.getQuizById(id);
        if (quizOpt.isEmpty()) return "redirect:/admin";
        model.addAttribute("quiz", quizOpt.get());
        model.addAttribute("questions", quizService.getQuestionsByQuizId(id));
        return "admin-quiz-edit";
    }

    // Update quiz title/description
    @PostMapping("/quiz/update/{id}")
    public String updateQuiz(@PathVariable Long id, @ModelAttribute Quiz updatedQuiz, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        quizService.getQuizById(id).ifPresent(quiz -> {
            quiz.setTitle(updatedQuiz.getTitle());
            quiz.setDescription(updatedQuiz.getDescription());
            quizService.saveQuiz(quiz);
        });
        return "redirect:/admin/quiz/edit/" + id;
    }

    // Add question to quiz - POST /admin/quiz/{id}/question
    @PostMapping("/quiz/{id}/question")
    public String addQuestion(@PathVariable Long id,
                              @RequestParam String questionText,
                              @RequestParam String option1,
                              @RequestParam String option2,
                              @RequestParam String option3,
                              @RequestParam String option4,
                              @RequestParam String correctAnswer,
                              HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        Question question = new Question();
        question.setQuestionText(questionText);
        question.setOption1(option1);
        question.setOption2(option2);
        question.setOption3(option3);
        question.setOption4(option4);
        question.setCorrectAnswer(correctAnswer);
        quizService.addQuestionToQuiz(id, question);
        return "redirect:/admin/quiz/edit/" + id;
    }

    // Keep old endpoint as alias so existing links don't break
    @PostMapping("/quiz/{quizId}/question/add")
    public String addQuestionAlias(@PathVariable Long quizId,
                                   @RequestParam String questionText,
                                   @RequestParam String option1,
                                   @RequestParam String option2,
                                   @RequestParam String option3,
                                   @RequestParam String option4,
                                   @RequestParam String correctAnswer,
                                   HttpSession session) {
        return addQuestion(quizId, questionText, option1, option2, option3, option4, correctAnswer, session);
    }

    // Delete quiz
    @PostMapping("/quiz/delete/{id}")
    public String deleteQuiz(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        quizService.deleteQuiz(id);
        return "redirect:/admin";
    }

    // Delete question
    @PostMapping("/question/delete/{questionId}")
    public String deleteQuestion(@PathVariable Long questionId,
                                 @RequestParam Long quizId,
                                 HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        quizService.deleteQuestion(questionId);
        return "redirect:/admin/quiz/edit/" + quizId;
    }

    // Admin results dashboard: all quiz attempts sorted by score
    @GetMapping("/results")
    public String adminResults(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("results", resultRepository.findAllByOrderByScoreDesc());
        return "admin-results";
    }
}
