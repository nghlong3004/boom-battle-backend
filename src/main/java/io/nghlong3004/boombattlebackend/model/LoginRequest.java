package io.nghlong3004.boombattlebackend.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotNull
        @Size(min = 4, max = 255)
        String username,
        @NotNull
        @Size(min = 6)
        String password
) {
}
