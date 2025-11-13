package io.nghlong3004.boombattlebackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nghlong3004.boombattlebackend.model.Message;
import io.nghlong3004.boombattlebackend.model.MessageType;
import io.nghlong3004.boombattlebackend.model.game.Room;
import io.nghlong3004.boombattlebackend.model.request.CreateRoomRequest;
import io.nghlong3004.boombattlebackend.model.request.JoinRoomRequest;
import io.nghlong3004.boombattlebackend.service.BomberService;
import io.nghlong3004.boombattlebackend.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomController {

    private final BomberService bomberService;
    private final RoomService roomService;
    private final ObjectMapper objectMapper;

    public void create(WebSocketSession session, String data) throws IOException {
        CreateRoomRequest createRoomRequest = objectMapper.readValue(data, CreateRoomRequest.class);
        String dataResponse = roomService.create(createRoomRequest);
        Message message = new Message(MessageType.CREATE_ROOM, dataResponse);
        response(session, message);
    }

    public void list(WebSocketSession session) throws IOException {
        String dataResponse = roomService.list();
        Message message = new Message(MessageType.ROOM_LIST, dataResponse);
        response(session, message);
    }

    public void join(WebSocketSession session, String data) throws IOException {
        JoinRoomRequest joinRoomRequest = objectMapper.readValue(data, JoinRoomRequest.class);
        Room room = roomService.join(joinRoomRequest);
        if (room == null) {
            list(session);
            return;
        }
        updateRoom(room);
    }

    public void leave(WebSocketSession session) throws IOException {
        Message message = new Message(MessageType.LEAVE_ROOM, "");
        response(session, message);
        Room room = roomService.leave(session.getId());
        updateRoom(room);
    }

    private void updateRoom(Room room) throws IOException {
        if (room == null) {
            return;
        }
        String dataResponse = objectMapper.writeValueAsString(room);
        for (var bomberInfo : room.getBomberInfos()) {
            var session = getSessionByBomberId(bomberInfo.getId());
            if (session != null) {
                Message message = new Message(MessageType.ROOM_UPDATE, dataResponse);
                response(session, message);
            }
        }
    }

    private void response(WebSocketSession session, Message message) throws IOException {
        String json = objectMapper.writeValueAsString(message);
        log.info("Reply: {}", json);
        session.sendMessage(new TextMessage(json));
    }

    private WebSocketSession getSessionByBomberId(String bomberId) {
        for (var session : bomberService.getSessions()) {
            if (bomberId.equals(session.getId())) {
                return session;
            }
        }
        return null;
    }
}
