package de.dreja.game.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import de.dreja.common.model.HasId;
import de.dreja.common.model.HasTimestamp;
import de.dreja.common.model.IdBase32;
import jakarta.annotation.Nonnull;

public class Game implements HasId<IdBase32>, HasTimestamp {
    
    private IdBase32 id;
    private LocalDateTime createDate = LocalDateTime.now();

    @Override
    @Nonnull
    @JsonInclude(Include.NON_NULL)
    public IdBase32 getId() {
        return id;
    }

    public void setId(IdBase32 id) {
        this.id = id;
    }

    @Override
    public LocalDateTime getCreateDate() {
        return createDate;
    }
    
}
