package de.dreja.quiz.model.persistence.game;

import de.dreja.quiz.model.persistence.quiz.Question;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "game_question")
public class GameQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    private long points;

    @ManyToOne(targetEntity = Team.class)
    @JoinColumn(name = "answered_by_team_id")
    private Team answeredBy;

    @ManyToOne(targetEntity = GameSection.class, optional = false)
    @JoinColumn(name = "section_id")
    private GameSection section;

    @ManyToOne(targetEntity = Question.class, optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }

    public long getPoints() {
        return points;
    }

    @Nonnull
    public GameQuestion setPoints(long points) {
        this.points = points;
        return this;
    }

    @Nullable
    public Team getAnsweredBy() {
        return answeredBy;
    }

    @Nonnull
    public GameQuestion setAnsweredBy(@Nullable Team answeredBy) {
        this.answeredBy = answeredBy;
        return this;
    }

    @Nonnull
    public Question getQuestion() {
        return question;
    }

    @Nonnull
    public GameQuestion setQuestion(@Nonnull Question question) {
        this.question = question;
        return this;
    }

    @Nonnull
    public GameSection getSection() {
        return section;
    }

    protected void setSection(GameSection category) {
        this.section = category;
    }
}
