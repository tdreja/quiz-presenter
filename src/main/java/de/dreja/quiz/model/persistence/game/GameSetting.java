package de.dreja.quiz.model.persistence.game;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

@Entity
@Table(name = "game_settings")
public class GameSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(name = "setting_key", nullable = false)
    private String key;

    @Column(name = "setting_value", nullable = false)
    private String value;

    @ManyToOne(targetEntity = Game.class)
    @JoinColumn(name = "game_id")
    private Game game;

    public long getId() {
        return id;
    }

    @Nonnull
    public String getKey() {
        return key;
    }

    @Nonnull
    public GameSetting setKey(@Nonnull String key) {
        this.key = key;
        return this;
    }

    @Nonnull
    public String getValue() {
        return value;
    }

    @Nonnull
    public GameSetting setValue(@Nonnull String value) {
        this.value = value;
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
