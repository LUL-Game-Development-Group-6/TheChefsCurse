package com.mygdx.game.physics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.FoodGame;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import java.util.ArrayList;


public class Player extends DynamicObject {

	// Player variables
    private int score;
    private float jitter;
	private float playerSize;
	private boolean facingRight;
	private Sprite playerSprite;
	private Texture playerTexture;
	private Texture playerTexture_RedGunDOWN;
	private Texture playerTexture_RedGunUP;
	private Texture playerTexture_Standing;


	// Animation
	private TextureAtlas currentAtlas;
	private Animation<Sprite> currentAnimation;
	private Animation<Sprite> walkingAnimation;
	private Animation<Sprite> RedGunAnimation_DOWN;
	private Animation<Sprite> RedGunAnimation_UP;
	private ArrayList<Animation<Sprite>> allAnimations;

    public Player(float x, float y, float width, float height)
    {
		allAnimations = new ArrayList<Animation<Sprite>>();
		score = 0;
		setSpeed(2f);
		setJitter(2f);

		playerTexture_Standing = new Texture("cheff/Chef_Still_Image.png");
		playerTexture_RedGunDOWN = new Texture("cheff/RedGun/RedGun_standing_DOWN.png");
		playerTexture_RedGunUP = new Texture("cheff/RedGun/RedGun_standing_UP.png");
		playerTexture = playerTexture_Standing;
		setTexture(playerTexture);

		playerSize = 165;

		playerSprite = new Sprite(playerTexture);
		playerSprite.setSize(width, height);
		playerSprite.setPosition(x, y);
		setSprite(playerSprite);

		setHitbox(new Rectangle(x + width + 14, y, width, height));
		facingRight = true;
		create();
    }

	public void create() {

		currentAtlas = new TextureAtlas(Gdx.files.internal("cheff/RedGun/RedGun_DOWN.atlas"));
		RedGunAnimation_DOWN = new Animation<Sprite>(
			1/15f,
			currentAtlas.createSprite("Chef_Still_RedGun_DOWN1"),
			currentAtlas.createSprite("Chef_Still_RedGun_DOWN2"),
			currentAtlas.createSprite("Chef_Still_RedGun_DOWN3"),
			currentAtlas.createSprite("Chef_Still_RedGun_DOWN4"),
			currentAtlas.createSprite("Chef_Still_RedGun_DOWN5"),
			currentAtlas.createSprite("Chef_Still_RedGun_DOWN6"),
			currentAtlas.createSprite("Chef_Still_RedGun_DOWN7"),
			currentAtlas.createSprite("Chef_Still_RedGun_DOWN8"));

		allAnimations.add(RedGunAnimation_DOWN);

		currentAtlas = new TextureAtlas(Gdx.files.internal("cheff/RedGun/RedGun_UP.atlas"));
		RedGunAnimation_UP = new Animation<Sprite>(
			1/15f,
			currentAtlas.createSprite("Chef_Still_RedGun_UP1"),
			currentAtlas.createSprite("Chef_Still_RedGun_UP2"),
			currentAtlas.createSprite("Chef_Still_RedGun_UP3"),
			currentAtlas.createSprite("Chef_Still_RedGun_UP4"),
			currentAtlas.createSprite("Chef_Still_RedGun_UP5"),
			currentAtlas.createSprite("Chef_Still_RedGun_UP6"),
			currentAtlas.createSprite("Chef_Still_RedGun_UP7"),
			currentAtlas.createSprite("Chef_Still_RedGun_UP8"));

		allAnimations.add(RedGunAnimation_UP);

		currentAtlas = new TextureAtlas(Gdx.files.internal("cheff/cheff_spritesheet.atlas"));
		walkingAnimation = new Animation<Sprite>(
			1/15f,
			currentAtlas.createSprite("Left1"),
			currentAtlas.createSprite("Left2"),
			currentAtlas.createSprite("Left3"),
			currentAtlas.createSprite("Left4"),
			currentAtlas.createSprite("Left5"),
			currentAtlas.createSprite("Left6"),
			currentAtlas.createSprite("Left7"),
			currentAtlas.createSprite("Left8"));	
		
		allAnimations.add(walkingAnimation);

		currentAnimation = RedGunAnimation_UP;
	}

	public void flipAnimation() {
		for (Animation<Sprite> animation : allAnimations) {
			for (TextureRegion frame : animation.getKeyFrames()) {
				frame.flip(true, false);
			}
		}
	}

