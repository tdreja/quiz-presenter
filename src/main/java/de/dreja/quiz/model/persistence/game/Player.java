package de.dreja.quiz.model.persistence.game;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Emoji emoji;

    @Column(nullable = false)
    private long points;

    @ManyToOne(targetEntity = Team.class, optional = false)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(targetEntity = Game.class, optional = false)
    @JoinColumn(name = "game_id")
    private Game game;

    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }

    @Nonnull
    public Emoji getEmoji() {
        return emoji;
    }

    @Nonnull
    public Player setEmoji(@Nonnull Emoji emoji) {
        this.emoji = emoji;
        return this;
    }

    public long getPoints() {
        return points;
    }

    @Nonnull
    public Player setPoints(long points) {
        this.points = points;
        return this;
    }

    @Nonnull
    public Team getTeam() {
        return team;
    }

    protected void setTeam(Team team) {
        this.team = team;
    }

    @Nonnull
    public Game getGame() {
        return game;
    }

    protected void setGame(Game game) {
        this.game = game;
    }
}
