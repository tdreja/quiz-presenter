package de.dreja.quiz.model.json.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.dreja.quiz.model.common.Color;
import de.dreja.quiz.model.common.Emoji;
import de.dreja.quiz.model.common.GameState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.*;

import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;

@Schema(name = "Game", description = "Overall container for the entire game setup and state")
public record GameDto(@Nonnull
                      @JsonProperty(required = true)
                      GameState state,
                      @Nonnull
                      @JsonProperty(required = true)
                      List<GameSectionDto> sections,
                      @Nonnull
                      @JsonIgnore
                      Set<Emoji> availableEmojis,
                      @Nonnull
                      @JsonIgnore
                      Set<Color> availableColors,
                      @Nonnull
                      @JsonIgnore
                      Map<Emoji, PlayerDto> players,
                      @Nonnull
                      @JsonIgnore
                      Map<Color, TeamDto> teams,
                      @JsonProperty(required = true)
                      long roundCounter,
                      @Nullable
                      @JsonProperty
                      String currentSection,
                      @Nullable
                      @JsonProperty
                      String currentRound) {

    @Nonnull
    @JsonCreator
    static GameDto fromJson(@Nonnull
                            @JsonProperty(required = true)
                            GameState state,
                            @Nonnull
                            @JsonProperty(required = true)
                            List<GameSectionDto> sections,
                            @Nonnull
                            @JsonProperty(value = "availableEmojis", required = true)
                            List<Emoji> availableEmojis,
                            @Nonnull
                            @JsonProperty(value = "availableColors", required = true)
                            List<Color> availableColors,
                            @Nonnull
                            @JsonProperty(value = "players", required = true)
                            List<PlayerDto> players,
                            @Nonnull
                            @JsonProperty(value = "teams", required = true)
                            List<TeamDto> teams,
                            @JsonProperty(required = true)
                            long roundCounter,
                            @Nullable
                            @JsonProperty
                            String currentSection,
                            @Nullable
                            @JsonProperty
                            String currentRound) {
        final EnumSet<Emoji> emoji = EnumSet.noneOf(Emoji.class);
        emoji.addAll(availableEmojis);

        final EnumSet<Color> color = EnumSet.noneOf(Color.class);
        color.addAll(availableColors);

        final EnumMap<Emoji, PlayerDto> playerMap = new EnumMap<>(Emoji.class);
        players.forEach(p -> playerMap.put(p.emoji(), p));

        final EnumMap<Color, TeamDto> teamMap = new EnumMap<>(Color.class);
        teams.forEach(t -> teamMap.put(t.color(), t));

        return new GameDto(state, sections,
                unmodifiableSet(emoji), unmodifiableSet(color),
                unmodifiableMap(playerMap), unmodifiableMap(teamMap),
                roundCounter, currentSection, currentRound);
    }

    @Nonnull
    @JsonProperty(value = "availableEmojis", required = true)
    List<Emoji> getAvailableEmojiList() {
        return new ArrayList<>(availableEmojis);
    }

    @Nonnull
    @JsonProperty(value = "availableColors", required = true)
    List<Color> getAvailableColorList() {
        return new ArrayList<>(availableColors);
    }

    @Nonnull
    @JsonProperty(value = "players", required = true)
    List<PlayerDto> getPlayerList() {
        return new ArrayList<>(players.values());
    }

    @Nonnull
    @JsonProperty(value = "teams", required = true)
    List<TeamDto> getTeamList() {
        return new ArrayList<>(teams.values());
    }
}
