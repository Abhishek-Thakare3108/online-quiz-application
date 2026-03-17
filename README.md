Online Quiz Application
Project Overview

The Online Quiz Application is a web-based system developed using Java Spring Boot that allows users to take quizzes on different topics. The application supports multiple-choice questions, tracks user progress, and provides feedback on quiz performance.

Users can register, log in, attempt quizzes, and view their scores. Administrators can manage quizzes by creating, editing, and deleting questions.

Features
User Features

User registration and login

View available quizzes

Attempt quizzes

View quiz results

Track past quiz attempts

View leaderboard rankings

Admin Features

Create quizzes

Add multiple-choice questions

Edit quizzes

Delete quizzes

View user results and leaderboard

Technologies Used
Technology	Purpose
Java	Backend development
Spring Boot	Application framework
Spring Data JPA	Database interaction
Thymeleaf	Frontend template engine
HTML / CSS / Bootstrap	User interface
MySQL	Database
Maven	Dependency management
System Architecture

The project follows MVC (Model-View-Controller) architecture.

Model: Entity classes representing database tables

View: Thymeleaf HTML templates

Controller: Handles HTTP requests and responses

Service: Contains business logic

Repository: Handles database operations

Database Structure

The application uses MySQL database with the following tables:

Users

Stores registered users.

Column	Description
id	User ID
username	User name
email	Login email
password	Hashed password
role	USER or ADMIN
Quizzes

Stores quiz details.

Column	Description
id	Quiz ID
title	Quiz title
description	Quiz description
Questions

Stores quiz questions.

Column	Description
id	Question ID
quiz_id	Associated quiz
question_text	Question
option1	Option
option2	Option
option3	Option
option4	Option
correct_answer	Correct answer
Results

Stores quiz attempts.

Column	Description
id	Result ID
user_id	User
quiz_id	Quiz
score	Score
total_questions	Total questions
attempted_at	Attempt time
Setup Instructions
1. Clone the Repository
git clone https://github.com/Abhishek-Thakare3108/online-quiz-application.git
2. Create Database

Open MySQL and run:

CREATE DATABASE quizdb;
3. Configure Database

Update application.properties

spring.datasource.url=jdbc:mysql://localhost:3306/quizdb
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
4. Run the Application

Run the project using Spring Tool Suite (STS) or IntelliJ.

Application will start at:

http://localhost:8080/login
Security

The application uses BCrypt password hashing to securely store user passwords.

Passwords are hashed before being stored in the database, preventing them from being stored in plain text.

Example hashed password:

$2a$10$xxxxxxxxxxxxxxxxxxxx
Leaderboard

The system includes a leaderboard that ranks users based on their quiz scores.

It displays:

Rank

Username

Quiz title

Score

Percentage

Future Improvements

Possible enhancements include:

Timer-based quizzes

Random question selection

Difficulty levels

Email verification

Password reset functionality

Author

Abhishek Thakare

GitHub:
https://github.com/Abhishek-Thakare3108
