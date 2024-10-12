package de.dreja.common.model;

import jakarta.annotation.Nonnull;

public interface HasId {
    
    @Nonnull
    public Base64Id getId();
}
