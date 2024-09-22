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
import de.dreja.quiz.model.persistence.game.GameSection;
import de.dreja.quiz.model.persistence.game.GameQuestion;
import de.dreja.quiz.model.persistence.game.Player;
import de.dreja.quiz.model.persistence.game.Team;
import de.dreja.quiz.model.persistence.quiz.Question;
import de.dreja.quiz.model.persistence.quiz.Section;
import de.dreja.quiz.model.persistence.quiz.Quiz;
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
    public Game setupNewGame(@Nonnull Quiz quiz) {
        final Game game = new Game();
        game.setGameCode(findNextGameCode());

        final Quiz mQuiz = entityManager.merge(quiz);
        game.setQuiz(mQuiz);
        gameRepository.save(game);

        for (Section category : mQuiz.getSections()) {
            final GameSection gameCategory = new GameSection().setSection(category);
            game.addSection(gameCategory);
            gameCategory.setName(category.getName());
            gameCategoryRepository.save(gameCategory);
            long points = 100;

            for (Question question : category.getQuestions()) {
                final GameQuestion gameQuestion = new GameQuestion().setQuestion(question).setPoints(points);
                gameCategory.addQuestion(gameQuestion);
                points += 100L;
                gameQuestionRepository.save(gameQuestion);
            }
            gameCategoryRepository.save(gameCategory);
        }

        gameRepository.save(game);
        return game;
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
