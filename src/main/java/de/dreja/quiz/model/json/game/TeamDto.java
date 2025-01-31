package de.dreja.quiz.model.json.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Color;
import de.dreja.quiz.model.common.Emoji;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Set;

@Schema(name = "Team")
public record TeamDto(@Nonnull
                      @JsonProperty(required = true)
                      Color color,
                      @JsonProperty(required = true)
                      long points,
                      @JsonProperty(required = true)
                      @Nonnull Set<Emoji> players,
                      @Nullable
                      @JsonProperty
                      String gamepadId) {
}
