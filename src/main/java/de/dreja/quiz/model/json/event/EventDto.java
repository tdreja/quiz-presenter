package de.dreja.quiz.model.json.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

public interface EventDto {

    @Nonnull
    @JsonProperty(required = true, access = READ_ONLY)
    EventType getEventType();
}
