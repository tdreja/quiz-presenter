package de.dreja.quiz.model;

import java.time.LocalDateTime;

import de.dreja.common.model.HasId;
import de.dreja.common.model.IdBase64;

public class Quiz implements HasId<IdBase64> {
    
    private IdBase64 id;
    private String name;
    private String author;
    private LocalDateTime lastUpdate;
    
    @Override
    public IdBase64 getId() {
        return id;
    }
}
