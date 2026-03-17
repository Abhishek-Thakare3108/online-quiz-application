package com.quiz.quizapp.repository;

import com.quiz.quizapp.model.Result;
import com.quiz.quizapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByUser(User user);
    boolean existsByUserIdAndQuizId(Long userId, Long quizId);
    List<Result> findTop10ByOrderByScoreDesc();
    List<Result> findAllByOrderByScoreDesc();
}
