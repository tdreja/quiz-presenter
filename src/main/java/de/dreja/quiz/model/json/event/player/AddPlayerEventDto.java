package de.dreja.quiz.model.json.event.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.json.event.GameEventDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;

@Schema(description = "Add a new player to the current game")
@JsonIgnoreProperties(ignoreUnknown = true)
public record AddPlayerEventDto(@JsonProperty(required = true)
                                @Nonnull
                                String playerName) implements GameEventDto {
}
