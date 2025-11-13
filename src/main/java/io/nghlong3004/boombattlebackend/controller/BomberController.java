package io.nghlong3004.boombattlebackend.controller;

import io.nghlong3004.boombattlebackend.service.BomberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class BomberController {

    private final BomberService bomberService;

    public void connect(WebSocketSession session) throws IOException {
        String json = bomberService.connect(session);
        session.sendMessage(new TextMessage(json));
    }

    public void disconnect(WebSocketSession session, CloseStatus status) {
        bomberService.disconnect(session, status);
    }

}
