package io.nghlong3004.boombattlebackend.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Boomber {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
