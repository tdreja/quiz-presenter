package de.dreja.quiz.model.persistence.game;

import de.dreja.quiz.model.game.ModeGrosserPreis;
import de.dreja.quiz.model.persistence.quiz.Quiz;
import de.dreja.quiz.model.persistence.quiz.QuizDevSetup;
import de.dreja.quiz.service.persistence.game.GameSetupService;
import de.dreja.quiz.service.persistence.game.PlayerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static de.dreja.quiz.model.persistence.game.TeamPersistenceTest.assertFirst;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class PlayerPersistenceTest {

    @Autowired
    private QuizDevSetup quizTestData;

    @Autowired
    private GameSetupService gameSetupService;

    @Autowired
    private ModeGrosserPreis grosserPreis;

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
        assertThat(game.getActivePlayer()).isNotNull().isEqualTo(player);

        final Team team = assertFirst(game.getTeams());
        assertThat(player.getTeam()).isEqualTo(team);
        assertThat(team.getPlayers()).isNotNull().isNotEmpty().hasSize(1).containsExactly(player);
    }

    @BeforeEach
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void setupGame() {
        final Quiz quiz = quizTestData.quiz("Quiz");
        final Game game = grosserPreis.prepareGame(quiz);

        final Player player = gameSetupService.addPlayer(game, Emoji.BEAVER);
        game.setActivePlayer(player);
        playerId = player.getId();
    }
}
