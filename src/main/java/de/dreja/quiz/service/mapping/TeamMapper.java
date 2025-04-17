package de.dreja.quiz.service.mapping;

import de.dreja.quiz.model.common.Color;
import de.dreja.quiz.model.common.Emoji;
import de.dreja.quiz.model.json.game.TeamDto;
import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.Player;
import de.dreja.quiz.model.persistence.game.Team;
import de.dreja.quiz.service.persistence.game.TeamRepository;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class TeamMapper {

    @Autowired
    protected TeamRepository teamRepository;

    @Nullable
    public abstract TeamDto toJson(@Nullable Team team);

    @Mapping(target = "game", ignore = true)
    @Mapping(target = "players", ignore = true)
    protected abstract void updateDb(@Nullable
                                     TeamDto teamDto,
                                     @MappingTarget
                                     @Nullable
                                     Team team);

    @Transactional
    public void updateTeamsInGame(@Nullable Map<Color, TeamDto> teamDtos, @Nullable Game game) {
        if(game == null) {
            return;
        }
        final Map<Color, TeamDto> toAdd = new EnumMap<>(Color.class);
        if(teamDtos != null) {
            toAdd.putAll(teamDtos);
        }

        final List<Team> toDelete = new LinkedList<>();
        for(Team team : game.getTeams()) {
            final TeamDto dto = toAdd.remove(team.getColor());
            if(dto != null) {
                updateDb(dto, team);
            } else {
                toDelete.add(team);
            }
        }

        for(TeamDto dto: toAdd.values()) {
            final Team team = new Team();
            updateDb(dto, team);
            team.setGame(game);
            game.getTeams().add(team);
        }

        for(Team team : toDelete) {
            deleteTeam(team, game);
        }
    }

    @Transactional
    public void deleteTeam(@Nullable Team team, @Nullable Game game) {
        if(team == null || game == null) {
            return;
        }
        final List<Team> options = new ArrayList<>(game.getTeams());
        options.remove(team);
        if(options.isEmpty()) {
            return;
        }

        for(Player player : new ArrayList<>(team.getPlayers())) {
            final Team smallest = options.stream()
                    .min(Comparator.comparing(t -> t.getPlayers().size()))
                    .orElseThrow();
            player.setTeam(smallest);
            smallest.getPlayers().add(player);
        }

        game.getTeams().remove(team);
        teamRepository.delete(team);
    }

    @Nullable
    @Transactional
    public Team findOrCreate(@Nullable Game game, @Nullable Color color) {
        if (game == null || color == null) {
            return null;
        }
        final var existing = game.getTeams().stream()
                .filter(t -> t.getColor().equals(color))
                .findAny();
        if (existing.isPresent()) {
            return existing.get();
        }
        final var team = new Team();
        team.setColor(color);
        team.setGame(game);
        game.getTeams().add(team);
        teamRepository.save(team);
        return team;
    }

    @Nonnull
    protected List<Emoji> getPlayers(@Nullable List<Player> players) {
        if (players == null || players.isEmpty()) {
            return Collections.emptyList();
        }
        return players.stream()
                .map(Player::getEmoji)
                .toList();
    }
}
