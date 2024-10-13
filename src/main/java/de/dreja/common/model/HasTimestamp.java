package de.dreja.common.model;

import java.time.LocalDateTime;

import jakarta.annotation.Nonnull;

public interface HasTimestamp {
    
    @Nonnull
    LocalDateTime getCreateDate();
}
