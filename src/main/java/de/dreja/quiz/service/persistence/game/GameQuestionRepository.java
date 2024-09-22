package de.dreja.quiz.service.persistence.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.dreja.quiz.model.persistence.game.GameQuestion;

@Repository
public interface GameQuestionRepository extends JpaRepository<GameQuestion, Long> {
}
