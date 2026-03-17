package com.quiz.quizapp.service;

import com.quiz.quizapp.model.Result;
import com.quiz.quizapp.model.User;
import com.quiz.quizapp.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    public Result saveResult(Result result) {
        return resultRepository.save(result);
    }

    public boolean hasAttempted(Long userId, Long quizId) {
        return resultRepository.existsByUserIdAndQuizId(userId, quizId);
    }

    public Optional<Result> getResultById(Long id) {
        return resultRepository.findById(id);
    }

    // Get all past attempts for a user (progress tracking)
    public List<Result> getResultsByUser(User user) {
        return resultRepository.findByUser(user);
    }

    // Get set of quiz IDs already attempted by a user (for dashboard display)
    public java.util.Set<Long> getAttemptedQuizIds(User user) {
        return resultRepository.findByUser(user).stream()
                .map(r -> r.getQuiz().getId())
                .collect(java.util.stream.Collectors.toSet());
    }
}
