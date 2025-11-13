package io.nghlong3004.boombattlebackend.model.game;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Room {
    private String id;
    private String owner;
    private String name;
    private List<BomberInfo> bomberInfos;
    private Integer maxBomber;
    private MapType map;
}
