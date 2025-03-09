package de.dreja.quiz.model.json.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.dreja.quiz.model.json.event.player.AddPlayerEventDto;
import de.dreja.quiz.model.json.event.player.ReRollPlayerEmojiEventDto;
import de.dreja.quiz.model.json.event.player.RemovePlayerEventDto;
import de.dreja.quiz.model.json.event.player.RenamePlayerEventDto;
import de.dreja.quiz.model.json.event.team.*;
import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.service.persistence.GameService;
import jakarta.annotation.Nonnull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * Tries to update the game based on the input data from the event
     * @param game Current game state
     * @param service Service with utilities
     * @return List of changed objects
     */
    @Nonnull
    default Changes updateGame(@Nonnull Game game, @Nonnull GameService service) {
        return Changes.none();
    }
}
