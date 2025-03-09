package de.dreja.quiz.service.mapping;

import de.dreja.quiz.model.json.game.PlayerDto;
import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.Player;
import de.dreja.quiz.model.persistence.game.Team;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PlayerMapper {

    @Mapping(target = "assignedToTeam", source = "team.color")
    PlayerDto toJson(Player player);

    @Mapping(target = "team", ignore = true)
    @Mapping(target = "game", ignore = true)
    void fromJson(PlayerDto playerDto, @MappingTarget Player player, @Context Game game);

    @AfterMapping
    default void updateEntity(PlayerDto playerDto, @MappingTarget Player player, @Context Game game) {
        if(!game.getPlayers().contains(player)) {
            player.setGame(game);
            game.getPlayers().add(player);
        }
        final Team oldTeam = player.getTeam();
        final Team newTeam = game.getTeams().stream()
                .filter(t -> t.getColor() == playerDto.assignedToTeam())
                .findAny()
                .orElseThrow();
        if(oldTeam.equals(newTeam)) {
            return;
        }
        oldTeam.getPlayers().remove(player);
        newTeam.getPlayers().add(player);
        player.setTeam(newTeam);
    }
}
