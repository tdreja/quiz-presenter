package de.dreja.quiz.model;

import java.time.LocalDateTime;

import de.dreja.common.model.Base64Id;
import de.dreja.common.model.HasId;

public class Quiz implements HasId {
    
    private Base64Id id;
    private String name;
    private String author;
    private LocalDateTime lastUpdate;
    
    @Override
    public Base64Id getId() {
        return id;
    }
}
