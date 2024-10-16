package de.dreja.quiz.model.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import de.dreja.quiz.model.json.game.AnswerFromTeam;
import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.GameQuestion;
import de.dreja.quiz.model.persistence.game.GameSection;
import de.dreja.quiz.model.persistence.game.Team;
import de.dreja.quiz.model.persistence.quiz.Question;
import de.dreja.quiz.model.persistence.quiz.Quiz;
import de.dreja.quiz.model.persistence.quiz.Section;
import de.dreja.quiz.service.game.GameQuestions;
import de.dreja.quiz.service.game.Teams;
import de.dreja.quiz.service.persistence.game.GameSetupService;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;

@GameMode
public class ModeGrosserPreis implements IsGameMode {

    ModeGrosserPreis(
            @Value(value = "${de.dreja.quiz.game-modes.grosser-preis.starting-points:100}") long startingPoints,
            @Value(value = "${de.dreja.quiz.game-modes.grosser-preis.point-increment:100}") long pointIncrements,
            GameSetupService gameSetupService,
            Teams teams,
            GameQuestions gameQuestions) {
        this.startingPoints = startingPoints;
        this.pointIncrement = pointIncrements;
        this.gameSetupService = gameSetupService;
        this.teams = teams;
        this.questions = gameQuestions;
    }

    private static final Comparator<Question> LOWEST_DIFFICULTY_FIRST = Comparator
            .comparingInt(Question::getDifficulty);

    private static final Comparator<Team> LOWEST_POINTS_FIRST = Comparator.comparingLong(Team::getPoints);
    private static final Comparator<Team> HIGHEST_POINTS_FIRST = LOWEST_POINTS_FIRST.reversed();

    private final Teams teams;
    private final GameQuestions questions;
    private final GameSetupService gameSetupService;
    private final long startingPoints;
    private final long pointIncrement;

    @Override
    @Nonnull
    @Transactional
    public Game prepareGame(@Nonnull Quiz quiz) {
        final Game game = gameSetupService.newGame(quiz).setGameMode(this.getClass());

        for (Section section : quiz.getSections()) {
            final GameSection gameSection = gameSetupService.newSection(game, section, section.getName());

            final List<Question> easiestFirst = new ArrayList<>(section.getQuestions());
            easiestFirst.sort(LOWEST_DIFFICULTY_FIRST);

            long points = startingPoints;
            for (Question question : easiestFirst) {
                gameSetupService.newQuestion(gameSection, question, points);
                points += pointIncrement;
            }
        }

        return game;
    }

    

    @Override
    @Transactional
    public void onAnswersReceived(@Nonnull Game game, @Nonnull List<AnswerFromTeam> answers) {
        final GameQuestion question = game.getCurrentQuestion();
        if (question == null) {
            return;
        }

        // Mark question as answered, by either none or more teams
        if(answers.isEmpty()) {
            questions.answerQuestion(game, question, null);
        } else {
            for(AnswerFromTeam answer : answers) {
                questions.answerQuestion(game, question, answer);
            }
        }

        // Prepare for next question
        game.setCurrentQuestion(null);
        game.setWaitForTeamInput(false);
        
        // Are we done already?
        if(game.getEnd() != null) {
            final Team winner = game.getTeams().stream().sorted(HIGHEST_POINTS_FIRST).findFirst().orElse(null);
            game.setActiveTeam(winner);
        } else {
            // Otherwise the next team gets to choose
            game.setActiveTeam(teams.getNextInOrder(game));
        }
    }

    @Override
    @Transactional
    public boolean onQuestionSelected(@Nonnull Game game, @Nonnull GameQuestion question) {
        game.setCurrentQuestion(question);
        game.setActiveTeam(null);
        game.setActivePlayer(null);
        game.setWaitForTeamInput(false);
        return true;
    }
}
