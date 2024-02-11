package com.mygdx.game.Room;

import java.util.ArrayList;

import com.mygdx.game.Room.Furniture.FurnitureType;
import com.mygdx.game.Room.Room.RoomType;

public class FurnitureBuilder {

    public FurnitureBuilder() {}

    public void create(RoomType roomType, ArrayList<Object> entityList) {
        switch (roomType) {
            
            case KITCHEN_1:
                Furniture table = new Furniture(2500, 3889, FurnitureType.METAL_TABLE);
                Furniture chair = new Furniture(1543, 4116, FurnitureType.CHAIR1);
                entityList.add(table);
                entityList.add(chair);
                break;
            case KITCHEN_2:
                Furniture box1 = new Furniture(3263, 2351, FurnitureType.BOX);
                Furniture box2 = new Furniture(5685, 2586, FurnitureType.BOX);
                Furniture table1 = new Furniture(3902, 881, FurnitureType.METAL_TABLE);
                Furniture chair1 = new Furniture(3800, 866, FurnitureType.CHAIR1);
                Furniture chair2 = new Furniture(3850, 750, FurnitureType.CHAIR2);
                Furniture chair3 = new Furniture(6333, 3600, FurnitureType.CHAIR1);
                Furniture chair2_2 = new Furniture(4365, 3329, FurnitureType.CHAIR2);
                Furniture chair3_3 = new Furniture(4590, 3248, FurnitureType.CHAIR1);
                entityList.add(box1);
                entityList.add(box2);
                entityList.add(table1);
                entityList.add(chair1);
                entityList.add(chair2);
                entityList.add(chair3);
                entityList.add(chair2_2);
                entityList.add(chair3_3);
                break;
            case KITCHEN_3:
                Furniture table2 = new Furniture(5250, 3129, FurnitureType.METAL_TABLE);
                Furniture box3 = new Furniture(3827, 4216, FurnitureType.BOX);
                Furniture chair4 = new Furniture(3749, 3022, FurnitureType.CHAIR1);
                Furniture chair5 = new Furniture(7600, 3500, FurnitureType.CHAIR2);
                Furniture chair6 = new Furniture(7440, 3617, FurnitureType.CHAIR1);
                Furniture chair7 = new Furniture(2138, 3858, FurnitureType.CHAIR2);
                entityList.add(table2);
                entityList.add(box3);
                entityList.add(chair4);
                entityList.add(chair5);
                entityList.add(chair6);
                entityList.add(chair7);
                break;
            case FREEZER_1:
                Furniture ice1 = new Furniture(2591, 4242, FurnitureType.ICE);
                entityList.add(ice1);
                break;
            case FREEZER_2:
                Furniture ice2 = new Furniture(1176, 2390, FurnitureType.ICE);
                Furniture ice3 = new Furniture(2526, 1913, FurnitureType.ICE);
                Furniture ice4 = new Furniture(6360, 4496, FurnitureType.ICE);
                entityList.add(ice2);
                entityList.add(ice3);
                entityList.add(ice4);
                break;
            case FREEZER_3:
                Furniture ice5 = new Furniture(1598, 4318, FurnitureType.ICE);
                Furniture ice6 = new Furniture(3650, 3769, FurnitureType.ICE);
                Furniture ice7 = new Furniture(4667, 3022, FurnitureType.ICE);
                Furniture ice8 = new Furniture(5621, 4390, FurnitureType.ICE);
                Furniture ice9 = new Furniture(6719, 3643, FurnitureType.ICE);
                Furniture ice10 = new Furniture(8534, 3491, FurnitureType.ICE);
                Furniture ice11 = new Furniture(9029, 2465, FurnitureType.ICE);
                Furniture ice12 = new Furniture(10199, 2879, FurnitureType.ICE);
                Furniture shelf1 = new Furniture(6762, 2173, FurnitureType.SINGLE_SHELF);
                Furniture shelf2 = new Furniture(6438, 2002, FurnitureType.SINGLE_SHELF);
                entityList.add(ice5);
                entityList.add(ice6);
                entityList.add(ice7);
                entityList.add(ice8);
                entityList.add(ice9);
                entityList.add(ice10);
                entityList.add(ice11);
                entityList.add(ice12);
                entityList.add(shelf1);
                entityList.add(shelf2);
                break;
            case RESTAURANT_1:
                Furniture resTable1 = new Furniture(1318, 4549, FurnitureType.RESTAURANT_TABLE);
                Furniture resTable2 = new Furniture(2096, 3979, FurnitureType.RESTAURANT_TABLE);
                entityList.add(resTable1);
                entityList.add(resTable2);
                break;
            case RESTAURANT_2:

                break;
            case RESTAURANT_3:

                break;
            default:
                break;
        }
    }
    
}
