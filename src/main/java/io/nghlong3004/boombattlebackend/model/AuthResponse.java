package io.nghlong3004.boombattlebackend.model;

import jakarta.validation.constraints.NotNull;

public record AuthResponse(
        @NotNull
        String token
) {
}
