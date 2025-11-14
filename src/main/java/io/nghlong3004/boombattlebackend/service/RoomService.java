package io.nghlong3004.boombattlebackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nghlong3004.boombattlebackend.model.game.*;
import io.nghlong3004.boombattlebackend.model.request.ChatMessageRequest;
import io.nghlong3004.boombattlebackend.model.request.CreateRoomRequest;
import io.nghlong3004.boombattlebackend.model.request.JoinRoomRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
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

    public Room join(JoinRoomRequest joinRoomRequest) {
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
        var entryOpt = findRoomByBomberId(bomberId);
        if (entryOpt.isEmpty()) {
            return null;
        }

        var entry = entryOpt.get();
        String roomId = entry.getKey();
        Room room = entry.getValue();
        String bomberName = getBomberNameAndRemoveBomberByBomberId(room, bomberId);

        log.info("remove room if list bomber info is empty");
        if (room.getBomberInfos()
                .isEmpty()) {
            rooms.remove(roomId, room);
            return null;
        }
        var chatMessage = new ChatMessage("System", "%s left the room".formatted(bomberName));
        room.getChatMessages()
            .add(chatMessage);
        log.info("switch owner in room");
        if (room.getOwner()
                .equals(bomberId)) {
            String newOwner = room.getBomberInfos()
                                  .getFirst()
                                  .getId();
            room.setOwner(newOwner);
            chatMessage = new ChatMessage("System", "%s becomes the room owner.".formatted(room.getBomberInfos()
                                                                                               .getFirst()
                                                                                               .getName()));
            room.getChatMessages()
                .add(chatMessage);
        }
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

    public Room updateReady(String bomberId, String data) {
        log.debug("bomberId {} is ready", bomberId);
        boolean isReady = data.contains("true");
        return updateReadyOrMapOrSkin(bomberId, isReady, null, null);
    }

    public Room updateMap(String bomberId, String data) throws JsonProcessingException {
        MapType mapType = objectMapper.readValue(data, MapType.class);
        return updateReadyOrMapOrSkin(bomberId, null, mapType, null);
    }

    public Room updateSkin(String bomberId, String data) throws JsonProcessingException {
        log.debug("bomberId: {} is changing skin", bomberId);
        SkinType skinType = objectMapper.readValue(data, SkinType.class);
        return updateReadyOrMapOrSkin(bomberId, null, null, skinType);
    }

    private Room updateReadyOrSkin(String bomberId, Boolean isReady, SkinType skinType, Room room) {
        for (var bomberInfo : room.getBomberInfos()) {
            if (bomberInfo.getId()
                          .equals(bomberId)) {
                if (isReady != null) {
                    bomberInfo.setReady(isReady);
                }
                else if (skinType != null) {
                    bomberInfo.setSkin(skinType);
                }
                break;
            }
        }
        return room;
    }

    private Room updateReadyOrMapOrSkin(String bomberId, Boolean isReady, MapType mapType, SkinType skinType) {
        var entryRoom = findRoomByBomberId(bomberId);
        if (entryRoom.isEmpty()) {
            return null;
        }
        Room room = entryRoom.get()
                             .getValue();
        if (mapType != null) {
            log.debug("roomId: {} update map", room.getId());
            room.setMap(mapType);
            return room;
        }
        return updateReadyOrSkin(bomberId, isReady, skinType, room);
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

    private String getBomberNameAndRemoveBomberByBomberId(Room room, String bomberId) {
        for (var bomberInfo : room.getBomberInfos()) {
            if (bomberInfo.getId()
                          .equals(bomberId)) {
                room.getBomberInfos()
                    .remove(bomberInfo);
                return bomberInfo.getName();
            }
        }
        return null;
    }

    private Optional<Map.Entry<String, Room>> findRoomByBomberId(String bomberId) {
        return rooms.entrySet()
                    .stream()
                    .filter(e -> e.getValue()
                                  .getBomberInfos()
                                  .stream()
                                  .anyMatch(info -> info.getId()
                                                        .equals(bomberId)))
                    .findFirst();
    }
}
