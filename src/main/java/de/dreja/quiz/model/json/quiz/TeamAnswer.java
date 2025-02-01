package de.dreja.quiz.model.json.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Color;
import jakarta.annotation.Nonnull;

public record TeamAnswer(@Nonnull
                         @JsonProperty(required = true)
                         Color byTeam,
                         @Nonnull
                         @JsonProperty(required = true)
                         String answer) {
}
