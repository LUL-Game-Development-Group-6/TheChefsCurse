package com.mygdx.game.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.misc.Coords;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;

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
	private Texture shotgunTexture_UP_RIGHT;
	private Texture shotgunTexture_UP_LEFT;
	private Texture shotgunTexture_DOWN_RIGHT;
	private Texture shotgunTexture_DOWN_LEFT;
	private Texture shotGunTexture;
	
	private int damage;
	private int weaponType;
	private long timeToDespawn;

	private float initialX;
	private float initialY;

	private Animation<Sprite> bulletAnimation;
	private long animationStart;
	
	public Bullet(float xS, float yS, float xPos, float yPos, int weaponType)
	{
		xSpeed = xS;
		ySpeed = yS;
		xPosition = xPos;
		yPosition = yPos;

		initialX = xPosition;
		initialY = yPosition;
		//bulletTexture = new Texture("bullet_sprite.png");
		bulletTexture = new Texture("objects/bullet/bullet.png");
		shotgunTexture_UP_RIGHT = new Texture("cheff/Shotgun/shotgun_bullet_UP_RIGHTt.png");
		shotgunTexture_UP_LEFT = new Texture("cheff/Shotgun/shotgun_bullet_UP_LEFT.png");
		shotgunTexture_DOWN_RIGHT = new Texture("cheff/Shotgun/shotgun_bullet_DOWN_RIGHT.png");
		shotgunTexture_DOWN_LEFT = new Texture("cheff/Shotgun/shotgun_bullet_DOWN_LEFT.png");
		bulletSprite = new Sprite(bulletTexture);
		bulletHitbox = new Rectangle(xPos, yPos, 10, 10);
		visible = true;
		speedModifier = 2f;
		damage = 10;
		this.weaponType = weaponType;
		
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

		bulletHitbox.setPosition(xPosition, yPosition);
	}
	
	public void render(SpriteBatch batch)
	{
		if(weaponType == 2) {
			batch.draw(bulletSprite, xPosition, yPosition, 15, 15);
		}
		if(weaponType == 3) {
			batch.draw(shotGunTexture, xPosition, yPosition, 20, 15);
		}
	}
	
	public int getDamage()
	{
		return damage;
	}	

	// Different range for different bullets
	public long getDespawnTime() {
		return this.timeToDespawn;
	}

	public void setDespawnTime(int weaponType) {
		switch (weaponType) {
			case 1:
				break;
			case 2:
				this.timeToDespawn = System.currentTimeMillis() + 200;
				this.damage = 18;
				break;
			case 3:
				this.timeToDespawn = System.currentTimeMillis() + 100;
				this.damage = 40;
				break;
			default:
				break;
		}
	}

	public int getWeapontype() {
		return this.weaponType;
	}
	public void setBulletAnimation(Animation<Sprite> anim) {
		this.bulletAnimation = anim;
	}

	public Animation<Sprite> getBulletAnimation() {
		return this.bulletAnimation;
	}

	public float getInitialX() {
		return this.initialX;
	}

	public float getInitialY() {
		return this.initialY;
	}

	public void setShotgunUP(boolean isUp, boolean isLeft) {
		if (isUp && isLeft) {
			shotGunTexture = shotgunTexture_UP_LEFT;
		} 
		if (isUp && !isLeft) {
			shotGunTexture = shotgunTexture_UP_RIGHT;
		}
		if (!isUp && isLeft) {
			shotGunTexture = shotgunTexture_DOWN_LEFT;
		}
		if (!isUp && !isLeft) {
			shotGunTexture = shotgunTexture_DOWN_RIGHT;
		}
	}

	public void setAnimationStart(long mins) {
		this.animationStart = mins;
	}

	public long getAnimationStart() {
		return this.animationStart;
	}
}
