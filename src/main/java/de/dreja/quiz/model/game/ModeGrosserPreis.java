package de.dreja.quiz.model.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.GameSection;
import de.dreja.quiz.model.persistence.game.Player;
import de.dreja.quiz.model.persistence.game.Team;
import de.dreja.quiz.model.persistence.quiz.Question;
import de.dreja.quiz.model.persistence.quiz.Quiz;
import de.dreja.quiz.model.persistence.quiz.Section;
import de.dreja.quiz.service.persistence.game.GameSetupService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;

@GameMode
public class ModeGrosserPreis implements IsGameMode {

    ModeGrosserPreis(
            @Value(value = "${de.dreja.quiz.game-modes.grosser-preis.starting-points:100}") long startingPoints,
            @Value(value = "${de.dreja.quiz.game-modes.grosser-preis.point-increment:100}") long pointIncrements,
            GameSetupService gameSetupService,
            Teams teams) {
        this.startingPoints = startingPoints;
        this.pointIncrement = pointIncrements;
        this.gameSetupService = gameSetupService;
        this.teams = teams;
    }

    private static final Comparator<Question> LOWEST_DIFFICULTY_FIRST = Comparator
            .comparingInt(Question::getDifficulty);

    private final Teams teams;
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
    public void onCorrectAnswer(@Nonnull Game game, @Nullable Team team, @Nullable Player player) {
        final var current = game.getCurrentQuestion();
        if (current == null) {
            return;
        }

        // Store points
        current.setAnswered(true);
        current.setAnsweredBy(team);
        teams.alterPoints(team, current.getPoints());

        // Prepare for next question
        game.setCurrentQuestion(null);
        game.setActiveTeam(teams.getNextTeam(game, team));
    }

    @Override
    @Transactional
    public void onWrongAnswer(@Nonnull Game game, @Nullable Team team, @Nullable Player player) {
        final var current = game.getCurrentQuestion();
        if (current == null) {
            return;
        }

        // Mark question as done
        current.setAnswered(true);
        
        // Prepare for next question
        game.setCurrentQuestion(null);
        game.setActiveTeam(teams.getNextTeam(game, null));
    }
}
