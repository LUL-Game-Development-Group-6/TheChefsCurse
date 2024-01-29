package com.mygdx.game.Room.factories;

import com.mygdx.game.Room.builders.KitchenRoomBuilder;

public class KitchenRoomFactory extends RoomFactory{
    @Override
    public KitchenRoomBuilder createRoomBuilder() {
        return new KitchenRoomBuilder();
    }
}
