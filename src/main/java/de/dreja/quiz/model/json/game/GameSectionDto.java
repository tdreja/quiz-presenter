package de.dreja.quiz.model.json.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

import java.util.List;

public record GameSectionDto(@Nonnull
                             @JsonProperty(required = true)
                             String name,
                             @Nonnull
                             @JsonProperty(required = true)
                             List<GameRoundDto> rounds) {
}
