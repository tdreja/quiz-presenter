package de.dreja.quiz.model.persistence.game;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

@Entity
@Table(name = "team")
public class Team implements Comparable<Team> {

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

    @Column(nullable = false)
    private long orderNumber;

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
    @JsonIgnore
    public Game getGame() {
        return game;
    }

    protected void setGame(Game game) {
        this.game = game;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    @Nonnull
    public Team setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    @Override
    public int compareTo(@Nonnull Team o) {
        return Long.compare(orderNumber, o.orderNumber);
    }

    
}
