package de.dreja.quiz.interfaces.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(WebException.class)
    public ResponseEntity<String> handleWebException(WebException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
    }
}
