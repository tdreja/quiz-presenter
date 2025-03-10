package de.dreja.quiz.service.mapping;

import de.dreja.quiz.model.json.game.GameDto;
import de.dreja.quiz.model.persistence.game.Game;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {PlayerMapper.class, TeamMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface GameMapper {

    @Mapping(target = "sections", ignore = true)
    @Mapping(target = "currentSection", ignore = true)
    @Mapping(target = "currentRound", ignore = true)
    GameDto toJson(Game game);

    @Mapping(target = "locale", ignore = true)
    @Mapping(target = "teams", ignore = true)
    @Mapping(target = "players", ignore = true)
    void fromJson(GameDto gameDto, @MappingTarget Game game);
}
