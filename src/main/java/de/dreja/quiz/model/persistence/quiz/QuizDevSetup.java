package de.dreja.quiz.model.persistence.quiz;

import de.dreja.quiz.service.persistence.quiz.AnswerRepository;
import de.dreja.quiz.service.persistence.quiz.QuestionGroupRepository;
import de.dreja.quiz.service.persistence.quiz.QuestionRepository;
import de.dreja.quiz.service.persistence.quiz.QuizRepository;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static java.util.Locale.ENGLISH;

@Service
@Transactional
@Profile({"dev","test"})
public class QuizDevSetup {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final QuestionGroupRepository categoryRepository;
    private final QuizRepository quizRepository;
    private final EntityManager entityManager;

    @Autowired
    QuizDevSetup(AnswerRepository answerRepository,
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
        return textAnswer(text, true);
    }

    @Nonnull
    public TextAnswer textAnswer(@Nonnull String text, boolean correct) {
        final TextAnswer answer = new TextAnswer()
                .setAnswerText(text)
                .setCorrect(correct)
                .setLocale(ENGLISH);
        answerRepository.save(answer);
        return answer;
    }

    @Nonnull
    public MultipleChoiceQuestion multipleChoiceQuestion(@Nonnull String questionText,
                                                         @Nonnull TextAnswer correctAnswer,
                                                         @Nullable TextAnswer... falseOptions) {
        final TextAnswer correct = entityManager.merge(correctAnswer);
        final MultipleChoiceQuestion question = new MultipleChoiceQuestion()
                .addAnswerOption(correct)
                .setCorrectAnswer(correct)
                .setQuestionText(questionText)
                .setDifficulty(5)
                .setLocale(ENGLISH);
        if(falseOptions != null) {
            for(TextAnswer option : falseOptions) {
                question.addAnswerOption(entityManager.merge(option));
            }
        }
        questionRepository.save(question);
        return question;
    }

    @Nonnull
    public Quiz quiz(@Nonnull String name,
                     @Nonnull Question question,
                     @Nullable Question... furtherQuestions) {
        final Question actual = entityManager.merge(question);
        final Section category = new Section()
                .setName(name)
                .setLocale(ENGLISH)
                .addQuestion(actual);
        if(furtherQuestions != null) {
            for(Question add : furtherQuestions) {
                category.addQuestion(entityManager.merge(add));
            }
        }

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
