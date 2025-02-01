package de.dreja.quiz.model.json.event.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Color;
import de.dreja.quiz.model.json.event.GameEventDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;

@Schema(description = "Remove a team from the current game")
@JsonIgnoreProperties(ignoreUnknown = true)
public record RemoveTeamEventDto(@Nonnull
                                 @JsonProperty(required = true)
                                 Color teamColor) implements GameEventDto {
}
