package de.dreja.quiz.model.json.event.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Color;
import de.dreja.quiz.model.json.event.GameEventDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

@Schema(description = "Assigns the current controller ID to the team")
@JsonIgnoreProperties(ignoreUnknown = true)
public record AssignGamepadEventDto(@Nonnull
                                    @JsonProperty(required = true)
                                    Color teamColor,
                                    @Nullable
                                    @JsonProperty
                                    String gamepadId) implements GameEventDto {
}
