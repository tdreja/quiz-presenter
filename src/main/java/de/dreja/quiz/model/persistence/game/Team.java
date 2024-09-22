package de.dreja.quiz.model.persistence.game;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

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

    @ManyToOne(targetEntity = Game.class, optional = false)
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(nullable = false)
    private long points;

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
    public Team addPlayer(@Nonnull Player player) {
        player.setTeam(this);
        if (players.contains(player)) {
            return this;
        }
        players.add(player);
        return this;
    }

    @Nonnull
    public Team removePlayer(@Nonnull Player player) {
        if (players.remove(player)) {
            player.setTeam(null);
        }
        return this;
    }

    @Nonnull
    public Game getGame() {
        return game;
    }

    protected void setGame(Game game) {
        this.game = game;
    }
}
