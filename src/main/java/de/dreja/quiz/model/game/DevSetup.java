package de.dreja.quiz.model.game;

import de.dreja.quiz.model.persistence.game.Color;
import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.quiz.QuizDevSetup;
import de.dreja.quiz.service.persistence.game.GameRepository;
import de.dreja.quiz.service.persistence.game.GameSetupService;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.List;

@Profile("dev")
@Component
public class DevSetup implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DevSetup.class);

    private final QuizDevSetup quizSetup;
    private final GameRepository gameRepository;
    private final GameSetupService gameSetupService;

    public DevSetup(QuizDevSetup quizSetup, GameRepository gameRepository, GameSetupService gameSetupService) {
        this.quizSetup = quizSetup;
        this.gameRepository = gameRepository;
        this.gameSetupService = gameSetupService;
    }

    @Override
    @EventListener(ContextRefreshedEvent.class)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void onApplicationEvent(@Nonnull ContextRefreshedEvent event) {
        LOG.info("Startup in DEV Mode!");
        final List<Game> games = gameRepository.findAll();
        if(!games.isEmpty()) {
            LOG.info("We have already {} games in database", games.size());
            return;
        }

        LOG.info("No games in database. Setup new one!");
        final var answer = quizSetup.textAnswer("First answer");
        final var question = quizSetup.multipleChoiceQuestion("Question?", answer);
        final var quiz = quizSetup.quiz("Quiz Name", question);
        final var game = gameSetupService.newGame(quiz);
        gameSetupService.addTeam(game, Color.BLUE);
        gameSetupService.addTeam(game, Color.RED);
        LOG.info("Game {} was created", game.getGameId());
    }
}
