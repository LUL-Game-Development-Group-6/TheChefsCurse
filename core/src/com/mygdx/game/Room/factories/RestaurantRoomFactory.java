package com.mygdx.game.Room.factories;

import com.mygdx.game.Room.builders.RestaurantRoomBuilder;


public class RestaurantRoomFactory extends RoomFactory {
    @Override
    public RestaurantRoomBuilder createRoomBuilder() {
        return new RestaurantRoomBuilder();
    }
    
}
