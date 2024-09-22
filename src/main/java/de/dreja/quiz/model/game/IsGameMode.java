package de.dreja.quiz.model.game;

import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.Player;
import de.dreja.quiz.model.persistence.game.Team;
import de.dreja.quiz.model.persistence.quiz.Quiz;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;

/**
 * Common API to convert a Quiz into an active game
 */
public interface IsGameMode {
    
    @Nonnull
    @Transactional
    Game prepareGame(@Nonnull Quiz quiz);

    @Transactional
    void onCorrectAnswer(@Nonnull Game game, @Nullable Team team, @Nullable Player player);

    @Transactional
    void onWrongAnswer(@Nonnull Game game, @Nullable Team team, @Nullable Player player);
}
