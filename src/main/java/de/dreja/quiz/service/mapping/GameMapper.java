package de.dreja.quiz.service.mapping;

import de.dreja.quiz.model.json.game.GameDto;
import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.service.persistence.game.GameRepository;
import jakarta.annotation.Nullable;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {PlayerMapper.class, TeamMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class GameMapper {

    @Autowired
    protected TeamMapper teamMapper;

    @Autowired
    protected PlayerMapper playerMapper;

    @Autowired
    protected GameRepository gameRepository;

    @Mapping(target = "sections", ignore = true)
    @Mapping(target = "currentSection", ignore = true)
    @Mapping(target = "currentRound", ignore = true)
    @Nullable
    public abstract GameDto toJson(@Nullable Game game);

    @Nullable
    @Transactional
    public Game toEntity(@Nullable GameDto gameDto, @Nullable Long gameId) {
        if (gameDto == null) {
            return null;
        }

        final Optional<Game> existing = gameId == null ? Optional.empty() : gameRepository.findById(gameId);
        final Game game;
        if (existing.isPresent()) {
            game = existing.get();
        } else {
            game = new Game();
            gameRepository.save(game);
        }
        updateDb(gameDto, game);
        teamMapper.updateTeamsInGame(gameDto.teams(), game);
        playerMapper.updatePlayersInGame(gameDto.players(), game);
        return game;
    }

    @Mapping(target = "locale", ignore = true)
    @Mapping(target = "teams", ignore = true)
    @Mapping(target = "players", ignore = true)
    protected abstract void updateDb(@Nullable
                                     GameDto gameDto,
                                     @MappingTarget
                                     @Nullable
                                     Game game);
}
