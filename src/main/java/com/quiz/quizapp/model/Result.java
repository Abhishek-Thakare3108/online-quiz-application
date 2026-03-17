package com.quiz.quizapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which user attempted
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Which quiz was attempted
    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    private int score;          // number of correct answers
    private int totalQuestions; // total questions in quiz
    private LocalDateTime attemptedAt;

    @PrePersist
    protected void onCreate() {
        this.attemptedAt = LocalDateTime.now();
    }

    public Result() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public LocalDateTime getAttemptedAt() { return attemptedAt; }
    public void setAttemptedAt(LocalDateTime attemptedAt) { this.attemptedAt = attemptedAt; }
}
