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
	private Rectangle playerHitbox;
	private Sprite playerSprite;
	private Texture playerTexture;
	private boolean facingRight;
	private static float speed = 5;

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
			this.testMove(0, speed);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.S)){
			this.testMove(0, -speed);
		}

		staticInterceptors(previousX, previousY);
	}

	private void staticInterceptors(float previousX, float previousY)
	{
		if (Intersector.intersectSegmentRectangle(85, 270, 630, 45, this.getHitbox())){//this could all be much neater, but it will work for now.
			this.testMove(previousX - this.getSprite().getX(), previousY - this.getSprite().getY());//moves the player back to previous position
			if (Gdx.input.isKeyPressed(Input.Keys.A)){
				//these bits aren't really needed but it means the player slides a bit on the wall if they keep trying to walk into it.
				this.testMove(0, 1);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.S)){
				this.testMove(1, 0);
			}
		}

		if (Intersector.intersectSegmentRectangle(1230, 290, 630, 45, this.getHitbox())){
			this.testMove(previousX - this.getSprite().getX(), previousY - this.getSprite().getY());
			if (Gdx.input.isKeyPressed(Input.Keys.D)){
				this.testMove(0, 1);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.S)){
				this.testMove(-1, 0);
			}
		}

		if (Intersector.intersectSegmentRectangle(1230, 0, 1230, 720, this.getHitbox())){
			this.testMove(previousX - this.getSprite().getX(), previousY - this.getSprite().getY());
		}
		if (Intersector.intersectSegmentRectangle(85, 0, 85, 720, this.getHitbox())){
			this.testMove(previousX - this.getSprite().getX(), previousY - this.getSprite().getY());
		}

		if (Intersector.intersectSegmentRectangle(1230, 480, 685, 700, this.getHitbox())){
			this.testMove(previousX - this.getSprite().getX(), previousY - this.getSprite().getY());
			if (Gdx.input.isKeyPressed(Input.Keys.D)){
				this.testMove(0, -1);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.W)){
				this.testMove(-1, 0);
			}
		}

		if (Intersector.intersectSegmentRectangle(85, 460, 685, 700, this.getHitbox())){
			this.testMove(previousX - this.getSprite().getX(), previousY - this.getSprite().getY());
			if (Gdx.input.isKeyPressed(Input.Keys.A)){
				this.testMove(0, -1);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.W)){
				this.testMove(1, 0);
			}
		}
	}

	private void updateFace(float speed, boolean speedDirection)
	{
		this.testMove((speedDirection ? -speed : speed), 0);
		if (this.getFace()) {
			this.getSprite().flip(true, false);
			this.setFace(false);
		} else {
			this.getSprite().flip(true, false);
			this.setFace(true);
		}
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
