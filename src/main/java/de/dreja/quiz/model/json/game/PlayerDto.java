package de.dreja.quiz.model.json.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Color;
import de.dreja.quiz.model.common.Emoji;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;

@Schema(name = "Player", description = "One player within the game with a unique Emoji")
public record PlayerDto(@Nonnull
                        @JsonProperty(required = true)
                        Emoji emoji,
                        @Nonnull
                        @JsonProperty(required = true)
                        String name,
                        @JsonProperty(required = true)
                        long points,
                        @Nonnull
                        @JsonProperty(required = true)
                        Color assignedToTeam) {
}
