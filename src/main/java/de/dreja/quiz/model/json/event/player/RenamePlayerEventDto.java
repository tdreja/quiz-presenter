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

@Schema(description = "Rename a player")
@JsonIgnoreProperties(ignoreUnknown = true)
public record RenamePlayerEventDto(@Nonnull
                                   @JsonProperty(required = true)
                                   Emoji playerEmoji,
                                   @Nonnull
                                   @JsonProperty(required = true)
                                   String newName) implements GameEventDto {

    @Nonnull
    @Override
    public Changes updateGame(@Nonnull Game game, @Nonnull GameService service) {
        final var existingPlayer = game.getPlayers().stream()
                .filter(p -> p.getEmoji() == playerEmoji)
                .findAny();
        if(existingPlayer.isEmpty()) {
            return Changes.none();
        }
        final var player = existingPlayer.get();
        if(player.getName().equals(newName)) {
            return Changes.none();
        }
        player.setName(newName);
        return Changes.builder().updated(player).build();
    }
}
