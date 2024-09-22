package de.dreja.quiz.model.persistence.game;

import java.util.ArrayList;
import java.util.List;

import de.dreja.quiz.model.persistence.quiz.Category;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "game_category")
public class GameCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @ManyToOne(targetEntity = Category.class, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(targetEntity = GameQuestion.class, mappedBy = "category")
    private final List<GameQuestion> questions = new ArrayList<>();

    @ManyToOne(targetEntity = Game.class)
    @JoinColumn(name = "game_id")
    private Game game;

    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }

    @Nonnull
    public Category getCategory() {
        return category;
    }

    @Nonnull
    public GameCategory setCategory(@Nonnull Category category) {
        this.category = category;
        return this;
    }

    @Nonnull
    public List<GameQuestion> getQuestions() {
        return questions;
    }

    @Nonnull
    public GameCategory addQuestion(@Nonnull GameQuestion question) {
        question.setCategory(this);
        if (questions.contains(question)) {
            return this;
        }
        questions.add(question);
        return this;
    }

    @Nonnull
    public GameCategory removeQuestion(@Nonnull GameQuestion question) {
        if (questions.remove(question)) {
            question.setCategory(null);
        }
        return this;
    }

    @Nonnull
    public Game getGame() {
        return game;
    }

    protected void setGame(Game game) {
        this.game = game;
    }
}
