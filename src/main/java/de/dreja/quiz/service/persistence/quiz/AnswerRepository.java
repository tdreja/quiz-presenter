package de.dreja.quiz.service.persistence.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.dreja.quiz.model.persistence.quiz.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
