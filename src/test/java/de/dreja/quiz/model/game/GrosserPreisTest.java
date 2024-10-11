package de.dreja.quiz.model.game;

import de.dreja.quiz.model.persistence.game.*;
import de.dreja.quiz.model.persistence.quiz.Question;
import de.dreja.quiz.model.persistence.quiz.Quiz;
import de.dreja.quiz.model.persistence.quiz.QuizDevSetup;
import de.dreja.quiz.service.persistence.game.GameRepository;
import de.dreja.quiz.service.persistence.game.GameSetupService;
import de.dreja.quiz.service.persistence.game.TeamRepository;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class GrosserPreisTest {

    @Autowired
    private QuizDevSetup quizTestData;

    @Autowired
    private GameSetupService gameSetupService;

    @Autowired
    private ModeGrosserPreis grosserPreis;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Long gameId;

    @Test
    @Transactional
    void testGame() {
        assertThat(gameId).isNotNull();
        final Game game = gameRepository.findById(gameId).orElse(null);
        assertThat(game).isNotNull();
        assertThat(game.getSections()).isNotNull().isNotEmpty().hasSize(1);
        final GameSection section = game.getSections().getFirst();
        assertThat(section).isNotNull();
        assertThat(section.getQuestions()).isNotNull().isNotEmpty().hasSize(2);

        final GameQuestion firstQuestion = game.getSections().getFirst().getQuestions().getFirst();
        assertThat(firstQuestion).isNotNull();

        final GameQuestion secondQuestion = game.getSections().getFirst().getQuestions().get(1);
        assertThat(secondQuestion).isNotNull();

        assertThat(game.getOrderedTeams()).isNotNull().isNotEmpty().hasSize(2);
        final Team firstTeam = game.getOrderedTeams().findFirst().orElse(null);
        assertThat(firstTeam).isNotNull();

        final Team secondTeam = game.getOrderedTeams().skip(1).findFirst().orElse(null);
        assertThat(secondTeam).isNotNull();
    }

    @BeforeEach
    @Transactional
    void setupGame() {
        final Quiz quiz = quizTestData.quiz("Quiz", buildQuestion(1), buildQuestion(2));
        final Game game = grosserPreis.prepareGame(quiz);
        gameRepository.save(game);

        teamRepository.save(gameSetupService.addTeam(game, Color.RED));
        teamRepository.save(gameSetupService.addTeam(game, Color.BLUE));

        gameId = game.getId();
    }

    @Nonnull
    private Question buildQuestion(int number) {
        return quizTestData.multipleChoiceQuestion("Question #"+number,
                quizTestData.textAnswer("Alpha"),
                quizTestData.textAnswer("Beta", false),
                quizTestData.textAnswer("Gamma", false),
                quizTestData.textAnswer("Delta", false));
    }
}
