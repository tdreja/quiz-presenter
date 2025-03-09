package de.dreja.quiz.model.persistence.game;

import de.dreja.quiz.model.common.Emoji;
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

    @ManyToOne(targetEntity = Team.class, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(targetEntity = Game.class, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(nullable = false)
    private String name;

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

    public void setTeam(@Nonnull Team team) {
        this.team = team;
    }

    @Nonnull
    public Game getGame() {
        return game;
    }

    public void setGame(@Nonnull Game game) {
        this.game = game;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }
}
