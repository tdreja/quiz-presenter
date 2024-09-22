package de.dreja.quiz.model.persistence.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.dreja.quiz.model.game.IsGameMode;
import de.dreja.quiz.model.persistence.LocalizedEntity;
import de.dreja.quiz.model.persistence.quiz.Quiz;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

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

    @Column(name="game_mode", nullable=false)
    private String gameModeType = IsGameMode.class.getCanonicalName();

    @Transient
    private Class<? extends IsGameMode> gameMode = IsGameMode.class;

    @OneToOne(targetEntity=Team.class)
    @JoinColumn(name="active_team_id")
    private Team activeTeam;

    @OneToOne(targetEntity=Player.class)
    @JoinColumn(name="active_player_id")
    private Player activePlayer;

    @OneToOne(targetEntity=GameQuestion.class)
    @JoinColumn(name="current_question_id")
    private GameQuestion currentQuestion;

    @Override
    @Nonnull
    public Game setLocale(@Nonnull Locale locale) {
        super.setLocale(locale);
        return this;
    }

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

    @SuppressWarnings("unchecked")
    @Nonnull
    public Class<? extends IsGameMode> getGameMode() {
        if(gameMode == null) {
            try {
                gameMode = (Class<? extends IsGameMode>) Class.forName(gameModeType);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Could not load game mode type!", e);
            }
        }
        return gameMode;
    }

    @Nonnull
    public Game setGameMode(@Nonnull Class<? extends IsGameMode> gameMode) {
        this.gameMode = gameMode;
        this.gameModeType = this.gameMode.getCanonicalName();
        return this;
    }

    @Nullable
    public Team getActiveTeam() {
        return activeTeam;
    }

    @Nonnull
    public Game setActiveTeam(@Nullable Team team) {
        this.activeTeam = team;
        return this;
    }

    @Nullable
    public Player getActivePlayer() {
        return activePlayer;
    }

    @Nonnull
    public Game setActivePlayer(@Nullable Player player) {
        this.activePlayer = player;
        return this;
    }

    @Nullable
    public GameQuestion getCurrentQuestion() {
        return currentQuestion;
    }

    @Nonnull
    public Game setCurrentQuestion(@Nullable GameQuestion question) {
        this.currentQuestion = question;
        return this;
    }

}
