package de.dreja.quiz.model.json.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.dreja.quiz.model.json.event.player.AddPlayerEventDto;
import de.dreja.quiz.model.json.event.player.ReRollPlayerEmojiEventDto;
import de.dreja.quiz.model.json.event.player.RemovePlayerEventDto;
import de.dreja.quiz.model.json.event.player.RenamePlayerEventDto;
import de.dreja.quiz.model.json.event.team.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        include = JsonTypeInfo.As.PROPERTY
)
@JsonSubTypes({
        // Players
        @Type(value = AddPlayerEventDto.class, name = "add-player"),
        @Type(value = RemovePlayerEventDto.class, name = "remove-player"),
        @Type(value = RenamePlayerEventDto.class, name = "rename-player"),
        @Type(value = ReRollPlayerEmojiEventDto.class, name = "re-roll-player-emoji"),
        // Teams
        @Type(value = AddTeamEventDto.class, name = "add-team"),
        @Type(value = RemoveTeamEventDto.class, name = "remove-team"),
        @Type(value = ShuffleTeamsEventDto.class, name = "shuffle-teams"),
        @Type(value = AssignGamepadEventDto.class, name = "assign-gamepad"),
        @Type(value = RequestGamepadEventDto.class, name = "request-gamepad")
})
public interface GameEventDto {
}
