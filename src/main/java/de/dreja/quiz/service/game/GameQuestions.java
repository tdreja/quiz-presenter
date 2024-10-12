package de.dreja.quiz.service.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.dreja.quiz.model.persistence.game.GameQuestion;
import de.dreja.quiz.model.persistence.game.Team;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class GameQuestions {

    private final EntityManager entityManager;
    private final Teams teams;

    @Autowired
    GameQuestions(EntityManager entityManager, Teams teams) {
        this.entityManager = entityManager;
        this.teams = teams;
    }

    @Transactional
    public void answerQuestion(@Nonnull GameQuestion question, @Nullable Team team) {
        final GameQuestion editQuestion = entityManager.merge(question);
        editQuestion.setAnswered(true);

        final boolean complete = editQuestion.getSection().getQuestions().stream().allMatch(GameQuestion::isAnswered);
        editQuestion.getSection().setComplete(complete);

        if(team != null) {
            editQuestion.setAnsweredBy(entityManager.merge(team));
        }
        teams.alterPoints(team, editQuestion.getPoints());
    }
}
