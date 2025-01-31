package de.dreja.quiz.model.json.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

public record AddPlayerEventDto(@JsonProperty(required = true, access = READ_ONLY)
                                @Nonnull
                                String playerName) implements EventDto {
    @Nonnull
    @Override
    public EventType getEventType() {
        return EventType.ADD_PLAYER;
    }
}
