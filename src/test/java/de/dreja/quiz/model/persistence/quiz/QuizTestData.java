package de.dreja.quiz.model.persistence.quiz;

import static java.util.Locale.ENGLISH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.dreja.quiz.service.persistence.quiz.AnswerRepository;
import de.dreja.quiz.service.persistence.quiz.QuestionGroupRepository;
import de.dreja.quiz.service.persistence.quiz.QuestionRepository;
import de.dreja.quiz.service.persistence.quiz.QuizRepository;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class QuizTestData {

    @Autowired
    private final AnswerRepository answerRepository;

    @Autowired
    private final QuestionRepository questionRepository;

    @Autowired
    private final QuestionGroupRepository categoryRepository;

    @Autowired
    private final QuizRepository quizRepository;

    @Autowired
    private final EntityManager entityManager;

    @Autowired
    QuizTestData(AnswerRepository answerRepository,
                 QuestionRepository questionRepository,
                 QuestionGroupRepository categoryRepository,
                 QuizRepository quizRepository, EntityManager entityManager) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
        this.quizRepository = quizRepository;
        this.entityManager = entityManager;
    }

    @Nonnull
    public TextAnswer textAnswer(@Nonnull String text) {
        final TextAnswer answer = new TextAnswer()
                .setAnswerText(text)
                .setCorrect(true)
                .setLocale(ENGLISH);
        answerRepository.save(answer);
        return answer;
    }

    @Nonnull
    public MultipleChoiceQuestion multipleChoiceQuestion(@Nonnull String questionText,
                                                         @Nonnull TextAnswer singleAnswer) {
        final TextAnswer actual = entityManager.merge(singleAnswer);
        final MultipleChoiceQuestion question = new MultipleChoiceQuestion()
                .addAnswerOption(actual)
                .setCorrectAnswer(actual)
                .setQuestionText(questionText)
                .setDifficulty(5)
                .setLocale(ENGLISH);
        questionRepository.save(question);
        return question;
    }

    @Nonnull
    public Quiz quiz(@Nonnull String name,
                     @Nonnull Question question) {
        final Question actual = entityManager.merge(question);
        final Section category = new Section()
                .setName(name)
                .setLocale(ENGLISH)
                .addQuestion(actual);

        final Quiz quiz = new Quiz()
                .setName(name)
                .setAuthor("Author")
                .setLocale(ENGLISH)
                .addSection(category);
        quizRepository.save(quiz);
        categoryRepository.save(category);

        return quiz;
    }

    @Nonnull
    public Quiz quiz(@Nonnull String name) {
        final TextAnswer answer = textAnswer("Answer-" + name);
        final Question question = multipleChoiceQuestion("Question-" + name, answer);
        return quiz(name, question);
    }
}
