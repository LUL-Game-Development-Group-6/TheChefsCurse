package com.mygdx.game.Screens;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.gamesave.GameSaveLoader;

public class Menu extends Game {

    private static Menu game;

    public static Menu getInstance() {
        if(game == null) game = new Menu();
        return game;
    }
    
    public SpriteBatch batch;
    private int round;

    public void create() {
        round = 1;
        GameSaveLoader.getInstance().load();
        batch = new SpriteBatch();
        this.setScreen(Cover.getInstance());
    }

    public void render() {
        super.render();
    }
    public void dispose() {
        super.dispose();
    }

    public int getRound() {
        return round;
    }

    public String getStrRound() {
        String string = Integer.toString(round);
        return string;
    }

    public void incrementRound() {
        round++;
    }
}