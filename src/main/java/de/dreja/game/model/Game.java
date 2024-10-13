package de.dreja.game.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import de.dreja.common.model.HasId;
import de.dreja.common.model.HasTimestamp;
import de.dreja.common.model.IdBase64;
import jakarta.annotation.Nonnull;

public class Game implements HasId<IdBase64>, HasTimestamp {
    
    private IdBase64 id;
    private LocalDateTime createDate = LocalDateTime.now();

    @Override
    @Nonnull
    @JsonInclude(Include.NON_NULL)
    public IdBase64 getId() {
        return id;
    }

    public void setId(IdBase64 id) {
        this.id = id;
    }

    @Override
    public LocalDateTime getCreateDate() {
        return createDate;
    }
    
}
