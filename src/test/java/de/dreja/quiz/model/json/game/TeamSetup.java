package de.dreja.quiz.model.json.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Color;
import de.dreja.quiz.model.common.Emoji;
import jakarta.annotation.Nonnull;

import java.util.*;

public record TeamSetup(@Nonnull
                        @JsonIgnore
                        Map<Color, TeamDto> teams,
                        @Nonnull
                        @JsonIgnore
                        Map<Emoji, PlayerDto> players) {

    @Nonnull
    @JsonCreator
    public static TeamSetup fromJson(@Nonnull @JsonProperty("teams") List<TeamDto> teams,
                                     @Nonnull @JsonProperty("players") List<PlayerDto> players) {
        final Map<Color, TeamDto> teamsMap = new EnumMap<>(Color.class);
        for (TeamDto team : teams) {
            teamsMap.put(team.color(), team);
        }

        final Map<Emoji, PlayerDto> playerMap = new EnumMap<>(Emoji.class);
        for (PlayerDto player : players) {
            playerMap.put(player.emoji(), player);
        }

        return new TeamSetup(Collections.unmodifiableMap(teamsMap), Collections.unmodifiableMap(playerMap));
    }

    @Nonnull
    @JsonProperty("teams")
    public List<TeamDto> getTeamsList() {
        return new ArrayList<>(teams.values());
    }

    @Nonnull
    @JsonProperty("players")
    public List<PlayerDto> getPlayersList() {
        return new ArrayList<>(players.values());
    }
}
