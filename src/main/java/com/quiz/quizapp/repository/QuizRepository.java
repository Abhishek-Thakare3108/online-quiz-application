package com.quiz.quizapp.repository;

import com.quiz.quizapp.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
