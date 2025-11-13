package io.nghlong3004.boombattlebackend.model;

public enum MessageType {
    CONNECT,
    CREATE_ROOM,
    JOIN_ROOM,
    LEAVE_ROOM,
    START_GAME,
    CHAT_MESSAGE,
    PLAYER_MOVE,
    PLACE_BOMB,
    GAME_STATE_UPDATE,
    ROOM_LIST,
    ROOM_UPDATE,
    UPDATE_READY,
    UPDATE_SKIN,
    UPDATE_MAP,
    ERROR
}
