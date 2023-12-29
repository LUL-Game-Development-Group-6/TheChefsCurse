package com.mygdx.game.physics.Room.factories;

import com.mygdx.game.physics.Room.builders.RoomBuilder;

public abstract class RoomFactory {
    public RoomBuilder create()
    {
        RoomBuilder roomBuilder = createRoomBuilder();
        return roomBuilder;
    }

    protected abstract RoomBuilder createRoomBuilder();
}
