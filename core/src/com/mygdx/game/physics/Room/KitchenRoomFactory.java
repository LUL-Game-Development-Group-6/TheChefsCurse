package com.mygdx.game.physics.Room;

public class KitchenRoomFactory extends RoomFactory{
    @Override
    public KitchenRoomBuilder createRoomBuilder() {
        return new KitchenRoomBuilder();
    }
}
