package de.dreja.quiz.model.json.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

public record AddPlayerEventDto(@JsonProperty(required = true)
                                @Nonnull
                                String name) {
}
