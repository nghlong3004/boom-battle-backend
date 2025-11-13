package io.nghlong3004.boombattlebackend.model.request;

import io.nghlong3004.boombattlebackend.model.game.MapType;
import io.nghlong3004.boombattlebackend.model.game.SkinType;
import lombok.Builder;
import org.springframework.lang.NonNull;

@Builder
public record CreateRoomRequest(
        @NonNull
        String owner,
        @NonNull
        String ownerName,
        @NonNull
        String name,
        @NonNull
        Integer maxBombers,
        @NonNull
        MapType mapType,
        @NonNull
        SkinType skinType
) {}
