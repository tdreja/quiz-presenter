package de.dreja.quiz.service.game;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.dreja.quiz.model.json.game.AnswerFromTeam;
import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.GameQuestion;
import de.dreja.quiz.model.persistence.game.GameSection;
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
    public void answerQuestion(@Nonnull Game game, @Nonnull GameQuestion question, @Nullable AnswerFromTeam answer) {
        final GameQuestion editQuestion = entityManager.merge(question);
        // Always mark the question as answered!
        if(!editQuestion.isAnswered()) {
            editQuestion.setAnswered(true);
        }

        // Ensure that the section is marked as complete, if necessary
        final Game editGame = entityManager.merge(game);
        completeSection(editGame, editQuestion.getSection());


        // Check whether or not the answer by the team deserves points
        checkTeamAnswer(editGame, editQuestion, answer);
    }

    @Transactional
    protected void completeSection(@Nonnull Game game, @Nonnull GameSection section) {
        if(section.isComplete()) {
            return;
        }
        
        final boolean complete = section.getQuestions().stream().allMatch(GameQuestion::isAnswered);
        section.setComplete(complete);

        // Check if the game is done already
        if(complete && game.getEnd() == null && game.getSections().stream().allMatch(GameSection::isComplete)) {
            game.setEnd(LocalDateTime.now());
        }
    }

    @Transactional
    protected void checkTeamAnswer(@Nonnull Game game, @Nonnull GameQuestion question, @Nullable AnswerFromTeam answer) {
        if(answer == null) {
            return;
        }
        final Team team = teams.getTeamByColor(game, answer.color()).orElse(null);
        if(team == null) {
            return;
        }
        if(!question.getQuestion().isAnswerCorrect(answer.answer())) {
            return;
        }
        if(question.getAnsweredBy() == null) {
            question.setAnsweredBy(team);
        }
        teams.alterPoints(team, question.getPoints());
    }
}
