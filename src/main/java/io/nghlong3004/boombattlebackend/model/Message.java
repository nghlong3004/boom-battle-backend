package io.nghlong3004.boombattlebackend.model;

import org.springframework.lang.NonNull;

public record Message(
        @NonNull
        MessageType type,
        @NonNull
        String data
) {}
