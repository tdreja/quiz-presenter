package de.dreja.quiz.model.persistence.game;

import de.dreja.quiz.model.game.ModeGrosserPreis;
import de.dreja.quiz.model.persistence.quiz.Quiz;
import de.dreja.quiz.model.persistence.quiz.QuizDevSetup;
import de.dreja.quiz.service.persistence.game.GameSetupService;
import de.dreja.quiz.service.persistence.game.TeamRepository;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class TeamPersistenceTest {

    @Autowired
    private QuizDevSetup quizTestData;

    @Autowired
    private GameSetupService gameSetupService;

    @Autowired
    private ModeGrosserPreis grosserPreis;

    @Autowired
    private TeamRepository teamRepository;

    private Long teamId;

    @Test
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void testDatabase() {
        assertThat(teamId).isNotNull().isNotNegative();
        final Team team = teamRepository.findById(teamId).orElse(null);
        assertThat(team).isNotNull().hasNoNullFieldsOrProperties();

        assertThat(team.getGame()).isNotNull().hasNoNullFieldsOrPropertiesExcept("activePlayer", "currentQuestion");
        assertThat(team.getGame().getTeams()).isNotNull().hasSize(1).containsExactly(team);
        assertThat(team.getGame().getActiveTeam()).isNotNull().isEqualTo(team);

        final GameSection gameCategory = assertFirst(team.getGame().getSections());
        final GameQuestion gameQuestion = assertFirst(gameCategory.getQuestions());
        assertThat(gameQuestion).isNotNull().hasNoNullFieldsOrProperties();
    }

    @BeforeEach
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void setupGame() {
        final Quiz quiz = quizTestData.quiz("Quiz");
        final Game game = grosserPreis.prepareGame(quiz);

        final GameSection gameCategory = assertFirst(game.getSections());
        final GameQuestion gameQuestion = assertFirst(gameCategory.getQuestions());
        assertThat(gameQuestion).isNotNull().hasNoNullFieldsOrPropertiesExcept("answeredBy");

        final Team team = gameSetupService.addTeam(game, Color.RED);
        gameQuestion.setAnsweredBy(team);
        game.setActiveTeam(team);
        teamId = team.getId();
    }

    @Nonnull
    static <ITEM> ITEM assertFirst(@Nullable List<ITEM> items) {
        assertThat(items).isNotNull().isNotEmpty().hasSizeGreaterThanOrEqualTo(1);
        final ITEM first = items.getFirst();
        assertThat(first).isNotNull();
        return first;
    }
}
