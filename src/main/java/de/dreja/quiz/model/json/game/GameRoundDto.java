package de.dreja.quiz.model.json.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Color;
import de.dreja.quiz.model.common.RoundState;
import de.dreja.quiz.model.json.quiz.QuestionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;

import java.util.List;

@Schema(name = "GameRound", description = "One round (aka question) in the quiz")
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
                           String inSection,
                           @Nonnull
                           @JsonProperty(required = true)
                           QuestionDto question) {
}
