package com.mygdx.game.helpers;

import org.w3c.dom.Text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.physics.Enemy;

public class XpAnimationHelper {

    private TextureAtlas atlas10 = new TextureAtlas(Gdx.files.internal("xp/healthxp.atlas"));
    private TextureAtlas atlas50 = new TextureAtlas(Gdx.files.internal("xp/50xp.atlas"));
    private Animation<Sprite> tenXP;
    private Animation<Sprite> fiftyXP;


    public XpAnimationHelper() {
        createXP();
    }

    public void createXP() {

        Sprite[] sprites = new Sprite[21];


        for (int i = 0; i < 21; i++) {
            sprites[i] = atlas10.createSprite("10xp" + (i + 1));
        }
	    tenXP = new Animation<Sprite>(1/25f, sprites);   
        
        Sprite[] sprites50 = new Sprite[16];
        for (int i = 0; i < 16; i++) {
            sprites50[i] = atlas50.createSprite("50xp" + (i + 1));
        }
	    fiftyXP = new Animation<Sprite>(1/25f, sprites50);   

    }

    public void create50xp() {

    }

    public Animation<Sprite> get10xp() {
        return tenXP;
    }

    public Animation<Sprite> get50xp() {
        return fiftyXP;
    }

}

