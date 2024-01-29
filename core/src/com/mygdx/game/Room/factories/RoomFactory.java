package com.mygdx.game.Room.factories;

import com.mygdx.game.Room.builders.RoomBuilder;

public abstract class RoomFactory {
    
    public RoomBuilder create()
    {
        RoomBuilder roomBuilder = createRoomBuilder();
        return roomBuilder;
    }

    protected abstract RoomBuilder createRoomBuilder();
}
