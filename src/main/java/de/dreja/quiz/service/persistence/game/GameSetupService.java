package de.dreja.quiz.service.persistence.game;

import java.util.*;

import de.dreja.quiz.model.persistence.game.*;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.dreja.quiz.model.persistence.quiz.Question;
import de.dreja.quiz.model.persistence.quiz.Quiz;
import de.dreja.quiz.model.persistence.quiz.Section;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class GameSetupService {

    private static final Comparator<Team> SORT_BY_MEMBER_COUNT = Comparator.comparing(team -> team.getPlayers().size());

    private final GameRepository gameRepository;

    private final GameQuestionGroupRepository gameCategoryRepository;

    private final GameQuestionRepository gameQuestionRepository;

    private final TeamRepository teamRepository;

    private final PlayerRepository playerRepository;

    private final GameSettingRepository settingRepository;

    private final EntityManager entityManager;

    private final Random random;

    @Autowired
    GameSetupService(GameRepository gameRepository,
                     GameQuestionGroupRepository gameCategoryRepository,
                     GameQuestionRepository gameQuestionRepository,
                     TeamRepository teamRepository,
                     PlayerRepository playerRepository,
                     GameSettingRepository settingRepository,
                     EntityManager entityManager) {
        this.gameRepository = gameRepository;
        this.gameCategoryRepository = gameCategoryRepository;
        this.gameQuestionRepository = gameQuestionRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.settingRepository = settingRepository;
        this.entityManager = entityManager;
        this.random = new Random();
    }
    
    @Nonnull
    @Transactional
    public Game newGame(@Nonnull Quiz quiz) {
        final Game game = new Game().setQuiz(entityManager.merge(quiz)).setLocale(quiz.getLocale());
        gameRepository.save(game);
        return game;
    }

    @Nonnull
    @Transactional
    public GameSection newSection(@Nonnull Game game, @Nonnull Section section, @Nonnull String name) {
        final GameSection gameSection = new GameSection().setName(name).setSection(entityManager.merge(section));
        entityManager.merge(game).addSection(gameSection);
        gameCategoryRepository.save(gameSection);
        return gameSection;
    }

    @Nonnull
    @Transactional
    public GameQuestion newQuestion(@Nonnull GameSection section, @Nonnull Question question, long points) {
        final GameQuestion gameQuestion = new GameQuestion().setPoints(points).setQuestion(entityManager.merge(question));
        entityManager.merge(section).addQuestion(gameQuestion);
        gameQuestionRepository.save(gameQuestion);
        return gameQuestion;
    }

    

    @Nonnull
    @Transactional
    public Player addPlayer(@Nonnull Game game, @Nonnull Emoji emoji) {
        final Game mGame = entityManager.merge(game);
        for (Player player : mGame.getPlayers()) {
            if (emoji == player.getEmoji()) {
                return player;
            }
        }
        final Team team;
        if (mGame.getTeams().isEmpty()) {
            team = addTeam(mGame, Color.values()[0]);
        } else if (mGame.getTeams().size() == 1) {
            team = addTeam(mGame, Color.values()[1]);
        } else {
            team = mGame.getTeams().stream().min(SORT_BY_MEMBER_COUNT).orElseThrow();
        }
        final Player player = new Player().setEmoji(emoji).setPoints(0L);
        team.addPlayer(player);
        mGame.addPlayer(player);
        playerRepository.save(player);
        teamRepository.save(team);
        gameRepository.save(game);
        return player;
    }

    @Nonnull
    @Transactional
    public Team addTeam(@Nonnull Game game, @Nonnull Color teamColor) {
        final Game mGame = entityManager.merge(game);
        for (Team team : mGame.getTeams()) {
            if (teamColor == team.getColor()) {
                return team;
            }
        }

        final long orderNumber = mGame.getTeams().size() + 1L;
        final Team team = new Team().setColor(teamColor).setPoints(0L).setOrderNumber(orderNumber);
        mGame.addTeam(team);
        teamRepository.save(team);
        gameRepository.save(mGame);
        return team;
    }

    @Transactional
    public void updateSettings(@Nonnull Game game, @Nullable Map<String,String> settings) {
        final Map<String,String> map = settings == null ? new TreeMap<>() : new TreeMap<>(settings);

        final List<GameSetting> oldSettings = new ArrayList<>(game.getSettings());
        for(GameSetting old : oldSettings) {
            if(map.containsKey(old.getKey())) {
                old.setValue(map.remove(old.getKey()));
            } else {
                game.removeSetting(old);
                settingRepository.delete(old);
            }
        }

        for(Map.Entry<String,String> entry : map.entrySet()) {
            final GameSetting setting = new GameSetting().setKey(entry.getKey()).setValue(entry.getValue());
            game.addSetting(setting);
            settingRepository.save(setting);
        }
    }
}
