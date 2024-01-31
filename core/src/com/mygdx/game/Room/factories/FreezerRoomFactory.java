package com.mygdx.game.Room.factories;

import com.mygdx.game.Room.builders.FreezerRoomBuilder;

public class FreezerRoomFactory extends RoomFactory{
    @Override
    public FreezerRoomBuilder createRoomBuilder() {
        return new FreezerRoomBuilder();
    }
}
