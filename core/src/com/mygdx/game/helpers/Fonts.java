package com.mygdx.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;


public class Fonts {

    private FreeTypeFontGenerator myFont;

    public Fonts() {
        myFont = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixelmix_bold.ttf"));
    }

    public BitmapFont getFont(Color color, int size, int border) {
        FreeTypeFontParameter values = new FreeTypeFontParameter();
        values.size = size;
        values.borderColor = Color.BLACK;
        values.borderWidth = border;
        values.color = color;
        
        return this.myFont.generateFont(values);
    }

    public void dispose() {
        this.myFont.dispose();
    }

    
}
