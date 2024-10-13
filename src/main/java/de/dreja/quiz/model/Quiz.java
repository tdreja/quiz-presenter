package de.dreja.quiz.model;

import java.time.LocalDateTime;

import de.dreja.common.model.HasId;
import de.dreja.common.model.IdBase32;

public class Quiz implements HasId<IdBase32> {
    
    private IdBase32 id;
    private String name;
    private String author;
    private LocalDateTime lastUpdate;
    
    @Override
    public IdBase32 getId() {
        return id;
    }
}
