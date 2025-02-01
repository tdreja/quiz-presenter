package de.dreja.quiz.model.json.event.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Color;
import de.dreja.quiz.model.json.event.GameEventDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

@Schema(description = "Adds a new, empty team to the game")
@JsonIgnoreProperties(ignoreUnknown = true)
public record AddTeamEventDto(@Nullable
                              @JsonProperty
                              @JsonInclude(JsonInclude.Include.NON_NULL)
                              Color targetColor) implements GameEventDto {
}
