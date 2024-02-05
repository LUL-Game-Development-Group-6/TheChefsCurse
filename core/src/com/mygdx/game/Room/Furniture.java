package com.mygdx.game.Room;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.misc.Coords;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.mygdx.game.physics.DynamicObject;

public class Furniture extends DynamicObject
{
	private Rectangle furnitureHitbox;
	private Sprite furnitureSprite;
	private Texture furnitureTexture;
	private float xPosition;
	private float yPosition;
	private float width;
	private float height;
	
	public Furniture(float xPos, float yPos, float width, float height)//width and height should probably be handled differently, I just put them in now to demonstrate that the hitbox needs to be much smaller than the sprite itself
	{
		furnitureTexture = new Texture("objects/Chairs/Chair_1.png");//just so I can see it working. We can add other furnitures later.
		furnitureSprite = new Sprite(furnitureTexture);
		furnitureSprite.setPosition(xPos, yPos);
		xPosition = xPos;
		yPosition = yPos;
		this.width = width;
		this.height = height;
		furnitureHitbox = new Rectangle(xPos, yPos, width, height / 10f);
	}	
	
	public float getXPosition()
	{
		return xPosition;
	}
	
	public float getYPosition()
	{
		return yPosition;
	}
	
	public Rectangle getHitbox()
	{
		return furnitureHitbox;
	}
	
	public Sprite getSprite()
	{
		return furnitureSprite;
	}
	
	public void render(SpriteBatch batch)
	{
		batch.draw(furnitureSprite, xPosition, yPosition, width, height);//last two are for scale. Maybe need to change those for different furnitures, maybe not.
	}
}