package de.dreja.quiz.service.mapping;

import de.dreja.quiz.model.common.Emoji;
import de.dreja.quiz.model.json.game.PlayerDto;
import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.Player;
import de.dreja.quiz.model.persistence.game.Team;
import de.dreja.quiz.service.persistence.game.PlayerRepository;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PlayerMapper {

    @Autowired
    protected TeamMapper teamMapper;

    @Autowired
    protected PlayerRepository playerRepository;

    @Mapping(target = "assignedToTeam", source = "team.color")
    @Nullable
    public abstract PlayerDto toJson(@Nullable Player player);

    @Transactional
    public void updatePlayersInGame(@Nullable Map<Emoji, PlayerDto> newPlayers, @Nullable Game game) {
        if (game == null) {
            return;
        }
        final Map<Emoji, PlayerDto> toAdd = new EnumMap<>(Emoji.class);
        if (newPlayers != null) {
            toAdd.putAll(newPlayers);
        }

        final List<Player> toDelete = new LinkedList<>();
        for (Player player : game.getPlayers()) {
            final PlayerDto dto = toAdd.remove(player.getEmoji());
            if (dto != null) {
                updatePlayer(dto, player, game);
                playerRepository.save(player);
            } else {
                toDelete.add(player);
            }
        }

        // TODO Check if delete works correctly
        playerRepository.deleteAll(toDelete);

        for (PlayerDto dto : toAdd.values()) {
            final Player newPlayer = new Player();
            newPlayer.setGame(game);
            game.getPlayers().add(newPlayer);
            updatePlayer(dto, newPlayer, game);
            playerRepository.save(newPlayer);
        }
    }

    @SuppressWarnings("ConstantValue")
    @Transactional
    protected void updatePlayer(@Nonnull PlayerDto dto, @Nonnull Player player, @Nonnull Game game) {
        updateAttributes(dto, player, game);

        final Team newTeam = teamMapper.findOrCreate(game, dto.assignedToTeam());
        if (newTeam == null) {
            return;
        }
        final Team oldTeam = player.getTeam();
        if (newTeam.equals(oldTeam)) {
            return;
        }
        if (oldTeam != null) {
            oldTeam.getPlayers().remove(player);
        }
        player.setTeam(newTeam);
        newTeam.getPlayers().add(player);
    }

    @Mapping(target = "team", ignore = true)
    @Mapping(target = "game", ignore = true)
    protected abstract void updateAttributes(@Nullable
                                             PlayerDto playerDto,
                                             @Nullable
                                             @MappingTarget
                                             Player player,
                                             @Nullable
                                             @Context
                                             Game game);
}
