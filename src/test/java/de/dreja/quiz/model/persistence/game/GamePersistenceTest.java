package de.dreja.quiz.model.persistence.game;

import de.dreja.quiz.model.game.ModeGrosserPreis;
import de.dreja.quiz.model.persistence.quiz.Quiz;
import de.dreja.quiz.model.persistence.quiz.QuizDevSetup;
import de.dreja.quiz.service.persistence.game.GameRepository;
import de.dreja.quiz.service.persistence.game.GameSetupService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class GamePersistenceTest {

    @Autowired
    private QuizDevSetup quizTestData;

    @Autowired
    private GameSetupService gameSetupService;

    @Autowired
    private ModeGrosserPreis grosserPreis;

    @Autowired
    private GameRepository gameRepository;

    private Long gameId;

    @Test
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void testDatabase() {
        assertThat(gameId).isNotNull().isNotNegative();
        final Game game = gameRepository.findById(gameId).orElse(null);
        assertThat(game).isNotNull().hasNoNullFieldsOrPropertiesExcept("activeTeam", "activePlayer", "currentQuestion");
        assertThat(game.getSections()).isNotNull().hasSize(1);
        assertThat(game.getGameMode()).isNotNull().isEqualTo(ModeGrosserPreis.class);

        final GameSection category = game.getSections().getFirst();
        assertThat(category).isNotNull().hasNoNullFieldsOrProperties();
        assertThat(category.getQuestions()).isNotNull().hasSize(1);

        final GameQuestion question = category.getQuestions().getFirst();
        assertThat(question).isNotNull().hasNoNullFieldsOrPropertiesExcept("answeredBy");
    }

    @BeforeEach
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void setupGame() {
        final Quiz quiz = quizTestData.quiz("Quiz");
        final Game game = grosserPreis.prepareGame(quiz);
        gameRepository.save(game);
        gameId = game.getId();
    }
}
