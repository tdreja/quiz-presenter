package de.dreja.quiz.model.json.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AddPlayerEventDto(@JsonProperty(required = true)
                                @Nonnull
                                String playerName) implements GameEventDto {
}
