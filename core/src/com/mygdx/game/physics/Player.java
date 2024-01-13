package com.mygdx.game.physics;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;

//testing stuff for the rectangle goes here
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Player extends DynamicObject {
	private int score;
	private boolean facingRight;
	

    public Player(float x, float y, float width, float height)
    {
		score = 0;
		setSpeed(5);
		Texture playerTexture = new Texture("Chef_Still_Image.png");
		setTexture(playerTexture);

		Sprite playerSprite = new Sprite(playerTexture);
		playerSprite.setSize(width, height);
		playerSprite.setPosition(x, y);
		setSprite(playerSprite);

		setHitbox(new Rectangle(x + width + 22, y, width, height));
		facingRight = false;
    }
	
	public boolean getFace()
	{
			return facingRight;
	}
	public void setFace(boolean newDirection)
	{
			facingRight = newDirection;
	}

	/*
		Renders the player
	 */
	public void render()
	{
		float previousX = this.getSprite().getX();
		float previousY = this.getSprite().getY();

		if (Gdx.input.isKeyPressed(Input.Keys.A)){
			updateFace(speed, true);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.D)){
			updateFace(speed, false);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W)){
			move(0, speed);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.S)){
			move(0, -speed);
		}

		staticInterceptors(previousX, previousY);
	}

	/*
		Checks player's position for obstacles and prevents movement beyond them
		@Param float previuosX - previous player position in X dimension
		@Param float previuosY - previous player position in Y dimension
	 */
	private void staticInterceptors(float previousX, float previousY)
	{
		if (Intersector.intersectSegmentRectangle(85, 270, 630, 45, this.getHitbox())){//this could all be much neater, but it will work for now.
			move(previousX - this.getSprite().getX(), previousY - this.getSprite().getY());//moves the player back to previous position
			if (Gdx.input.isKeyPressed(Input.Keys.A)){
				//these bits aren't really needed but it means the player slides a bit on the wall if they keep trying to walk into it.
				move(0, 1);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.S)){
				move(1, 0);
			}
		}

		if (Intersector.intersectSegmentRectangle(1230, 290, 630, 45, this.getHitbox())){
			move(previousX - this.getSprite().getX(), previousY - this.getSprite().getY());
			if (Gdx.input.isKeyPressed(Input.Keys.D)){
				move(0, 1);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.S)){
				move(-1, 0);
			}
		}

		if (Intersector.intersectSegmentRectangle(1230, 0, 1230, 720, this.getHitbox())){
			move(previousX - this.getSprite().getX(), previousY - this.getSprite().getY());
		}
		if (Intersector.intersectSegmentRectangle(85, 0, 85, 720, this.getHitbox())){
			move(previousX - this.getSprite().getX(), previousY - this.getSprite().getY());
		}

		if (Intersector.intersectSegmentRectangle(1230, 480, 685, 700, this.getHitbox())){
			move(previousX - this.getSprite().getX(), previousY - this.getSprite().getY());
			if (Gdx.input.isKeyPressed(Input.Keys.D)){
				move(0, -1);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.W)){
				move(-1, 0);
			}
		}

		if (Intersector.intersectSegmentRectangle(85, 460, 685, 700, this.getHitbox())){
			move(previousX - this.getSprite().getX(), previousY - this.getSprite().getY());
			if (Gdx.input.isKeyPressed(Input.Keys.A)){
				move(0, -1);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.W)){
				move(1, 0);
			}
		}
	}

	/*
		Updated player's face direction (cap direction?)
		@Param float speed - numerical speed
		@Param boolean speedDirection - if true the speed is negative, if false positive
	 */
	private void updateFace(float speed, boolean speedDirection) {
		if (speedDirection != this.getFace()) {
			this.getSprite().flip(true, false);
			this.setFace(!this.getFace()); // Toggle face direction
		}
		
		this.move((speedDirection ? -speed : speed), 0);
	}
	
	
	/*
		Moves a player's texture with it's hitbox
		@Param float x - distance to move player horizontally
		@Param float y - distance to move player vertically
	 */
	private void move(float x, float y)
	{
		sprite.setPosition(sprite.getX() + x, sprite.getY() + y);
		hitbox.setPosition(hitbox.getX() + x, hitbox.getY() + y);
	}
}
