package io.nghlong3004.boombattlebackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nghlong3004.boombattlebackend.model.MessageType;
import io.nghlong3004.boombattlebackend.model.response.MessageResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class BomberService {

    @Getter
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final ObjectMapper mapper;

    public String connect(WebSocketSession session) throws JsonProcessingException {
        sessions.add(session);
        var message = new MessageResponse(MessageType.CONNECT, session.getId());
        log.info("Client with sessionId:{} connecting", session.getId());
        return mapper.writeValueAsString(message);
    }

    public void disconnect(WebSocketSession session, CloseStatus status) {
        log.info("Client with sessionId:{} disconnecting and status: {}", session.getId(), status);
        sessions.remove(session);
    }
}
