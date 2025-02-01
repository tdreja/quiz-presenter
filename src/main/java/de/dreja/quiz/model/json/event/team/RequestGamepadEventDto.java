package de.dreja.quiz.model.json.event.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Color;
import de.dreja.quiz.model.json.event.GameEventDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;

@Schema(description = "Requests the assignment of a controller to the team")
@JsonIgnoreProperties(ignoreUnknown = true)
public record RequestGamepadEventDto(@Nonnull
                                     @JsonProperty(required = true)
                                     Color teamColor) implements GameEventDto {
}
