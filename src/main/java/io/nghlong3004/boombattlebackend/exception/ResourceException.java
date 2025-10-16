package io.nghlong3004.boombattlebackend.exception;

import lombok.Getter;

@Getter
public class ResourceException extends RuntimeException {
    private final ErrorCode code;

    public ResourceException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
