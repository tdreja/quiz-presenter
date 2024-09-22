package de.dreja.quiz.service.persistence.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.dreja.quiz.model.persistence.quiz.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
