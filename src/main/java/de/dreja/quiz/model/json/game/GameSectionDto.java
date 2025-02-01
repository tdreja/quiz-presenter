package de.dreja.quiz.model.json.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;

import java.util.List;

@Schema(name = "GameSection", description = "A named group of rounds within the game (e.g. Category)")
public record GameSectionDto(@Nonnull
                             @JsonProperty(required = true)
                             String name,
                             @Nonnull
                             @JsonProperty(required = true)
                             List<GameRoundDto> rounds) {
}
