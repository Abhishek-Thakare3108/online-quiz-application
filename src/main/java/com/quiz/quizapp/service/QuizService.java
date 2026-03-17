package com.quiz.quizapp.service;

import com.quiz.quizapp.model.Question;
import com.quiz.quizapp.model.Quiz;
import com.quiz.quizapp.repository.QuestionRepository;
import com.quiz.quizapp.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    public Quiz saveQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    // Get all questions for a quiz object
    public List<Question> getQuestionsByQuiz(Quiz quiz) {
        return questionRepository.findByQuiz(quiz);
    }

    // Get all questions by quiz id directly
    public List<Question> getQuestionsByQuizId(Long quizId) {
        return quizRepository.findById(quizId)
                .map(questionRepository::findByQuiz)
                .orElse(List.of());
    }

    // Link question to quiz and save
    public void addQuestionToQuiz(Long quizId, Question question) {
        quizRepository.findById(quizId).ifPresent(quiz -> {
            question.setQuiz(quiz);
            questionRepository.save(question);
        });
    }

    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }

    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}
