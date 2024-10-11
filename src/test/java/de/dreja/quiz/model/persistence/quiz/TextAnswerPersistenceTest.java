package de.dreja.quiz.model.persistence.quiz;

import de.dreja.quiz.service.persistence.quiz.AnswerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TextAnswerPersistenceTest {

    @Autowired
    private AnswerRepository repository;

    @Autowired
    private QuizDevSetup dataProvider;

    private Long answerId;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Test
    void testDatabase() {
        assertThat(answerId).isNotNull().isNotNegative();
        final Answer answer = repository.findById(answerId).orElse(null);
        assertThat(answer).isNotNull().isInstanceOf(TextAnswer.class)
                .hasFieldOrPropertyWithValue("answerText", "Hallo")
                .hasFieldOrPropertyWithValue("correct", true)
                .hasFieldOrPropertyWithValue("locale", ENGLISH);
    }

    @BeforeEach
    void storeInDatabase() {
        answerId = dataProvider.textAnswer("Hallo").getId();
    }
}
