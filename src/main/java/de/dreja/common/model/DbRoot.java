package de.dreja.common.model;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import de.dreja.questions.model.Question;
import org.eclipse.serializer.collections.lazy.LazyHashMap;

import de.dreja.game.model.Game;
import de.dreja.quiz.model.Quiz;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class DbRoot {
    
    private final Map<IdBase32, Quiz> quizzes = new LazyHashMap<>();

    private final Map<IdBase32, Game> games = new LazyHashMap<>();

    private final Map<String, Question> questions = new LazyHashMap<>();

    private IdBase32 lastId = null;
    
    @Nonnull
    public Map<IdBase32, Quiz> getQuizzes() {
        return quizzes;
    }

    @Nonnull
    public Optional<Quiz> getQuiz(@Nullable IdBase32 id) {
        return HasId.get(quizzes, id);
    }

    @Nonnull
    public Map<IdBase32, Game> getGames() {
        return games;
    }

    @Nonnull
    public Optional<Game> getGame(@Nullable IdBase32 id) {
        return HasId.get(games, id);
    }

    @Nonnull
    public Map<String, Question> getQuestions() {
        return questions;
    }

    @Nonnull
    public Optional<Question> getQuestion(@Nullable String id) {
        return HasId.get(questions, id);
    }

    @Nonnull
    public IdBase32 nextId() {
        if(lastId == null) {
            lastId = IdBase32.of(Instant.now().getEpochSecond());
        }
        lastId = lastId.next();
        return lastId;
    }

}
