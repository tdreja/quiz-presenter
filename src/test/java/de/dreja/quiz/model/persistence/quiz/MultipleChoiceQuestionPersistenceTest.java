package de.dreja.quiz.model.persistence.quiz;

import static java.util.Locale.ENGLISH;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.dreja.quiz.service.persistence.quiz.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MultipleChoiceQuestionPersistenceTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizDevSetup dataProvider;

    private Long questionId;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Test
    void testDatabase() {
        assertThat(questionId).isNotNull().isNotNegative();
        final Question question = questionRepository.findById(questionId).orElse(null);
        assertThat(question).isNotNull().isInstanceOf(MultipleChoiceQuestion.class)
                .hasFieldOrPropertyWithValue("locale", ENGLISH)
                .hasFieldOrPropertyWithValue("questionText", "Frage")
                .hasFieldOrPropertyWithValue("difficulty", 5);

        final MultipleChoiceQuestion mcQuestion = (MultipleChoiceQuestion) question;
        assertThat(mcQuestion.getCorrectAnswer()).isNotNull().isInstanceOf(TextAnswer.class);

        final TextAnswer correct = (TextAnswer) mcQuestion.getCorrectAnswer();
        assertThat(correct).isNotNull()
                .hasFieldOrPropertyWithValue("correct", true)
                .hasFieldOrPropertyWithValue("locale", ENGLISH)
                .hasFieldOrPropertyWithValue("answerText", "Hallo")
                .hasFieldOrPropertyWithValue("multipleChoiceQuestion", mcQuestion);

        assertThat(mcQuestion.getAnswerOptions()).isNotNull().isNotEmpty().hasSize(1)
                .containsExactly((TextAnswer) mcQuestion.getCorrectAnswer());
    }

    @BeforeEach
    void storeInDatabase() {
        final TextAnswer answer = dataProvider.textAnswer("Hallo");
        questionId = dataProvider.multipleChoiceQuestion("Frage", answer).getId();
    }
}
