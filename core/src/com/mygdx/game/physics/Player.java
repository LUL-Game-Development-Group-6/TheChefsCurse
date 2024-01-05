package com.mygdx.game.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.misc.Coords;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;


//testing stuff for the rectangle goes here
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Player extends DynamicObject {

	// Player variables
    private int score;
	private Rectangle playerHitbox;
	private boolean facingRight;

	// Animation
	private Sprite playerSprite;
	private TextureAtlas walkingAtlas;
	private Animation<Sprite> animation;
	private Texture playerTexture;
	

    public Player(float x, float y, float width, float height)
    {	
		score = 0;
		playerTexture = new Texture("cheff/Chef_Still_Image.png");
		playerSprite = new Sprite(playerTexture);
		playerSprite.setSize(width, height);
		playerSprite.setPosition(x, y);
		playerHitbox = new Rectangle(x + width + 22, y, width, height);
		facingRight = true;
		
    }

	public void create() {

		walkingAtlas = new TextureAtlas(Gdx.files.internal("cheff/cheff_spritesheet.atlas"));

		animation = new Animation<Sprite>(
			1/15f,
			walkingAtlas.createSprite("Left1"),
			walkingAtlas.createSprite("Left2"),
			walkingAtlas.createSprite("Left3"),
			walkingAtlas.createSprite("Left4"),
			walkingAtlas.createSprite("Left5"),
			walkingAtlas.createSprite("Left6"),
			walkingAtlas.createSprite("Left7"),
			walkingAtlas.createSprite("Left8"));
			
		flipAnimation();	
	}

	public void flipAnimation() {
		for (TextureRegion frame : animation.getKeyFrames()) {
			frame.flip(true, false);
		}
	}

	public void dispose() {
		walkingAtlas.dispose();
	}
	
	public Animation<Sprite> getAnimation() {
		return animation;
	}
	public TextureAtlas getAtlas() {
		return walkingAtlas;
	}

	// Roddy's classes
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
