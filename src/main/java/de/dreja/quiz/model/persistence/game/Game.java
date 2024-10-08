package de.dreja.quiz.model.persistence.game;

import de.dreja.quiz.model.game.IsGameMode;
import de.dreja.quiz.model.persistence.LocalizedEntity;
import de.dreja.quiz.model.persistence.quiz.Quiz;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Stream;

@Entity
@Table(name = "game")
public class Game extends LocalizedEntity {

    @Transient
    private GameId gameId;

    @OneToMany(targetEntity = Player.class, mappedBy = "game")
    private final List<Player> players = new ArrayList<>();

    @OneToMany(targetEntity = Team.class, mappedBy = "game")
    private final List<Team> teams = new ArrayList<>();

    @OneToMany(targetEntity = GameSection.class, mappedBy = "game")
    private final List<GameSection> sections = new ArrayList<>();

    @OneToMany(targetEntity = GameSetting.class, mappedBy = "game")
    private final List<GameSetting> settings = new ArrayList<>();

    @Transient
    private Map<String,String> settingsMap;

    @ManyToOne(targetEntity = Quiz.class, optional = false)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Column(name = "game_mode", nullable = false)
    private String gameModeType = IsGameMode.class.getCanonicalName();

    @Transient
    private Class<? extends IsGameMode> gameMode = IsGameMode.class;

    @OneToOne(targetEntity = Team.class)
    @JoinColumn(name = "active_team_id")
    private Team activeTeam;

    @OneToOne(targetEntity = Player.class)
    @JoinColumn(name = "active_player_id")
    private Player activePlayer;

    @OneToOne(targetEntity = GameQuestion.class)
    @JoinColumn(name = "current_question_id")
    private GameQuestion currentQuestion;

    @Nonnull
    public GameId getGameId() {
        if (gameId == null) {
            gameId = GameId.of(getId());
        }
        return gameId;
    }

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
        if (gameMode == null) {
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

    @Nonnull
    public Stream<Team> getOrderedTeams() {
        return teams.stream().sorted();
    }

    @Nonnull
    public List<GameSetting> getSettings() {
        return settings;
    }

    @Nonnull
    public Game addSetting(@Nonnull GameSetting setting) {
        setting.setGame(this);
        if(settings.contains(setting)) {
            return this;
        }
        settingsMap = null;
        settings.add(setting);
        return this;
    }

    @Nonnull
    public Game removeSetting(@Nonnull GameSetting setting) {
        if(settings.remove(setting)) {
            settingsMap = null;
            setting.setGame(null);
        }
        return this;
    }

    @Nonnull
    public Map<String, String> getSettingsMap() {
        if(settingsMap == null) {
            settingsMap = new TreeMap<>();
            for(GameSetting setting : settings) {
                settingsMap.put(setting.getKey(), setting.getValue());
            }
        }
        return Collections.unmodifiableMap(settingsMap);
    }
}
