package de.dreja.quiz.model.game;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import de.dreja.quiz.model.json.game.AnswerFromTeam;
import de.dreja.quiz.model.persistence.game.Color;
import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.GameQuestion;
import de.dreja.quiz.model.persistence.game.GameSection;
import de.dreja.quiz.model.persistence.game.Team;
import de.dreja.quiz.model.persistence.quiz.Question;
import de.dreja.quiz.model.persistence.quiz.Quiz;
import de.dreja.quiz.model.persistence.quiz.QuizDevSetup;
import de.dreja.quiz.model.persistence.quiz.TextAnswer;
import de.dreja.quiz.service.persistence.game.GameRepository;
import de.dreja.quiz.service.persistence.game.GameSetupService;
import de.dreja.quiz.service.persistence.game.TeamRepository;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;

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

    private Game game;
    private GameSection firstSection;
    private GameQuestion firstQuestion;
    private GameQuestion secondQuestion;
    private Team firstTeam;
    private Team secondTeam;

    @Test
    @Transactional
    void testGame() {
        assertThat(gameId).isNotNull();
        game = gameRepository.findById(gameId).orElse(null);
        assertThat(game).isNotNull();
        assertThat(game.getSections()).isNotNull().isNotEmpty().hasSize(1);
        firstSection = game.getSections().getFirst();
        assertThat(firstSection).isNotNull();
        assertThat(firstSection.getQuestions()).isNotNull().isNotEmpty().hasSize(2);

        firstQuestion = firstSection.getQuestions().getFirst();
        assertThat(firstQuestion).isNotNull().hasFieldOrPropertyWithValue("answered", false);

        secondQuestion = firstSection.getQuestions().get(1);
        assertThat(secondQuestion).isNotNull().hasFieldOrPropertyWithValue("answered", false);

        assertThat(game.getOrderedTeams()).isNotNull().isNotEmpty().hasSize(2);
        firstTeam = game.getOrderedTeams().findFirst().orElse(null);
        assertThat(firstTeam).isNotNull();

        secondTeam = game.getOrderedTeams().skip(1).findFirst().orElse(null);
        assertThat(secondTeam).isNotNull();

        runFirstQuestion();
        runSecondQuestion();
    }

    private void runFirstQuestion() {
        grosserPreis.onQuestionSelected(game, firstQuestion);
        assertThat(game.isWaitForTeamInput()).isFalse();
        assertThat(game.getCurrentQuestion()).isEqualTo(firstQuestion);

        game.setWaitForTeamInput(true);
        game.setActiveTeam(firstTeam);

        final TextAnswer textAnswer = firstQuestion.getQuestion().getCorrectAnswer().asTextAnswer();
        assertThat(textAnswer).isNotNull();
        grosserPreis.onAnswersReceived(game, List.of(new AnswerFromTeam(firstTeam.getColor(), Objects.requireNonNull(textAnswer).getAnswerText())));


        assertThat(game.getCurrentQuestion()).as("Current Question").isNull();
        assertThat(game.getActiveTeam()).as("Active Team").isEqualTo(secondTeam);
        assertThat(firstQuestion.getAnsweredBy()).as("Answered By").isEqualTo(firstTeam);
        assertThat(firstQuestion.isAnswered()).isTrue();
        assertThat(game.isWaitForTeamInput()).isFalse();
        assertThat(firstSection.isComplete()).isFalse();
    }

    private void runSecondQuestion() {
        grosserPreis.onQuestionSelected(game, secondQuestion);
        assertThat(game.isWaitForTeamInput()).isFalse();
        assertThat(game.getCurrentQuestion()).isEqualTo(secondQuestion);

        game.setWaitForTeamInput(true);
        game.setActiveTeam(secondTeam);

        grosserPreis.onAnswersReceived(game, List.of());

        assertThat(game.getCurrentQuestion()).as("Current Question").isNull();
        assertThat(game.getActiveTeam()).as("Active Team").isEqualTo(firstTeam);
        assertThat(secondQuestion.getAnsweredBy()).as("Answered By").isNull();
        assertThat(secondQuestion.isAnswered()).isTrue();
        assertThat(game.isWaitForTeamInput()).isFalse();
        assertThat(firstSection.isComplete()).isTrue();
        assertThat(game.getEnd()).isNotNull();
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
