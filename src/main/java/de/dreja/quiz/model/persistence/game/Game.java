package de.dreja.quiz.model.persistence.game;

import java.util.ArrayList;
import java.util.List;

import de.dreja.quiz.model.persistence.LocalizedEntity;
import de.dreja.quiz.model.persistence.quiz.Quiz;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "game")
public class Game extends LocalizedEntity {

    @Column(name = "game_code", unique = true)
    private String gameCode;

    @OneToMany(targetEntity = Player.class, mappedBy = "game")
    private final List<Player> players = new ArrayList<>();

    @OneToMany(targetEntity = Team.class, mappedBy = "game")
    private final List<Team> teams = new ArrayList<>();

    @OneToMany(targetEntity = GameSection.class, mappedBy = "game")
    private final List<GameSection> sections = new ArrayList<>();

    @ManyToOne(targetEntity = Quiz.class, optional = false)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Nonnull
    public List<Player> getPlayers() {
        return players;
    }

    @Nonnull
    public Game addPlayer(@Nonnull Player player) {
        player.setGame(this);
        if (players.contains(player)) {
            return this;
        }
        players.add(player);
        return this;
    }

    @Nonnull
    public Game removePlayer(@Nonnull Player player) {
        if (players.remove(player)) {
            player.setGame(null);
        }
        return this;
    }

    @Nonnull
    public List<Team> getTeams() {
        return teams;
    }

    @Nonnull
    public Game addTeam(@Nonnull Team team) {
        team.setGame(this);
        if (teams.contains(team)) {
            return this;
        }
        teams.add(team);
        return this;
    }

    @Nonnull
    public Game removeTeam(@Nonnull Team team) {
        if (teams.remove(team)) {
            team.setGame(null);
        }
        return this;
    }

    @Nonnull
    public List<GameSection> getSections() {
        return sections;
    }

    @Nonnull
    public Game addSection(@Nonnull GameSection section) {
        section.setGame(this);
        if (sections.contains(section)) {
            return this;
        }
        sections.add(section);
        return this;
    }

    @Nonnull
    public Game removeSection(@Nonnull GameSection section) {
        if (sections.remove(section)) {
            section.setGame(null);
        }
        return this;
    }

    @Nonnull
    public String getGameCode() {
        return gameCode;
    }

    @Nonnull
    public Game setGameCode(@Nonnull String gameCode) {
        this.gameCode = gameCode;
        return this;
    }

    @Nonnull
    public Quiz getQuiz() {
        return quiz;
    }

    @Nonnull
    public Game setQuiz(@Nonnull Quiz quiz) {
        this.quiz = quiz;
        return this;
    }
}
