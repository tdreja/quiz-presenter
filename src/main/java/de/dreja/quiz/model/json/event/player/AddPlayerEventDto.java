package de.dreja.quiz.model.json.event.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Emoji;
import de.dreja.quiz.model.json.event.Changes;
import de.dreja.quiz.model.json.event.GameEventDto;
import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.Player;
import de.dreja.quiz.model.persistence.game.Team;
import de.dreja.quiz.service.persistence.GameService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;

@Schema(description = "Add a new player to the current game")
@JsonIgnoreProperties(ignoreUnknown = true)
public record AddPlayerEventDto(@JsonProperty(required = true)
                                @Nonnull
                                String playerName) implements GameEventDto {

    @Override
    @Nonnull
    public Changes updateGame(@Nonnull Game game, @Nonnull GameService service) {
        final var reservedEmoji = service.reserveAvailableEmoji(game);
        if (reservedEmoji.isEmpty()) {
            return Changes.none(); // No Emoji available anymore
        }
        final var reservedTeam = service.findSmallestTeam(game);
        if (reservedTeam.isEmpty()) {
            return Changes.none(); // No team available
        }

        final Team team = reservedTeam.get();
        final Emoji emoji = reservedEmoji.get();

        final Player player = new Player();
        player.setName(playerName);
        player.setEmoji(emoji);
        game.getPlayers().add(player);
        player.setGame(game);
        team.getPlayers().add(player);
        player.setTeam(team);

        // Game, team and player are updated
        return Changes.builder().created(player).updated(team).updated(game).build();
    }
}
