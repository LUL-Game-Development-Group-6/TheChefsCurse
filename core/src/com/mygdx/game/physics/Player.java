package com.mygdx.game.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.misc.Coords;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



//testing stuff for the rectangle goes here
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Player extends DynamicObject {
    private int score;
	private Rectangle playerHitbox;
	private Sprite playerSprite;
	private Texture playerTexture;
	private boolean facingRight;
	
	

    public Player(float x, float y, float width, float height)
    {
		score = 0;
		playerTexture = new Texture("Chef_Still_Image.png");
		playerSprite = new Sprite(playerTexture);
		playerSprite.setSize(width, height);
		playerSprite.setPosition(x, y);
		playerHitbox = new Rectangle(x + width + 22, y, width, height);
		facingRight = true;
    }
	
	public Sprite getSprite()
	{
		return playerSprite;
	}
	
	public Rectangle getHitbox()
	{
			return playerHitbox;
	}
	
	public boolean getFace()
	{
			return facingRight;
	}
	public void setFace(boolean newDirection)
	{
			facingRight = newDirection;
	}
	
	
	public void testMove(float x, float y)
	{
		playerSprite.setPosition(playerSprite.getX() + x, playerSprite.getY() + y);
		playerHitbox.setPosition(playerHitbox.getX() + x, playerHitbox.getY() + y);
	}
	
	//more rectangle stuff
	public void renderHitbox(ShapeRenderer shapeRenderer){
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(playerHitbox.getX(), playerHitbox.getY(), playerHitbox.getWidth(), playerHitbox.getHeight());
		shapeRenderer.end();
	}
	
}
