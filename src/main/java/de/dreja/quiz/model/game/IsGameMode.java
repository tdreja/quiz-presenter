package de.dreja.quiz.model.game;

import java.util.List;

import de.dreja.quiz.model.json.game.AnswerFromTeam;
import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.GameQuestion;
import de.dreja.quiz.model.persistence.quiz.Quiz;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;

/**
 * Common API to convert a Quiz into an active game
 */
public interface IsGameMode {
    
    @Nonnull
    @Transactional
    Game prepareGame(@Nonnull Quiz quiz);

    @Transactional
    void onAnswersReceived(@Nonnull Game game, @Nonnull List<AnswerFromTeam> answers);

    @Transactional
    boolean onQuestionSelected(@Nonnull Game game, @Nonnull GameQuestion question);
}
