package de.dreja.quiz.model.json.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

public interface EventDto {

    @Nonnull
    @JsonProperty(required = true)
    EventType getEventType();
}
