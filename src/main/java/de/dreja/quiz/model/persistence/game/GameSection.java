package de.dreja.quiz.model.persistence.game;

import java.util.ArrayList;
import java.util.List;

import de.dreja.quiz.model.persistence.quiz.Section;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

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

    @Column(nullable=false)
    private boolean complete = false;

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

    public boolean isComplete() {
        return complete;
    }

    @Nonnull
    public GameSection setComplete(boolean complete) {
        this.complete = complete;
        return this;
    }
    
}
