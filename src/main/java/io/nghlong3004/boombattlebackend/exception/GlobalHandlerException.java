package io.nghlong3004.boombattlebackend.exception;

import io.nghlong3004.boombattlebackend.model.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(value = ResourceException.class)
    public ResponseEntity<ErrorResponse> handleResourceException(ResourceException e) {
        ErrorCode errorCode = e.getCode();
        String message = errorCode.getMessage();
        return globalHandlerException(errorCode, message);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        ErrorCode errorCode = ErrorCode.BAD_CREDENTIALS;
        String message = e.getMessage();
        return globalHandlerException(errorCode, message);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        String message = "An unexpected error occurred: " + e.getMessage();
        return globalHandlerException(errorCode, message);
    }

    private ResponseEntity<ErrorResponse> globalHandlerException(ErrorCode errorCode, String message) {
        int status = errorCode.getStatus()
                              .value();
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .status(status)
                                                   .error(errorCode.name())
                                                   .message(message)
                                                   .build();
        return ResponseEntity.status(errorCode.getStatus())
                             .body(errorResponse);
    }

}
