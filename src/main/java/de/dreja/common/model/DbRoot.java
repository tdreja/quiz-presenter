package de.dreja.common.model;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.eclipse.serializer.collections.lazy.LazyHashMap;

import de.dreja.quiz.model.Quiz;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class DbRoot {
    
    private final Map<Base64Id, Quiz> quizzes = new LazyHashMap<>();

    private final Map<Base64Id, Object> games = new LazyHashMap<>();

    private Base64Id lastId = null;
    
    @Nonnull
    public Map<Base64Id, Quiz> getQuizzes() {
        return quizzes;
    }

    @Nonnull
    public Optional<Quiz> getQuiz(@Nullable Base64Id id) {
        return get(id, quizzes);
    }

    @Nonnull
    public Map<Base64Id, Object> getGames() {
        return games;
    }

    @Nonnull
    public Optional<Object> getGame(@Nullable Base64Id id) {
        return get(id, games);
    }

    @Nonnull
    public Base64Id nextId() {
        final LocalDate today = LocalDate.now();
        if(lastId != null) {
            if(lastId.getDate().equals(today)) {
                lastId = Base64Id.of(today, lastId.getIndex()+1);
            } else {
                lastId = Base64Id.of(today, 1L);
            }
            return lastId;
        }
        lastId = Base64Id.of(today, 1L);
        return lastId;
    }
    
    @Nonnull
    protected <TYPE> Optional<TYPE> get(@Nullable Base64Id id, @Nullable Map<Base64Id, TYPE> map) {
        if(id == null || map == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(map.get(id));
    }
}
