package de.dreja.quiz.model.json.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Color;
import de.dreja.quiz.model.common.RoundState;
import jakarta.annotation.Nonnull;

import java.util.List;

public record GameRoundDto(@Nonnull
                           @JsonProperty(required = true)
                           String key,
                           @Nonnull
                           @JsonProperty(required = true)
                           RoundState state,
                           @Nonnull
                           @JsonProperty(required = true)
                           List<Color> currentlyAttempting,
                           @Nonnull
                           @JsonProperty(required = true)
                           List<Color> alreadyAttempted,
                           @Nonnull
                           @JsonProperty(required = true)
                           List<Color> completedBy,
                           @Nonnull
                           @JsonProperty(required = true)
                           String inSection) {
}
