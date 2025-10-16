package io.nghlong3004.boombattlebackend.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Message {
    private MessageType type;
    private String content;
    @NotNull
    private String sender;
}
