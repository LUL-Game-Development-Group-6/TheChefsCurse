package com.mygdx.game.physics.Room.factories;

import com.mygdx.game.physics.Room.builders.KitchenRoomBuilder;

public class KitchenRoomFactory extends RoomFactory{
    @Override
    public KitchenRoomBuilder createRoomBuilder() {
        return new KitchenRoomBuilder();
    }
}
