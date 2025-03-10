package de.dreja.quiz.model.persistence.game;

import de.dreja.quiz.model.common.Color;
import de.dreja.quiz.model.common.Emoji;
import de.dreja.quiz.model.common.GameState;
import de.dreja.quiz.model.persistence.LocalizedEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "game")
public class Game extends LocalizedEntity {

    @OneToMany(targetEntity = Player.class, mappedBy = "game")
    private final List<Player> players = new ArrayList<>();

    @ElementCollection(targetClass = Emoji.class, fetch = FetchType.EAGER)
    @JoinTable(name = "game_available_emojis", joinColumns = @JoinColumn(name = "game_id"))
    @Column(nullable = false, name = "emoji")
    @Enumerated(value = EnumType.STRING)
    private final List<Emoji> availableEmojis = new ArrayList<>();

    @ElementCollection(targetClass = Color.class, fetch = FetchType.EAGER)
    @JoinTable(name = "game_available_colors", joinColumns = @JoinColumn(name = "game_id"))
    @Column(nullable = false, name = "color")
    @Enumerated(value = EnumType.STRING)
    private final List<Color> availableColors = new ArrayList<>();

    @OneToMany(targetEntity = Team.class, mappedBy = "game")
    private final List<Team> teams = new ArrayList<>();

    @Column(name = "round_counter")
    private long roundCounter;

    @Column(name = "state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private GameState state = GameState.TEAM_SETUP;

    @Nonnull
    public List<Player> getPlayers() {
        return players;
    }

    @Nonnull
    public List<Team> getTeams() {
        return teams;
    }

    public List<Color> getAvailableColors() {
        return availableColors;
    }

    public List<Emoji> getAvailableEmojis() {
        return availableEmojis;
    }

    public long getRoundCounter() {
        return roundCounter;
    }

    public void setRoundCounter(long roundCounter) {
        this.roundCounter = roundCounter;
    }

    @Nonnull
    public GameState getState() {
        return state;
    }

    public void setState(@Nonnull GameState state) {
        this.state = state;
    }
}
