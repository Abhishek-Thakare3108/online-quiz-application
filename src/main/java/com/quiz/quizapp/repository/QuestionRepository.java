package com.quiz.quizapp.repository;

import com.quiz.quizapp.model.Question;
import com.quiz.quizapp.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuiz(Quiz quiz);
}
