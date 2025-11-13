package io.nghlong3004.boombattlebackend.model.request;

import org.springframework.lang.NonNull;


public record ChatMessageRequest(
        @NonNull
        String roomId,
        @NonNull
        String owner,
        @NonNull
        String content
) {}
