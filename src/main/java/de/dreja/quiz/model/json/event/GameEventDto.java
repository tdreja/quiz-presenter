package de.dreja.quiz.model.json.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        include = JsonTypeInfo.As.PROPERTY
)
@JsonSubTypes({
        @Type(value = AddPlayerEventDto.class, name = "add-player"),
        @Type(value = AddTeamEventDto.class, name = "add-team")
})
public interface GameEventDto {
}
