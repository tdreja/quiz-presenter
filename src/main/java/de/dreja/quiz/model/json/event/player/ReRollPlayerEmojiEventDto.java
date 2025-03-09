package de.dreja.quiz.model.json.event.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Emoji;
import de.dreja.quiz.model.json.event.Changes;
import de.dreja.quiz.model.json.event.GameEventDto;
import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.service.persistence.GameService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;

@Schema(description = "Change the emoji of an existing player")
@JsonIgnoreProperties(ignoreUnknown = true)
public record ReRollPlayerEmojiEventDto(@Nonnull
                                        @JsonProperty(required = true)
                                        Emoji currentEmoji) implements GameEventDto {

    @Nonnull
    @Override
    public Changes updateGame(@Nonnull Game game, @Nonnull GameService service) {
        final var existingPlayer = game.getPlayers().stream()
                .filter(p -> p.getEmoji() == currentEmoji)
                .findAny();
        if(existingPlayer.isEmpty()) {
            return Changes.none(); // No player found
        }
        final var reservedEmoji = service.reserveAvailableEmoji(game);
        if (reservedEmoji.isEmpty()) {
            return Changes.none(); // No Emoji available anymore
        }
        final var player = existingPlayer.get();
        final var emoji = reservedEmoji.get();
        player.setEmoji(emoji);
        return Changes.builder().updated(game).updated(player).build();
    }
}
