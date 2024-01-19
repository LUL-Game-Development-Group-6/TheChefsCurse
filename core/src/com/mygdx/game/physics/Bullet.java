package com.mygdx.game.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.misc.Coords;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet extends DynamicObject
{
	private float speedModifier;
	private float xSpeed;
	private float ySpeed;
	private Rectangle bulletHitbox;
	private boolean visible;//if we are reusing the same bullets then we don't want all the bullets to be visible at the same time.
	
	private float xPosition;
	private float yPosition;
	
	private Sprite bulletSprite;
	private Texture bulletTexture;
	
	private int damage;
	
	public Bullet(float xS, float yS, float xPos, float yPos)
	{
		xSpeed = xS;
		ySpeed = yS;
		xPosition = xPos;
		yPosition = yPos;
		//bulletTexture = new Texture("bullet_sprite.png");
		bulletTexture = new Texture("objects/bullet/bullet.png");
		bulletSprite = new Sprite(bulletTexture);
		bulletHitbox = new Rectangle(xPos, yPos, 10, 10);
		visible = true;
		speedModifier = 2f;
		damage = 10;
	}
	
	public Sprite getSprite()
	{
		return bulletSprite;
	}
	
	
	public Rectangle getHitbox()
	{
		return bulletHitbox;
	}
	
	public boolean getVisibility()
	{
		return visible;
	}
	
	public void setVisibility(boolean newVisibility)
	{
		visible = newVisibility;
	}
	
	public double getSpeed()
	{
		return speedModifier;
	}
	
	public void setSpeed(float newSpeed)
	{
		speedModifier = newSpeed;
	}
	
	public float getXSpeed()
	{
		return xSpeed;
	}
	
	public float getYSpeed()
	{
		return ySpeed;
	}
	
	public void setSpeedVector(float x, float y)
	{
		xSpeed = x;
		ySpeed = y;
	}
	
	public float getXPosition()
	{
		return xPosition;
	}
	public float getYPosition()
	{
		return yPosition;
	}
	
	
	public void update()
	{
		xPosition = xPosition + (xSpeed * speedModifier);
		yPosition = yPosition + (ySpeed * speedModifier);

		hitbox.setPosition(xPosition, yPosition);
	}
	
	public void render(SpriteBatch batch)
	{
		batch.draw(bulletSprite, xPosition, yPosition, 10, 10);
	}
	
	public int getDamage()
	{
		return damage;
	}	
}