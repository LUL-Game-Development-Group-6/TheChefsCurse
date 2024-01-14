package com.mygdx.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Menu extends Game {
    
    public SpriteBatch batch;

    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new Cover(this));
    }

    public void render() {
        super.render();
    }
    public void dispose() {
        super.dispose();
    }
}
