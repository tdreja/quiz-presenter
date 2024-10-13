package de.dreja.common.model;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.eclipse.serializer.collections.lazy.LazyHashMap;

import de.dreja.game.model.Game;
import de.dreja.quiz.model.Quiz;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class DbRoot {
    
    private final Map<Base64Id, Quiz> quizzes = new LazyHashMap<>();

    private final Map<Base64Id, Game> games = new LazyHashMap<>();

    private Base64Id lastId = null;
    
    @Nonnull
    public Map<Base64Id, Quiz> getQuizzes() {
        return quizzes;
    }

    @Nonnull
    public Optional<Quiz> getQuiz(@Nullable Base64Id id) {
        return HasId.get(quizzes, id);
    }

    @Nonnull
    public Map<Base64Id, Game> getGames() {
        return games;
    }

    @Nonnull
    public Optional<Game> getGame(@Nullable Base64Id id) {
        return HasId.get(games, id);
    }

    @Nonnull
    public Base64Id nextId() {
        final LocalDate today = LocalDate.now();
        if(lastId != null) {
            if(lastId.getDate().equals(today)) {
                lastId = Base64Id.of(today, lastId.getIndex()+1);
            } else {
                lastId = Base64Id.of(today, 1);
            }
            return lastId;
        }
        lastId = Base64Id.of(today, 1);
        return lastId;
    }
}
