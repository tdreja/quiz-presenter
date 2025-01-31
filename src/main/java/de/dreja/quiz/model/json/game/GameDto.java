package de.dreja.quiz.model.json.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Color;
import de.dreja.quiz.model.common.Emoji;
import de.dreja.quiz.model.common.GameState;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Map;
import java.util.Set;

public record GameDto(@Nonnull
                      @JsonProperty(required = true)
                      GameState state,
                      @Nonnull
                      @JsonProperty(required = true)
                      Map<String, GameSectionDto> sections,
                      @Nonnull
                      @JsonProperty(required = true)
                      Set<Emoji> availableEmoji,
                      @Nonnull
                      @JsonProperty(required = true)
                      Set<Color> availableColors,
                      @Nonnull
                      @JsonProperty(required = true)
                      Map<Emoji, PlayerDto> players,
                      @Nonnull
                      @JsonProperty(required = true)
                      Map<Color, TeamDto> teams,
                      @JsonProperty(required = true)
                      long roundCounter,
                      @Nullable
                      @JsonProperty
                      String currentSection,
                      @Nullable
                      @JsonProperty
                      String currentRound) {
}