	public void flipAnimationDynamic (float cursorY, SpriteBatch batch) {
		// Added 80 to match the middle of the sprite of the player
		if (cursorY >= sprite.getY() + 80) {
			currentAnimation = RedGunAnimation_UP;
			playerTexture = playerTexture_RedGunUP;
			setTexture(playerTexture);
			playerSprite = new Sprite(playerTexture);
			playerSprite.setSize(getSprite().getWidth(), getSprite().getHeight());
			playerSprite.setPosition(getSprite().getX(), getSprite().getY());
			setSprite(playerSprite);

		}
		if (cursorY < sprite.getY() + 80) {
			currentAnimation = RedGunAnimation_DOWN;
			playerTexture = playerTexture_RedGunDOWN;
			setTexture(playerTexture);
			playerSprite = new Sprite(playerTexture);
			playerSprite.setSize(getSprite().getWidth(), getSprite().getHeight());
			playerSprite.setPosition(getSprite().getX(), getSprite().getY());
			setSprite(playerSprite);
		}

		if (Gdx.input.getX() >= hitbox.getX()) {

			if(!this.facingRight) {
				flipAnimation();
				this.getSprite().flip(true, false);
				this.setFace(true);
			}
			
		}
		if (Gdx.input.getX() < hitbox.getX()) {
			if(this.facingRight) {
				flipAnimation();
				this.getSprite().flip(true, false);
				this.setFace(false);
				
			}
		}

	}

	public void dispose() {
		currentAtlas.dispose();
	}

	public Animation<Sprite> getAnimation() {
		return currentAnimation;
	}
	public TextureAtlas getAtlas() {
		return currentAtlas;
	}

	public boolean getFace()
	{
		return facingRight;
	}
	public void setFace(boolean newDirection)
	{
		facingRight = newDirection;
	}

	public float getJitter() {
		return jitter;
	}

	public void setJitter(float jitter) {
		this.jitter = jitter;
	}

	/*
	 * Renders the player
	 */
	public void render(SpriteBatch batch, FoodGame game)
	{
		float previousX = this.getSprite().getX();
		float previousY = this.getSprite().getY();
		float cursorY = Gdx.graphics.getHeight() - Gdx.input.getY();

		flipAnimationDynamic(cursorY, batch);

		if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A)
		|| Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D)) {
			batch.draw(this.getAnimation().getKeyFrame(game.getTimePassed(), true),
			getSprite().getX(), getSprite().getY(), playerSize, playerSize);
		} else {
			batch.draw(getSprite(), getSprite().getX(), getSprite().getY(), playerSize, playerSize);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.A)){
			move(-speed, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)){
			move(speed, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)){
			move(0, speed);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			move(0, -speed);
		}
		/*
		 * Change the sprite if the player is pointing up or down
		 * Adding 80 to switch between animations when pointing + or - at the middle of the sprite
		 * I had to do it manually fo find the propper coordinates (may change later)
		 */
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
				move(0, jitter);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.S)){
				move(jitter, 0);
			}
		}

		if (Intersector.intersectSegmentRectangle(1230, 290, 630, 45, this.getHitbox())){
			move(previousX - this.getSprite().getX(), previousY - this.getSprite().getY());
			if (Gdx.input.isKeyPressed(Input.Keys.D)){
				move(0, jitter);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.S)){
				move(-jitter, 0);
			}
		}

		if (Intersector.intersectSegmentRectangle(1230, 0, 1230, 720, this.getHitbox())){
			move(previousX - this.getSprite().getX(), previousY - this.getSprite().getY());
		}
		if (Intersector.intersectSegmentRectangle(85, 0, 85, 720, this.getHitbox())){
			move(previousX - this.getSprite().getX(), previousY - this.getSprite().getY());
		}

		if (Intersector.intersectSegmentRectangle(1230, 445, 685, 665, this.getHitbox())){
			move(previousX - this.getSprite().getX(), previousY - this.getSprite().getY());
			if (Gdx.input.isKeyPressed(Input.Keys.D)){
				move(0, -jitter);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.W)){
				move(-jitter, 0);
			}
		}

		if (Intersector.intersectSegmentRectangle(85, 425, 685, 665, this.getHitbox())){
			move(previousX - this.getSprite().getX(), previousY - this.getSprite().getY());
			if (Gdx.input.isKeyPressed(Input.Keys.A)){
				move(0, -jitter);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.W)){
				move(jitter, 0);
			}
		}
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
