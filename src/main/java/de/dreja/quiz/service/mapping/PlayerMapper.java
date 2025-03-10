package de.dreja.quiz.service.mapping;

import de.dreja.quiz.model.json.game.PlayerDto;
import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.Player;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PlayerMapper {

    @Mapping(target = "assignedToTeam", source = "team.color")
    PlayerDto toJson(Player player);

    @Mapping(target = "team", ignore = true)
    @Mapping(target = "game", ignore = true)
    void fromJson(PlayerDto playerDto, @MappingTarget Player player, @Context Game game);
}
