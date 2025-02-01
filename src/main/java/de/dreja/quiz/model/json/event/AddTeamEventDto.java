package de.dreja.quiz.model.json.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Color;
import jakarta.annotation.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AddTeamEventDto(@Nullable
                              @JsonProperty
                              @JsonInclude(JsonInclude.Include.NON_NULL)
                              Color targetColor) implements GameEventDto {
}
