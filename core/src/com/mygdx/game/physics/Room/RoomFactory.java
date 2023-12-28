package com.mygdx.game.physics.Room;

public abstract class RoomFactory {
    public RoomBuilder create()
    {
        RoomBuilder roomBuilder = createRoomBuilder();
        return roomBuilder;
    }

    protected abstract RoomBuilder createRoomBuilder();
}
