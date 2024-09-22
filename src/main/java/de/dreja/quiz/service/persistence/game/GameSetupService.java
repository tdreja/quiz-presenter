package de.dreja.quiz.service.persistence.game;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.dreja.quiz.model.persistence.game.Color;
import de.dreja.quiz.model.persistence.game.Emoji;
import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.GameQuestion;
import de.dreja.quiz.model.persistence.game.GameSection;
import de.dreja.quiz.model.persistence.game.Player;
import de.dreja.quiz.model.persistence.game.Team;
import de.dreja.quiz.model.persistence.quiz.Question;
import de.dreja.quiz.model.persistence.quiz.Quiz;
import de.dreja.quiz.model.persistence.quiz.Section;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class GameSetupService {

    private static final Comparator<Team> SORT_BY_MEMBER_COUNT = Comparator.comparing(team -> team.getPlayers().size());

    private static final int START = 'A';
    private static final int END = 'Z' + 1;

    private final GameRepository gameRepository;

    private final GameQuestionGroupRepository gameCategoryRepository;

    private final GameQuestionRepository gameQuestionRepository;

    private final TeamRepository teamRepository;

    private final PlayerRepository playerRepository;

    private final EntityManager entityManager;

    private final Random random;

    @Autowired
    GameSetupService(GameRepository gameRepository,
                     GameQuestionGroupRepository gameCategoryRepository,
                     GameQuestionRepository gameQuestionRepository,
                     TeamRepository teamRepository,
                     PlayerRepository playerRepository,
                     EntityManager entityManager) {
        this.gameRepository = gameRepository;
        this.gameCategoryRepository = gameCategoryRepository;
        this.gameQuestionRepository = gameQuestionRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.entityManager = entityManager;
        this.random = new Random();
    }
    
    @Nonnull
    @Transactional
    public Game newGame(@Nonnull Quiz quiz) {
        final Game game = new Game().setGameCode(findNextGameCode()).setQuiz(entityManager.merge(quiz)).setLocale(quiz.getLocale());
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

        final Team team = new Team().setColor(teamColor).setPoints(0L);
        mGame.addTeam(team);
        teamRepository.save(team);
        gameRepository.save(mGame);
        return team;
    }

    @Transactional
    public String findNextGameCode() {
        final Set<String> codes = new HashSet<>(gameRepository.findAllGameCodes());
        String code = generateRandomCode();
        while (codes.contains(code)) {
            code = generateRandomCode();
        }
        return code;
    }

    @Nonnull
    protected String generateRandomCode() {
        final StringBuilder builder = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            builder.append((char) random.nextInt(START, END));
        }
        return builder.toString();
    }
}
