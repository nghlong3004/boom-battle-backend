package io.nghlong3004.boombattlebackend.model.response;

import lombok.Builder;

@Builder
public record ErrorResponse(
        int status,
        String error,
        String message
) {
}
