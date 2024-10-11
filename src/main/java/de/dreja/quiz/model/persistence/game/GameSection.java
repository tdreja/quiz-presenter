package de.dreja.quiz.model.persistence.game;

import java.util.ArrayList;
import java.util.List;

import de.dreja.quiz.model.persistence.quiz.Section;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

@Entity
@Table(name = "game_section")
public class GameSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(targetEntity = Section.class, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    @OneToMany(targetEntity = GameQuestion.class, mappedBy = "section")
    private final List<GameQuestion> questions = new ArrayList<>();

    @ManyToOne(targetEntity = Game.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }

    @Nonnull
    public Section getSection() {
        return section;
    }

    @Nonnull
    public GameSection setSection(@Nonnull Section category) {
        this.section = category;
        return this;
    }

    @Nonnull
    public List<GameQuestion> getQuestions() {
        return questions;
    }

    @Nonnull
    public GameSection addQuestion(@Nonnull GameQuestion question) {
        question.setSection(this);
        if (questions.contains(question)) {
            return this;
        }
        questions.add(question);
        return this;
    }

    @Nonnull
    public GameSection removeQuestion(@Nonnull GameQuestion question) {
        if (questions.remove(question)) {
            question.setSection(null);
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

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public GameSection setName(@Nonnull String name) {
        this.name = name;
        return this;
    }
}
