package io.nghlong3004.boombattlebackend.model.request;

import io.nghlong3004.boombattlebackend.model.game.BomberInfo;
import org.springframework.lang.NonNull;

public record JoinRoomRequest(
        @NonNull
        String id,
        @NonNull
        BomberInfo bomberInfo
) {}
