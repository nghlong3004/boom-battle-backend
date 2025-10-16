package io.nghlong3004.boombattlebackend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request"),

    USER_EXISTS(HttpStatus.BAD_REQUEST, "Username is already taken"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "You are not authenticated"),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid username or password");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
