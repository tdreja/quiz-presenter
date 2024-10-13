package de.dreja.game.model;

import de.dreja.common.model.HasId;
import de.dreja.common.model.IdBase64;
import jakarta.annotation.Nonnull;

public class Game implements HasId<IdBase64> {
    
    private IdBase64 id;

    @Override
    @Nonnull
    public IdBase64 getId() {
        return id;
    }

    public void setId(IdBase64 id) {
        this.id = id;
    }
    
}
