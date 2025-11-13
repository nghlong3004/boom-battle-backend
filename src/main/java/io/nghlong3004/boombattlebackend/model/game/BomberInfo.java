package io.nghlong3004.boombattlebackend.model.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BomberInfo {
    private String id;
    private String name;
    private SkinType skin;
    @JsonProperty("isReady")
    private Boolean ready;
}
