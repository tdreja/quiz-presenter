package de.dreja.quiz.model.game;

import de.dreja.quiz.model.persistence.game.GameQuestion;
import de.dreja.quiz.model.persistence.game.Team;
import de.dreja.quiz.model.persistence.quiz.Question;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameQuestionService {

    private final EntityManager entityManager;

    @Autowired
    GameQuestionService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void answerQuestion(@Nonnull GameQuestion question, @Nullable Team team) {
        final Question editQuestion = entityManager.merge(question);
        question.setAnswered(true);
        if(team != null) {
            team.setPoints(team.getPoints() + question.getPoints());
            question.setAnsweredBy(team);
        }
    }
}
