package de.dreja.quiz.service.mapping;

import de.dreja.quiz.model.common.Emoji;
import de.dreja.quiz.model.json.game.TeamDto;
import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.Player;
import de.dreja.quiz.model.persistence.game.Team;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TeamMapper {

    TeamDto toJson(Team team);

    @Mapping(target = "game", ignore = true)
    @Mapping(target = "players", ignore = true)
    void fromJson(TeamDto teamDto, @MappingTarget Team team, @Context Game game);

    @Nonnull
    default List<Emoji> getPlayers(@Nullable List<Player> players) {
        if (players == null || players.isEmpty()) {
            return Collections.emptyList();
        }
        return players.stream()
                .map(Player::getEmoji)
                .toList();
    }
}
