package io.github.fintrack.auth.exception;

public abstract class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
