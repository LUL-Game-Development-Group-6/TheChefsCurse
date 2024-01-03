package com.mygdx.game.physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Enemy extends DynamicObject{
    public Enemy(float x, float y, float width, float height)
    {
        setSpeed(5);
        Texture playerTexture = new Texture("Chef_Still_Image.png");
        setTexture(playerTexture);

        Sprite playerSprite = new Sprite(playerTexture);
        playerSprite.setSize(width, height);
        playerSprite.setPosition(x, y);
        setSprite(playerSprite);

        setHitbox(new Rectangle(x + width + 22, y, width, height));
    }
}
