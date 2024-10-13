package de.dreja.common.model;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import org.eclipse.serializer.collections.lazy.LazyHashMap;

import de.dreja.game.model.Game;
import de.dreja.quiz.model.Quiz;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class DbRoot {
    
    private final Map<IdBase64, Quiz> quizzes = new LazyHashMap<>();

    private final Map<IdBase64, Game> games = new LazyHashMap<>();

    private IdBase64 lastId = null;
    
    @Nonnull
    public Map<IdBase64, Quiz> getQuizzes() {
        return quizzes;
    }

    @Nonnull
    public Optional<Quiz> getQuiz(@Nullable IdBase64 id) {
        return HasId.get(quizzes, id);
    }

    @Nonnull
    public Map<IdBase64, Game> getGames() {
        return games;
    }

    @Nonnull
    public Optional<Game> getGame(@Nullable IdBase64 id) {
        return HasId.get(games, id);
    }

    @Nonnull
    public IdBase64 nextId() {
        if(lastId == null) {
            lastId = IdBase64.of(Instant.now().getEpochSecond());
        }
        lastId = lastId.next();
        return lastId;
    }

}
