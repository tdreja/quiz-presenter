package de.dreja.quiz.model.json.event.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Color;
import de.dreja.quiz.model.json.event.GameEventDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;

import java.util.List;

@Schema(description = "Shuffle all players into new teams")
@JsonIgnoreProperties(ignoreUnknown = true)
public record ShuffleTeamsEventDto(@JsonProperty(required = true)
                                   @Nonnull
                                   List<Color> newTeams) implements GameEventDto {
}
