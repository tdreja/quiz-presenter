package de.dreja.game.model;

import de.dreja.common.model.Base64Id;
import de.dreja.common.model.HasId;
import jakarta.annotation.Nonnull;

public class Game implements HasId<Base64Id> {
    
    private Base64Id id;

    @Override
    @Nonnull
    public Base64Id getId() {
        return id;
    }

    public void setId(Base64Id id) {
        this.id = id;
    }
    
}
