package de.dreja.quiz.model.json.event.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Emoji;
import de.dreja.quiz.model.json.event.GameEventDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;

@Schema(description = "Remove a player from the current game")
@JsonIgnoreProperties(ignoreUnknown = true)
public record RemovePlayerEventDto(@Nonnull
                                   @JsonProperty(required = true)
                                   Emoji playerEmoji) implements GameEventDto {
}
