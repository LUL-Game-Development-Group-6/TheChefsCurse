package com.mygdx.game.physics;

public class Player extends DynamicObject {
    private static Player player;
    private int score;

    public static Player getInstance()
    {
        if(player == null) player = new Player();
        return player;
    }
}
