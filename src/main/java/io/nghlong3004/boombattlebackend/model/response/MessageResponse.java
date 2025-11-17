package io.nghlong3004.boombattlebackend.model.response;

import io.nghlong3004.boombattlebackend.model.MessageType;
import org.springframework.lang.NonNull;

public record MessageResponse(
        @NonNull
        MessageType type,
        @NonNull
        String data
) {}
