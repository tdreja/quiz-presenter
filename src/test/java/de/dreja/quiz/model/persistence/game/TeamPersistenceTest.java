package de.dreja.quiz.model.persistence.game;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.dreja.quiz.model.persistence.quiz.Quiz;
import de.dreja.quiz.model.persistence.quiz.QuizTestData;
import de.dreja.quiz.service.persistence.game.GameSetupService;
import de.dreja.quiz.service.persistence.game.TeamRepository;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;

@SpringBootTest
public class TeamPersistenceTest {

    @Autowired
    private QuizTestData quizTestData;

    @Autowired
    private GameSetupService gameSetupService;

    @Autowired
    private TeamRepository teamRepository;

    private Long teamId;

    @Test
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void testDatabase() {
        assertThat(teamId).isNotNull().isNotNegative();
        final Team team = teamRepository.findById(teamId).orElse(null);
        assertThat(team).isNotNull().hasNoNullFieldsOrProperties();

        assertThat(team.getGame()).isNotNull().hasNoNullFieldsOrProperties();
        assertThat(team.getGame().getTeams()).isNotNull().hasSize(1).containsExactly(team);

        final GameCategory gameCategory = assertFirst(team.getGame().getCategories());
        final GameQuestion gameQuestion = assertFirst(gameCategory.getQuestions());
        assertThat(gameQuestion).isNotNull().hasNoNullFieldsOrProperties();
    }

    @BeforeEach
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void setupGame() {
        final Quiz quiz = quizTestData.quiz("Quiz");
        final Game game = gameSetupService.setupNewGame(quiz);

        final GameCategory gameCategory = assertFirst(game.getCategories());
        final GameQuestion gameQuestion = assertFirst(gameCategory.getQuestions());
        assertThat(gameQuestion).isNotNull().hasNoNullFieldsOrPropertiesExcept("answeredBy");

        final Team team = gameSetupService.addTeam(game, Color.RED);
        gameQuestion.setAnsweredBy(team);
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
