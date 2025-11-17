package io.nghlong3004.boombattlebackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nghlong3004.boombattlebackend.model.MessageType;
import io.nghlong3004.boombattlebackend.model.game.ItemType;
import io.nghlong3004.boombattlebackend.model.game.MapType;
import io.nghlong3004.boombattlebackend.model.response.MapDataResponse;
import io.nghlong3004.boombattlebackend.model.response.MessageResponse;
import io.nghlong3004.boombattlebackend.model.response.StartGameResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameMapService {
    private final ObjectMapper objectMapper;
    private final Map<MapType, MapDataResponse> maps = new EnumMap<>(MapType.class);

    @PostConstruct
    public void init() throws IOException {
        for (var mapType : MapType.values()) {
            String resourcePath = "/map/%s.json".formatted(mapType.name()
                                                                  .toLowerCase());
            maps.put(mapType, loadMapFromFile(resourcePath));
        }
    }

    public MessageResponse getMapAndBomberSpawnsAndItemSpawns(MapType mapType) throws JsonProcessingException {
        var mapDataResponse = maps.get(mapType);
        if (mapDataResponse != null) {
            int n = mapDataResponse.map().length;
            int m = mapDataResponse.map()[0].length;
            var itemSpawns = new ItemType[n][m];
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < m; ++j) {
                    itemSpawns[i][j] = ItemType.BLANK;
                    if (Math.random() < 0.6) {
                        ItemType randomType = ItemType.random();
                        itemSpawns[i][j] = randomType;
                    }
                }
            }
            var startGameResponse = new StartGameResponse(mapDataResponse, itemSpawns);
            String data = objectMapper.writeValueAsString(startGameResponse);
            return new MessageResponse(MessageType.START_GAME, data);
        }
        return null;
    }

    private MapDataResponse loadMapFromFile(String resourcePath) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            log.info("loading file: {}", resourcePath);
            return objectMapper.readValue(inputStream, MapDataResponse.class);
        }
    }

}
