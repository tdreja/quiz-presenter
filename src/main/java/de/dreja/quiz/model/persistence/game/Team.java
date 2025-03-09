package de.dreja.quiz.model.persistence.game;

import de.dreja.quiz.model.common.Color;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Color color;

    @OneToMany(targetEntity = Player.class, mappedBy = "team")
    private final List<Player> players = new ArrayList<>();

    @ManyToOne(targetEntity = Game.class, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(nullable = false)
    private long points;

    @Column(nullable = false, name = "next_turn_number")
    private long nextTurnNumber;

    @Column(name = "gamepad_id")
    private String gamepadId;

    @Column(name = "gamepad_requested", nullable = false)
    private boolean gamepadRequested;

    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }

    public long getPoints() {
        return points;
    }

    @Nonnull
    public Team setPoints(long points) {
        this.points = points;
        return this;
    }

    @Nonnull
    public Color getColor() {
        return color;
    }

    @Nonnull
    public Team setColor(@Nonnull Color color) {
        this.color = color;
        return this;
    }

    @Nonnull
    public List<Player> getPlayers() {
        return players;
    }

    @Nonnull
    public Game getGame() {
        return game;
    }

    public void setGame(@Nonnull Game game) {
        this.game = game;
    }

    public long getNextTurnNumber() {
        return nextTurnNumber;
    }

    public void setNextTurnNumber(long nextTurnNumber) {
        this.nextTurnNumber = nextTurnNumber;
    }

    @Nullable
    public String getGamepadId() {
        return gamepadId;
    }

    public void setGamepadId(@Nullable String gamepadId) {
        this.gamepadId = gamepadId;
    }

    public boolean isGamepadRequested() {
        return gamepadRequested;
    }

    public void setGamepadRequested(boolean gamepadRequested) {
        this.gamepadRequested = gamepadRequested;
    }
}
