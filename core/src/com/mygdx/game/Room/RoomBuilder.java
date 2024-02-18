package com.mygdx.game.Room;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Screens.Menu;

public class RoomBuilder {
    private static RoomBuilder roomBuilder;

    private Room currentlyBuiltRoom;

    private Room.RoomType roomType;

    public RoomBuilder() {
        currentlyBuiltRoom = new Room();
        roomType = Room.RoomType.KITCHEN_1;
    }

    public static RoomBuilder init() {
        if(roomBuilder == null) roomBuilder = new RoomBuilder();
        return roomBuilder;
    }

    public RoomBuilder withRandomRoomType() {
        roomType = getRandomRoomType();
        return this;
    }

    public RoomBuilder create(Menu game) {
        if(currentlyBuiltRoom == null && roomType == null) {
            throw new IllegalStateException("Cannot create room as Room is null or it's type is undefined.");
        }
        currentlyBuiltRoom.create(roomType, game);
        return this;
    }

    public Room get() {
        if(currentlyBuiltRoom == null) {
            throw new IllegalStateException("Cannot create room as Room is null.");
        }
        return currentlyBuiltRoom;
    }

    private static Room.RoomType getRandomRoomType() {
        int a = MathUtils.random(1, 9);
        Room.RoomType returnRoom;

        switch (a) {

            case 1:
                returnRoom = Room.RoomType.KITCHEN_1;
                break;
            case 2:
                returnRoom = Room.RoomType.KITCHEN_2;
                break;
            case 3:
                returnRoom = Room.RoomType.KITCHEN_3;
                break;
            case 4:
                returnRoom = Room.RoomType.FREEZER_1;
                break;
            case 5:
                returnRoom = Room.RoomType.FREEZER_2;
                break;
            case 6:
                returnRoom = Room.RoomType.FREEZER_3;
                break;
            case  7:
                returnRoom = Room.RoomType.RESTAURANT_1;
                break;
            case  8:
                returnRoom = Room.RoomType.RESTAURANT_2;
                break;
            case 9:
                returnRoom = Room.RoomType.RESTAURANT_3;
                break;
            default:
                returnRoom = Room.RoomType.RESTAURANT_1;
                break;
        }
        return returnRoom;
    }
}