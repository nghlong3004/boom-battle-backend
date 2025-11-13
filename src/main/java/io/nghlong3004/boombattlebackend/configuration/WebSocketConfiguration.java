package io.nghlong3004.boombattlebackend.configuration;

import io.nghlong3004.boombattlebackend.websocket.GameWebSocket;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final GameWebSocket gameWebSocket;

    @Override
    public void registerWebSocketHandlers(
            @NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(gameWebSocket, "/ws")
                .setAllowedOrigins("*");
    }
}
