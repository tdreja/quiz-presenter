package de.dreja.quiz.model.json.game;

import de.dreja.quiz.model.persistence.game.Color;
import jakarta.annotation.Nonnull;

public record AnswerFromTeam(@Nonnull Color color, @Nonnull String answer) {
    
}
