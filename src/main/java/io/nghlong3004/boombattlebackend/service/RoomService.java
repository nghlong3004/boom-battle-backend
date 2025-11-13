package io.nghlong3004.boombattlebackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nghlong3004.boombattlebackend.model.game.BomberInfo;
import io.nghlong3004.boombattlebackend.model.game.ChatMessage;
import io.nghlong3004.boombattlebackend.model.game.Room;
import io.nghlong3004.boombattlebackend.model.request.ChatMessageRequest;
import io.nghlong3004.boombattlebackend.model.request.CreateRoomRequest;
import io.nghlong3004.boombattlebackend.model.request.JoinRoomRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public String create(CreateRoomRequest createRoomRequest) throws JsonProcessingException {
        Room room = fromCreateRoomRequest(createRoomRequest);
        room.setChatMessages(new ArrayList<>());
        rooms.put(room.getId(), room);
        log.info("Create room width id: {}", room.getId());
        return objectMapper.writeValueAsString(room);
    }

    public String list() throws JsonProcessingException {
        var roomList = new ArrayList<>(rooms.values());
        return objectMapper.writeValueAsString(roomList);
    }

    public Room join(JoinRoomRequest joinRoomRequest) throws JsonProcessingException {
        var room = rooms.get(joinRoomRequest.id());
        if (room == null || room.getBomberInfos()
                                .size() == room.getMaxBomber()) {
            log.info("Join room is false.");
            return null;
        }
        room.getBomberInfos()
            .add(joinRoomRequest.bomberInfo());
        room.getChatMessages()
            .add(new ChatMessage("System", "%s join room".formatted(joinRoomRequest.bomberInfo()
                                                                                   .getName())));
        log.info("Join room {}.", room.getId());
        return room;
    }

    public Room leave(String bomberId) {
        log.info("find element contain bomberId: {}", bomberId);
        var entryOpt = rooms.entrySet()
                            .stream()
                            .filter(e -> e.getValue()
                                          .getBomberInfos()
                                          .stream()
                                          .anyMatch(info -> info.getId()
                                                                .equals(bomberId)))
                            .findFirst();

        if (entryOpt.isEmpty()) {
            return null;
        }

        var entry = entryOpt.get();
        String roomId = entry.getKey();
        Room room = entry.getValue();
        String bomberName = null;
        log.info("get bomber name and remove bomber by bomberId in room");
        for (var bomberInfo : room.getBomberInfos()) {
            if (bomberInfo.getId()
                          .equals(bomberId)) {
                bomberName = bomberInfo.getName();
                room.getBomberInfos()
                    .remove(bomberInfo);
                break;
            }
        }

        log.info("remove room if list bomber info is empty");
        if (room.getBomberInfos()
                .isEmpty()) {
            rooms.remove(roomId, room);
            return null;
        }
        var chatMessage = new ChatMessage("System", "%s leave room".formatted(bomberName));
        room.getChatMessages()
            .add(chatMessage);
        return room;
    }

    public Room chat(ChatMessageRequest chatMessageRequest) {
        Room room = rooms.get(chatMessageRequest.roomId());
        if (room == null) {
            log.info("roomId: {} not found", chatMessageRequest.roomId());
            return null;
        }

        room.getChatMessages()
            .add(new ChatMessage(chatMessageRequest.owner(), chatMessageRequest.content()));

        return room;

    }

    private Room fromCreateRoomRequest(CreateRoomRequest createRoomRequest) {
        String id = String.valueOf(System.currentTimeMillis());
        BomberInfo bomberInfo = new BomberInfo(createRoomRequest.owner(), createRoomRequest.ownerName(),
                                               createRoomRequest.skinType(), true);
        var bomberInfos = new ArrayList<BomberInfo>();
        bomberInfos.add(bomberInfo);
        return Room.builder()
                   .id(id)
                   .owner(createRoomRequest.owner())
                   .name(createRoomRequest.name())
                   .maxBomber(createRoomRequest.maxBombers())
                   .bomberInfos(bomberInfos)
                   .map(createRoomRequest.mapType())
                   .build();
    }
}
