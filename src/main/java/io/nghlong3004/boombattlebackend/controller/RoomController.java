package io.nghlong3004.boombattlebackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nghlong3004.boombattlebackend.model.MessageType;
import io.nghlong3004.boombattlebackend.model.game.MapType;
import io.nghlong3004.boombattlebackend.model.game.Room;
import io.nghlong3004.boombattlebackend.model.request.ChatMessageRequest;
import io.nghlong3004.boombattlebackend.model.request.CreateRoomRequest;
import io.nghlong3004.boombattlebackend.model.request.JoinRoomRequest;
import io.nghlong3004.boombattlebackend.model.response.MessageResponse;
import io.nghlong3004.boombattlebackend.service.BomberService;
import io.nghlong3004.boombattlebackend.service.GameMapService;
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
    private final GameMapService gameMapService;
    private final RoomService roomService;
    private final ObjectMapper objectMapper;

    public void create(WebSocketSession session, String data) throws IOException {
        CreateRoomRequest createRoomRequest = objectMapper.readValue(data, CreateRoomRequest.class);
        String dataResponse = roomService.create(createRoomRequest);
        MessageResponse message = new MessageResponse(MessageType.CREATE_ROOM, dataResponse);
        response(session, message);
    }

    public void list(WebSocketSession session) throws IOException {
        String dataResponse = roomService.list();
        MessageResponse message = new MessageResponse(MessageType.ROOM_LIST, dataResponse);
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
        MessageResponse message = new MessageResponse(MessageType.LEAVE_ROOM, "");
        response(session, message);
        Room room = roomService.leave(session.getId());
        updateRoom(room);
    }

    public void disconnect(WebSocketSession session) throws IOException {
        Room room = roomService.leave(session.getId());
        updateRoom(room);
    }

    public void chat(WebSocketSession session, String data) throws IOException {
        var chatMessageRequest = objectMapper.readValue(data, ChatMessageRequest.class);
        Room room = roomService.chat(chatMessageRequest);
        updateRoom(room);
    }

    public void updateReady(WebSocketSession session, String data) throws IOException {
        Room room = roomService.updateReady(session.getId(), data);
        updateRoom(room);
    }

    public void updateMap(WebSocketSession session, String data) throws IOException {
        Room room = roomService.updateMap(session.getId(), data);
        updateRoom(room);
    }

    public void updateSkin(WebSocketSession session, String data) throws IOException {
        Room room = roomService.updateSkin(session.getId(), data);
        updateRoom(room);
    }

    public void startGame(WebSocketSession session, String data) throws IOException {
        MapType mapType = objectMapper.readValue(data, MapType.class);
        MessageResponse message = gameMapService.getMapAndBomberSpawnsAndItemSpawns(mapType);
        Room room = roomService.startGame(session.getId());
        updateAllBomberByRoom(room, message);
    }

    public void action(WebSocketSession session, String data) throws IOException {
        MessageResponse message = new MessageResponse(MessageType.BOMBER_ACTION, data);
        Room room = roomService.action(session.getId());
        updateAllBomberByRoom(room, message);
    }

    private void updateRoom(Room room) throws IOException {
        if (room == null) {
            return;
        }
        String dataResponse = objectMapper.writeValueAsString(room);
        MessageResponse message = new MessageResponse(MessageType.ROOM_UPDATE, dataResponse);
        updateAllBomberByRoom(room, message);
    }

    private void updateAllBomberByRoom(Room room, MessageResponse message) throws IOException {
        if (room == null) {
            return;
        }
        for (var bomberInfo : room.getBomberInfos()) {
            var session = getSessionByBomberId(bomberInfo.getId());
            if (session != null) {
                response(session, message);
            }
        }
    }

    private void response(WebSocketSession session, MessageResponse message) throws IOException {
        String json = objectMapper.writeValueAsString(message);
        log.info("Reply with id{}: {}", session.getId(), json);
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
