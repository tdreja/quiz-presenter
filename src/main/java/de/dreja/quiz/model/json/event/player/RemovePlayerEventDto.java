package de.dreja.quiz.model.json.event.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Emoji;
import de.dreja.quiz.model.json.event.Changes;
import de.dreja.quiz.model.json.event.GameEventDto;
import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.Player;
import de.dreja.quiz.service.persistence.GameService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;

import java.util.Iterator;

@Schema(description = "Remove a player from the current game")
@JsonIgnoreProperties(ignoreUnknown = true)
public record RemovePlayerEventDto(@Nonnull
                                   @JsonProperty(required = true)
                                   Emoji playerEmoji) implements GameEventDto {

    @Nonnull
    @Override
    public Changes updateGame(@Nonnull Game game, @Nonnull GameService service) {
        for(Iterator<Player> iterator = game.getPlayers().iterator(); iterator.hasNext(); ) {
            Player player = iterator.next();
            if(player.getEmoji() == playerEmoji) {
                iterator.remove();
                return Changes.builder().updated(game).deleted(player).build();
            }
        }
        return Changes.none();
    }
}
