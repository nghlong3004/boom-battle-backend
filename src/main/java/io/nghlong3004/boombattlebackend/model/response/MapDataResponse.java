package io.nghlong3004.boombattlebackend.model.response;


import org.springframework.lang.NonNull;

import java.util.List;

public record MapDataResponse(
        @NonNull
        int[][] map,
        @NonNull
        List<PointResponse> spawns
) {}
