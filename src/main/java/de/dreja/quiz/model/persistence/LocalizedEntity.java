package de.dreja.quiz.model.persistence;

import java.util.Locale;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/** 
 * Common API for all entitites in the database that reference a specific language
*/
@MappedSuperclass
public class LocalizedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(name = "locale", nullable = false)
    private Locale locale = Locale.GERMAN;

    /**
     * @return ID of the entity
     */
    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }
    
    /**
     * @return Language of the current entity
     */
    @Nonnull
    public Locale getLocale() {
        return locale;
    }

    @Nonnull
    public LocalizedEntity setLocale(@Nullable Locale locale) {
        this.locale = locale == null ? Locale.GERMAN : locale;
        return this;
    }
}
