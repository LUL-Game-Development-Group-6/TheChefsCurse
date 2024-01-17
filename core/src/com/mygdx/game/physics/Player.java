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
	private Texture OverlayTexture;
	private Sprite OverlaySprite;
	// Change this to ENUM eventually
	private int weaponType;
	private Texture RedGunTexture;
	private Sprite RedGunSprite;

	private Texture ShotGunTexture;
	private Sprite ShotGunSprite;

	private Texture HandTexture;
	private Sprite HandSprite;

	/*
	 * 
	 * 
	 * 
	 */
	// PLAYER TEXTURES
	// Player static textures
	// Current sprite is the one used in the methods
	private Sprite playerSprite;
	private Texture playerTexture;
	private Sprite Sprite_UP;
	private Sprite Sprite_DOWN;

	private Texture currentTexture_UP;
	private Texture currentTexture_DOWN;

	private Sprite player_Standing_Sprite;
	private Texture playerTexture_Standing;

	// Red gun textures (Static Cheff with the Red Gun)
	private Texture playerTexture_RedGunDOWN;
	private Sprite player_RedGunDOWN_Sprite;
	
	private Texture playerTexture_RedGunUP;
	private Sprite player_RedGunUP_Sprite;
	//
	// Shotgun Textures (Static Cheff with the Shotgun)
	private Texture playerTexture_ShotgunDOWN;
	private Sprite player_ShotgunDOWN_Sprite;
	
	private Texture playerTexture_ShotgunUP;
	private Sprite player_ShotgunUP_Sprite;
	//
	// List that stores all static sprites
	private ArrayList<Sprite> allStatics;
	/*
	 * 
	 * 
	 * 
	 */
	// PLAYER ANIMATIONS
	// Current Animation (Variable will change)
	private TextureAtlas currentAtlas;
	private Animation<Sprite> currentAnimation;
	private Animation<Sprite> currentAnimation_UP;
	private Animation<Sprite> currentAnimation_DOWN;

	// Unarmed Animation Set
	private Animation<Sprite> walkingAnimation;
	private Animation<Sprite> standingAnimation_PUNCHING;

	// Red Gun Animation Set
	private Animation<Sprite> RedGunAnimation_DOWN;
	private Animation<Sprite> RedGunAnimation_UP;

	// Shotgun animation set
	private Animation<Sprite> ShotgunAnimation_DOWN;
	private Animation<Sprite> ShotgunAnimation_UP;

	private ArrayList<Animation<Sprite>> allAnimations;

	// Shotgun animation set

    public Player(float x, float y, float width, float height)
    {
		allAnimations = new ArrayList<Animation<Sprite>>();
		allStatics = new ArrayList<Sprite>();
		score = 0;
		setSpeed(2f);
		setJitter(2f);

		// Overlay
		OverlayTexture = new Texture("cheff/Weapon_Overlay.png");
		OverlaySprite = new Sprite(OverlayTexture);

		RedGunTexture = new Texture("cheff/weapons/RedGun.png");
		RedGunSprite = new Sprite(RedGunTexture);
		
		ShotGunTexture = new Texture("cheff/weapons/Shotgun.png");
		ShotGunSprite = new Sprite(ShotGunTexture);

		HandTexture = new Texture("cheff/weapons/Hand.png");
		HandSprite = new Sprite(HandTexture);

		// Chef sprites
		playerTexture_Standing = new Texture("cheff/Chef_Still_Image.png");
		player_Standing_Sprite = new Sprite(playerTexture_Standing);
		allStatics.add(player_Standing_Sprite);

		playerTexture_RedGunDOWN = new Texture("cheff/RedGun/RedGun_standing_DOWN.png");
		player_RedGunDOWN_Sprite = new Sprite(playerTexture_RedGunDOWN);
		allStatics.add(player_RedGunDOWN_Sprite);

		playerTexture_RedGunUP = new Texture("cheff/RedGun/RedGun_standing_UP.png");
		player_RedGunUP_Sprite = new Sprite(playerTexture_RedGunUP);
		allStatics.add(player_RedGunUP_Sprite);

		playerTexture_ShotgunUP = new Texture("cheff/Shotgun/Chef_with_shtopgun_standing_UP.png");
		player_ShotgunUP_Sprite = new Sprite(playerTexture_ShotgunUP);
		allStatics.add(player_ShotgunUP_Sprite);

		playerTexture_ShotgunDOWN = new Texture("cheff/Shotgun/Chef_with_shotgungun_standing_DOWN.png");
		player_ShotgunDOWN_Sprite = new Sprite(playerTexture_ShotgunDOWN);
		allStatics.add(player_ShotgunDOWN_Sprite);

		playerTexture = playerTexture_Standing;

		playerSize = 165;

		playerSprite = new Sprite(playerTexture);
		playerSprite.setSize(width, height);
		playerSprite.setPosition(x, y);
		setSprite(playerSprite);

		setHitbox(new Rectangle(x + width + 14, y, width, height));
		facingRight = true;
		create();
		setUnarmed();
		flipAnimationStanding(walkingAnimation);
		setCurrentHealth(100);
    }

	public void create() {

		currentAtlas = new TextureAtlas(Gdx.files.internal("cheff/Cheff_punching/standing_punching.atlas"));
		standingAnimation_PUNCHING = new Animation<Sprite>(
			1/7f,
			currentAtlas.createSprite("Chef_standing_punching5"));

		allAnimations.add(standingAnimation_PUNCHING);

		currentAtlas = new TextureAtlas(Gdx.files.internal("cheff/Shotgun/shotgun_up.atlas"));
		ShotgunAnimation_UP = new Animation<Sprite>(
			1/15f,
			currentAtlas.createSprite("Chef_with_shtopgun_standing_UP1"),
			currentAtlas.createSprite("Chef_with_shtopgun_standing_UP2"),
			currentAtlas.createSprite("Chef_with_shtopgun_standing_UP3"),
			currentAtlas.createSprite("Chef_with_shtopgun_standing_UP4"),
			currentAtlas.createSprite("Chef_with_shtopgun_standing_UP5"),
			currentAtlas.createSprite("Chef_with_shtopgun_standing_UP6"),
			currentAtlas.createSprite("Chef_with_shtopgun_standing_UP7"),
			currentAtlas.createSprite("Chef_with_shtopgun_standing_UP8"));

		allAnimations.add(ShotgunAnimation_UP);

		currentAtlas = new TextureAtlas(Gdx.files.internal("cheff/Shotgun/shotgun_down.atlas"));
		ShotgunAnimation_DOWN = new Animation<Sprite>(
			1/15f,
			currentAtlas.createSprite("Chef_with_shotgungun_standing_DOWN1"),
			currentAtlas.createSprite("Chef_with_shotgungun_standing_DOWN2"),
			currentAtlas.createSprite("Chef_with_shotgungun_standing_DOWN3"),
			currentAtlas.createSprite("Chef_with_shotgungun_standing_DOWN4"),
			currentAtlas.createSprite("Chef_with_shotgungun_standing_DOWN5"),
			currentAtlas.createSprite("Chef_with_shotgungun_standing_DOWN6"),
			currentAtlas.createSprite("Chef_with_shotgungun_standing_DOWN7"),
			currentAtlas.createSprite("Chef_with_shotgungun_standing_DOWN8"));

		allAnimations.add(ShotgunAnimation_DOWN);

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

	public void flipAnimationStanding(Animation<Sprite> animation) {
		for (TextureRegion frame : animation.getKeyFrames()) {
			frame.flip(true, false);
		}
	}

	public void flipTextures() {
		for (Sprite sprite : allStatics) {
			sprite.flip(true, false);
		}
	}

	public void flipAnimationDynamic (float cursorY, SpriteBatch batch) {
		// Added 80 to match the middle of the sprite of the player
		if (cursorY >= sprite.getY() + 80) {
			currentAnimation = currentAnimation_UP;
	
			playerSprite = Sprite_UP;
			playerSprite.setSize(getSprite().getWidth(), getSprite().getHeight());
			playerSprite.setPosition(getSprite().getX(), getSprite().getY());
			setSprite(playerSprite);

		}
		if (cursorY < sprite.getY() + 80) {
			currentAnimation = currentAnimation_DOWN;
			
			playerSprite = Sprite_DOWN;
			playerSprite.setSize(getSprite().getWidth(), getSprite().getHeight());
			playerSprite.setPosition(getSprite().getX(), getSprite().getY());
			setSprite(playerSprite);
		}

		if (Gdx.input.getX() >= hitbox.getX()) {
			if(!this.facingRight) {
				flipAnimation();
				flipTextures();
				this.setFace(true);
			}
		}
		if (Gdx.input.getX() < hitbox.getX()) {
			if(this.facingRight) {
				flipAnimation();
				flipTextures();
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
		batch.draw(OverlaySprite, 20, 530, OverlaySprite.getWidth()/2, OverlaySprite.getHeight()/2);
		renderWeapon(batch);

		// Boolean to display punching animation instead of standing
		boolean isPunching = false;

		flipAnimationDynamic(cursorY, batch);

		// Change weapon with number keys 1, 2 & 3
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
			setUnarmed();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
			setRedGun();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
			setShotgun();
		}

		// Shoot or punch handlere
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            isPunching = shoot(batch, game);
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

		if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A)
		|| Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D)) {

			batch.draw(this.getAnimation().getKeyFrame(game.getTimePassed(), true),
			getSprite().getX(), getSprite().getY(), playerSize, playerSize);

		} else if(!isPunching) {
			batch.draw(getSprite(), getSprite().getX(), getSprite().getY(), playerSize, playerSize);
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
	
	// Change weapon type methods
	public void setShotgun() {
		weaponType = 3;
		setSpeed(2f - 1);
		currentAnimation_UP = ShotgunAnimation_UP;
		currentAnimation_DOWN = ShotgunAnimation_DOWN;
		currentTexture_UP = playerTexture_ShotgunUP;
		currentTexture_DOWN = playerTexture_ShotgunDOWN;
		Sprite_UP = player_ShotgunUP_Sprite;
		Sprite_DOWN = player_ShotgunDOWN_Sprite;
	}
	public void setRedGun() {
		weaponType = 2;
		setSpeed(2f);
		currentAnimation_UP = RedGunAnimation_UP;
		currentAnimation_DOWN = RedGunAnimation_DOWN;
		currentTexture_UP = playerTexture_RedGunUP;
		currentTexture_DOWN = playerTexture_RedGunDOWN;
		Sprite_UP = player_RedGunUP_Sprite;
		Sprite_DOWN = player_RedGunDOWN_Sprite;
	}
	public void setUnarmed() {
		weaponType = 1;
		setSpeed(2f + 1);
		currentAnimation_UP = walkingAnimation;
		currentAnimation_DOWN = walkingAnimation;
		currentTexture_UP = playerTexture_Standing;
		currentTexture_DOWN = playerTexture_Standing;
		Sprite_UP = player_Standing_Sprite;
		Sprite_DOWN = player_Standing_Sprite;
	}


	// Methods that handle the player shooting/punching
	public boolean shoot(SpriteBatch batch, FoodGame game) {

		if(weaponType == 1) {
			if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A)
			|| Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D)) {
				return false;
			} else {
				batch.draw(standingAnimation_PUNCHING.getKeyFrame(game.getTimePassed(), true),
				getSprite().getX(), getSprite().getY(), playerSize, playerSize);
				return true;
			}
		}
		if(weaponType == 2) {
			return false;
		}
		if(weaponType == 3) {
			return false;	
		}

		return false;
	}

	// This method is for displaying the weapon in the top left overlay
	public void renderWeapon(SpriteBatch batch) {
		if(weaponType == 1) {
			batch.draw(HandSprite, 65, 565, HandSprite.getWidth()/2, HandSprite.getHeight()/2);
		}
		if(weaponType == 2) {
			batch.draw(RedGunSprite, 45, 570, RedGunSprite.getWidth()/2, RedGunSprite.getHeight()/2);
		}
		if(weaponType == 3) {	
			batch.draw(ShotGunSprite, 40, 585, ShotGunSprite.getWidth()/3, ShotGunSprite.getHeight()/3);
		}
	}


} // End of Player class

