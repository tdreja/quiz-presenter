package de.dreja.quiz.model.persistence.quiz;

import static java.util.Locale.ENGLISH;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.dreja.quiz.service.persistence.quiz.QuizRepository;
import jakarta.transaction.Transactional;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class QuizPersistenceTest {

    private Long quizId;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizDevSetup dataProvider;

    @Test
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void testDatabase() {
        assertThat(quizId).isNotNull().isNotNegative();

        final Quiz quiz = quizRepository.findById(quizId).orElse(null);
        assertThat(quiz)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("locale", ENGLISH)
                .hasFieldOrPropertyWithValue("name", "Quiz")
                .hasFieldOrPropertyWithValue("author", "Author");

        assertThat(quiz.getSections())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        final Section category = quiz.getSections().getFirst();
        assertThat(category)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("locale", ENGLISH)
                .hasFieldOrPropertyWithValue("name", "Quiz");

        assertThat(category.getQuestions())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        final Question question = category.getQuestions().getFirst();
        assertThat(question)
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    @BeforeEach
    void storeInDb() {
        final TextAnswer answer = dataProvider.textAnswer("Answer");
        final MultipleChoiceQuestion question = dataProvider.multipleChoiceQuestion("Question", answer);
        quizId = dataProvider.quiz("Quiz", question).getId();
    }
}
