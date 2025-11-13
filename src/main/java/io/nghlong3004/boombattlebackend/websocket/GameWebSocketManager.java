package io.nghlong3004.boombattlebackend.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nghlong3004.boombattlebackend.controller.BomberController;
import io.nghlong3004.boombattlebackend.controller.RoomController;
import io.nghlong3004.boombattlebackend.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GameWebSocketManager {

    private final RoomController roomController;
    private final BomberController bomberController;

    private final ObjectMapper mapper;

    public void connect(WebSocketSession session) throws IOException {
        bomberController.connect(session);
    }

    public void managerMessage(WebSocketSession session, TextMessage textMessage) throws IOException {
        Message message = mapper.readValue(textMessage.getPayload(), Message.class);
        switch (message.type()) {
            case CREATE_ROOM -> roomController.create(session, message.data());
            case JOIN_ROOM -> roomController.join(session, message.data());
            case ROOM_LIST -> roomController.list(session);
            case LEAVE_ROOM -> roomController.leave(session);
            case CHAT_MESSAGE -> roomController.chat(session, message.data());
            case UPDATE_READY -> roomController.updateReady(session, message.data());
            case UPDATE_MAP -> roomController.updateMap(session, message.data());
            case UPDATE_SKIN -> roomController.updateSkin(session, message.data());
        }
    }

    public void disconnect(WebSocketSession session, CloseStatus status) throws IOException {
        roomController.disconnect(session);
        bomberController.disconnect(session, status);
    }

}
