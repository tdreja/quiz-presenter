package de.dreja.quiz.interfaces.web;

import jakarta.annotation.Nonnull;
import org.springframework.http.HttpStatusCode;

import java.util.Objects;

public class WebException extends RuntimeException {

    private final HttpStatusCode statusCode;

    public WebException(@Nonnull HttpStatusCode statusCode, @Nonnull String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public WebException(@Nonnull HttpStatusCode statusCode, @Nonnull String message, @Nonnull Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    @Override
    @Nonnull
    public String getMessage() {
        return Objects.requireNonNullElse(super.getMessage(), "");
    }

    @Nonnull
    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
