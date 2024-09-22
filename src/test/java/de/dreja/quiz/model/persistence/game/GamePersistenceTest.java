package de.dreja.quiz.model.persistence.game;

import de.dreja.quiz.model.persistence.quiz.Quiz;
import de.dreja.quiz.model.persistence.quiz.QuizTestData;
import de.dreja.quiz.service.persistence.game.GameRepository;
import de.dreja.quiz.service.persistence.game.GameSetupService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GamePersistenceTest {

    @Autowired
    private QuizTestData quizTestData;

    @Autowired
    private GameSetupService gameSetupService;

    @Autowired
    private GameRepository gameRepository;

    private Long gameId;

    @Test
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void testDatabase() {
        assertThat(gameId).isNotNull().isNotNegative();
        final Game game = gameRepository.findById(gameId).orElse(null);
        assertThat(game).isNotNull().hasNoNullFieldsOrProperties();
        assertThat(game.getCategories()).isNotNull().hasSize(1);

        final GameCategory category = game.getCategories().getFirst();
        assertThat(category).isNotNull().hasNoNullFieldsOrProperties();
        assertThat(category.getQuestions()).isNotNull().hasSize(1);

        final GameQuestion question = category.getQuestions().getFirst();
        assertThat(question).isNotNull().hasNoNullFieldsOrPropertiesExcept("answeredBy");
    }

    @BeforeEach
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void setupGame() {
        final Quiz quiz = quizTestData.quiz("Quiz");
        final Game game = gameSetupService.setupNewGame(quiz);
        gameRepository.save(game);
        gameId = game.getId();
    }
}
