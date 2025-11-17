package io.nghlong3004.boombattlebackend.model.response;

import io.nghlong3004.boombattlebackend.model.game.ItemType;
import org.springframework.lang.NonNull;

public record StartGameResponse(
        @NonNull
        MapDataResponse mapData,
        @NonNull
        ItemType[][] itemSpawns
) {}
