package io.nghlong3004.boombattlebackend.model.game;

import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor
public enum ItemType {
    BLANK,
    ITEM_BOMB,
    ITEM_BOMB_SIZE,
    ITEM_SHOE;

    private static final Random random = new Random();

    public static ItemType random() {
        ItemType[] values = values();
        return values[random.nextInt(values.length)];
    }
}
