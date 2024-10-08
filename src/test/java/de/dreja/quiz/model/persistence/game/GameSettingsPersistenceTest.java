package de.dreja.quiz.model.persistence.game;

import de.dreja.quiz.model.game.ModeGrosserPreis;
import de.dreja.quiz.model.persistence.quiz.Quiz;
import de.dreja.quiz.model.persistence.quiz.QuizTestData;
import de.dreja.quiz.service.persistence.game.GameRepository;
import de.dreja.quiz.service.persistence.game.GameSetupService;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GameSettingsPersistenceTest {
    @Autowired
    private QuizTestData quizTestData;

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
        assertThat(game.getSettings()).isNotNull().hasSize(2);
        assertThat(game.getSettingsMap()).isNotNull().hasSize(2)
                .containsAllEntriesOf(Map.of("First","1st", "Second","2nd"));

        gameSetupService.updateSettings(game, Map.of("First", "New"));
        assertThat(game.getSettings()).isNotNull().hasSize(1);
        assertThat(game.getSettingsMap()).isNotNull().hasSize(1).contains(Pair.of("First","New"));
    }

    @BeforeEach
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void setupGame() {
        final Quiz quiz = quizTestData.quiz("Quiz");
        final Game game = grosserPreis.prepareGame(quiz);
        gameRepository.save(game);
        gameSetupService.updateSettings(game, Map.of("First","1st", "Second","2nd"));
        gameId = game.getId();
    }
}
