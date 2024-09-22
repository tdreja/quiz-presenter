package de.dreja.quiz.model.persistence.game;

import de.dreja.quiz.model.persistence.quiz.Quiz;
import de.dreja.quiz.model.persistence.quiz.QuizTestData;
import de.dreja.quiz.service.persistence.game.GameSetupService;
import de.dreja.quiz.service.persistence.game.PlayerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static de.dreja.quiz.model.persistence.game.TeamPersistenceTest.assertFirst;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PlayerPersistenceTest {

    @Autowired
    private QuizTestData quizTestData;

    @Autowired
    private GameSetupService gameSetupService;

    @Autowired
    private PlayerRepository playerRepository;

    private Long playerId;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Test
    void testPlayer() {
        assertThat(playerId).isNotNull().isNotNegative();
        final Player player = playerRepository.findById(playerId).orElse(null);
        assertThat(player).isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("emoji", Emoji.BEAVER);

        final Game game = player.getGame();
        assertThat(game.getPlayers()).isNotNull().isNotEmpty().hasSize(1).containsExactly(player);

        final Team team = assertFirst(game.getTeams());
        assertThat(player.getTeam()).isEqualTo(team);
        assertThat(team.getPlayers()).isNotNull().isNotEmpty().hasSize(1).containsExactly(player);
    }

    @BeforeEach
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void setupGame() {
        final Quiz quiz = quizTestData.quiz("Quiz");
        final Game game = gameSetupService.setupNewGame(quiz);

        final Player player = gameSetupService.addPlayer(game, Emoji.BEAVER);
        playerId = player.getId();
    }
}
