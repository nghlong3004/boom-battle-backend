package io.nghlong3004.boombattlebackend.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameWebSocket extends TextWebSocketHandler {
    private final GameWebSocketManager manager;

    @Override
    public void afterConnectionEstablished(
            @NonNull WebSocketSession session) throws Exception {
        manager.connect(session);
    }

    @Override
    protected void handleTextMessage(
            @NonNull WebSocketSession session,
            @NonNull TextMessage message) throws Exception {
        manager.managerMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(
            @NonNull WebSocketSession session,
            @NonNull CloseStatus status) throws Exception {
        manager.disconnect(session, status);
    }
}
