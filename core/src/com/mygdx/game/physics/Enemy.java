package com.mygdx.game.physics;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Room.Room;
import com.mygdx.game.Screens.FoodGame;
import com.mygdx.game.Screens.Menu;
import com.mygdx.game.helpers.SoundPaths;

public class Enemy extends DynamicObject{
  
  private Vector2 position;
  private Vector2 velocity;
  private Vector2 bulletSpeed;

  private Vector2 previousSprite;
	private Vector2 previousPos;

  private float normalSpeed;


  private float height;
  private float width;
  private Texture enemyTexture;
  private int damage;
  private int offsetX;
  private int offsetY;

  private long lastShot;
  private long cooldown;
  private float lastHitObject;

  private int hitDistance;

  private TextureAtlas enemyAtlas;
  private Animation<Sprite> enemyAnimation;

  private LinkedList<Bullet> enemyAmmunition;
  
  private boolean isDead;
  private SpriteBatch enemyBatch;
  private Menu game;

  // Sound Effects
	SoundPaths soundPaths = SoundPaths.getInstance();
	private Sound damageSound = Gdx.audio.newSound(Gdx.files.internal(SoundPaths.ENEMYHIT_PATH));
	private Sound enemyShootSound = Gdx.audio.newSound(Gdx.files.internal(SoundPaths.ENEMYGUN_PATH));
  
  public static enum EnemyType{
    HAMBURGER,
    HOTDOG,
    POPCORN,
    SODA,
  }

  private EnemyType enemyType;

  public Enemy(Menu game, Vector2 position, float width, float height, EnemyType enemyType){
  
    super.createHealth();
    this.setPlayer(false);
    this.game = game;
    enemyAmmunition = new LinkedList<Bullet>();
    enemyBatch = new SpriteBatch();
    lastHitObject = 0;
	
    switch(enemyType){

      case HAMBURGER:
        this.damage = 10;
        this.hitDistance = 50;
        this.cooldown = 2000;
        offsetX = -25;
        offsetY = 20;
        enemyAtlas = new TextureAtlas("enemies/Hamburguer/hamburguer.atlas");
        enemyAnimation = new Animation<>(
        1/15f,
        enemyAtlas.createSprite("Hamburguer_Sprite1"),
        enemyAtlas.createSprite("Hamburguer_Sprite2"),
        enemyAtlas.createSprite("Hamburguer_Sprite3"),
        enemyAtlas.createSprite("Hamburguer_Sprite4"),
        enemyAtlas.createSprite("Hamburguer_Sprite5"),
        enemyAtlas.createSprite("Hamburguer_Sprite6"),
        enemyAtlas.createSprite("Hamburguer_Sprite7"),
        enemyAtlas.createSprite("Hamburguer_Sprite9"),
        enemyAtlas.createSprite("Hamburguer_Sprite9"),
        enemyAtlas.createSprite("Hamburguer_Sprite10"));


        enemyTexture = new Texture("enemies/Hamburguer/Hamburguer_Standing.png");
		    setCurrentHealth(100 +  game.getStatsHelper().getEnemyScaler());
        setSpeed(200);
        break;

      case HOTDOG:
        this.damage = 8;
        this.hitDistance = 1050;
        this.cooldown = 5000;
        offsetX = 20;
        offsetY = -10;
        enemyAtlas = new TextureAtlas("enemies/Hotdog/hotdog.atlas");
        enemyAnimation = new Animation<>(
        1/8f,
        enemyAtlas.createSprite("hotdog1"),
        enemyAtlas.createSprite("hotdog2"),
        enemyAtlas.createSprite("hotdog3"),
        enemyAtlas.createSprite("hotdog4"));

        enemyTexture = new Texture("enemies/Hotdog/hotdog_still.png");
		    setCurrentHealth(60 + game.getStatsHelper().getEnemyScaler());
        setSpeed(50);
        break;

      case POPCORN:
        this.damage = 5;
        this.hitDistance = 800;
        this.cooldown = 3000;
        offsetX = -20;
        offsetY = 20;
        enemyAtlas = new TextureAtlas("enemies/Popcorn/popcorn.atlas");
        enemyAnimation = new Animation<>(
          1/12f,
          enemyAtlas.createSprite("Popcorn1"),
          enemyAtlas.createSprite("Popcorn2"),
          enemyAtlas.createSprite("Popcorn3"),
          enemyAtlas.createSprite("Popcorn4"),
          enemyAtlas.createSprite("Popcorn5"));

        enemyTexture = new Texture("enemies/Popcorn/Popcorn_Standing.png");
		    setCurrentHealth(40 + game.getStatsHelper().getEnemyScaler());
        setSpeed(100);
        break;

      case SODA:
        this.damage = 15;
        this.hitDistance = 50;
        this.cooldown = 3000;
        offsetX = -20;
        offsetY = 20;
        enemyAtlas = new TextureAtlas("enemies/Soda/soda.atlas");
        enemyAnimation = new Animation<>(
          1/20f,
          enemyAtlas.createSprite("Soda_Sprite1"),
          enemyAtlas.createSprite("Soda_Sprite2"),
          enemyAtlas.createSprite("Soda_Sprite3"),
          enemyAtlas.createSprite("Soda_Sprite4"),
          enemyAtlas.createSprite("Soda_Sprite5"),
          enemyAtlas.createSprite("Soda_Sprite6"),
          enemyAtlas.createSprite("Soda_Sprite7"),
          enemyAtlas.createSprite("Soda_Sprite8"),
          enemyAtlas.createSprite("Soda_Sprite9"),
          enemyAtlas.createSprite("Soda_Sprite10"),
          enemyAtlas.createSprite("Soda_Sprite11"),
          enemyAtlas.createSprite("Soda_Sprite12"),
          enemyAtlas.createSprite("Soda_Sprite13"),
          enemyAtlas.createSprite("Soda_Sprite14"));


        enemyTexture = new Texture("enemies/Soda/Soda_Standing.png");

		    setCurrentHealth(100 + game.getStatsHelper().getEnemyScaler());
        setSpeed(150);
        break;

      default:
        break;
    }

    damage += game.getStatsHelper().getEnemyScaler();


	  this.isDead = false;
    this.setMaxHealth(this.getCurrentHealth());
    this.position = position;
    this.height = height;
    this.width = width;
    this.velocity = new Vector2(1, 1);
    this.lastShot = 0;
    this.enemyType = enemyType;
    this.setHit(false);

    normalSpeed = this.getSpeed();

    Sprite enemySprite = new Sprite(enemyTexture);
    enemySprite.setSize(width, height);
    enemySprite.setPosition(position.x, position.y);
    this.setHitbox(new Rectangle(0, 0, width - width/5, height - height/8));
    previousPos = new Vector2(this.getHitbox().x, this.getHitbox().y);
		previousSprite = new Vector2();
    bulletSpeed = new Vector2();
    
    setSprite(enemySprite);

  }


  public void update(float timePassed, float deltaTime, Player player, SpriteBatch batch, TiledMap map) {

    
    float distance = player.getPreviousPos().dst(position);

    // ENEMY ANIMATION
    // Draw enemy standing still if hes close enough
    if(distance < this.hitDistance) {
      setSpeed(0);
      batch.draw(enemyTexture, this.getSprite().getX(), this.getSprite().getY(), this.getWidth(), this.getHeight());
    } else {
      setSpeed(normalSpeed);
      batch.draw(this.getEnemyAnimation().getKeyFrame(timePassed, true),
      this.getSprite().getX(), this.getSprite().getY(), this.getWidth(), this.getHeight());

    }
    // draw enemie's health
    batch.draw(this.getHealthSprite(), this.getHitbox().getX() + offsetX , this.getHitbox().getY()
    + this.getHitbox().getHeight() + offsetY, this.getHealthSprite().getWidth()/2, this.getHealthSprite().getHeight()/2);
    this.healthPercentage();

    // Check enemy collision
    randomizeEnemyDirection(lastHitObject, player, deltaTime, map);

  }

  @Override
  public void render(float timePassed, float timeBetweenRenderCalls, SpriteBatch batch, Player player, FoodGame game, TiledMap map) {

    // Get previous position for colliders
		previousPos.set(this.getHitbox().x, this.getHitbox().y);
		previousSprite.set(this.getSprite().getX(), this.getSprite().getY());
    this.update(timePassed, timeBetweenRenderCalls, player, batch, map);

    for (Bullet bullet : enemyAmmunition) {
      bullet.update();
      bullet.renderEnemyBullet(batch, game);
      if (bullet.getHitbox().overlaps(player.getHitbox())) {
        player.setHit(true);
        player.setTimeHit();
        player.takeDamage(this.damage);
        bullet.setVisibility(false);
        this.getAmmunition().remove(bullet);
      }
      if(bullet.getDespawnTime() < System.currentTimeMillis()) {
        bullet.setVisibility(false);
        this.getAmmunition().remove(bullet);
      }
    }
  }

  @Override
  public void takeDamage(int damage)
  {
	  damageSound.play(soundPaths.getVolume());
	  this.setCurrentHealth(this.getCurrentHealth() - damage);
	  if(this.getCurrentHealth() <= 0){
		  damageSound.dispose();
		  enemyShootSound.dispose();
		  System.out.println("Enemy should die");
		  isDead = true;
	  }
  }
  

  public Vector2 getPosition() {
    return position;
  }

  @Override
  public Animation<Sprite> getAnimation() {
    return enemyAnimation;
  }

  public LinkedList<Bullet> getAmmunition() {
    return this.enemyAmmunition;
  }
  @Override
  public float getHeight() {
      return this.height;
  }
  @Override
  public float getWidth() {
      return this.width;
  }

  @Override
	public Vector2 getPreviousPos() {
		return this.previousPos;
	}

	@Override
	public Vector2 getPreviousSprite() {
		return this.previousSprite;
	}
  
  public void setIsDead(boolean isDead)
  {
	  this.isDead = isDead;
  }
  public boolean getIsDead()
  {
	  return isDead;
  }

  private void move(float x, float y)
	{
		sprite.setPosition(x - sprite.getWidth() / 2, y - sprite.getHeight() / 4);
		hitbox.setPosition(x - hitbox.getWidth() / 2, y - hitbox.getHeight() / 4);
	}

  @Override
	public void moveBack(Vector2 Hitbox, Vector2 spriteVector)
	{
		sprite.setPosition(spriteVector.x, spriteVector.y);
		hitbox.setPosition(Hitbox.x, Hitbox.y);
	}

  @Override
  public Animation<Sprite> getEnemyAnimation() {
    return this.enemyAnimation;
  }

  
  public void enemyHit(Player player) {

    long currentTime = System.currentTimeMillis();
    long timeSinceLastShot = currentTime - lastShot; 

    if(this.position.dst(player.getPreviousPos()) <= this.hitDistance && timeSinceLastShot >= this.cooldown) {

      switch (this.enemyType) {
        case HAMBURGER:
				  player.takeDamage(this.damage);
          player.setHit(true);
          player.setTimeHit();
				  break;
			  case SODA:
				  player.takeDamage(this.damage);
          player.setHit(true);
          player.setTimeHit();
				  break;
			  case POPCORN:
          enemyShootSound.play(soundPaths.getVolume());
          Bullet nextBullet1 = new Bullet(bulletSpeed.x, bulletSpeed.y, position.x, position.y + 50, Bullet.EnemyBullet.POPCORN_BULLET);
          nextBullet1.setSpeed(0.15f);
          nextBullet1.setDespawnTime(Bullet.EnemyBullet.HOTDOG_BULLET);
          enemyAmmunition.add(nextBullet1);
				  break;
			  case HOTDOG:
          enemyShootSound.play(soundPaths.getVolume());
				  Bullet nextBullet2 = new Bullet(bulletSpeed.x, bulletSpeed.y, position.x, position.y + 50, Bullet.EnemyBullet.HOTDOG_BULLET);
				  nextBullet2.setSpeed(0.2f);
				  nextBullet2.setDespawnTime(Bullet.EnemyBullet.HOTDOG_BULLET);
				  enemyAmmunition.add(nextBullet2);
				  break;
        default:
          break;
      }
      lastShot = currentTime;
    }
  }

  public void setBulletSpeed(Vector2 direction) {
    bulletSpeed.set(direction.x * normalSpeed, direction.y * normalSpeed);
  }

  public void randomizeEnemyDirection(float lastHitObject, Player player, float deltaTime, TiledMap map) {

    Vector2 enemyPlayerVector = new Vector2(player.getPreviousPos().x - position.x, player.getPreviousPos().y - position.y);
    enemyPlayerVector.nor();

    if(this.getCollided()) {
      adjustVelocity(enemyPlayerVector);
      if(isOutsideMap(map)) velocity.set(enemyPlayerVector.x * -speed*5, enemyPlayerVector.y * -speed*5);
    } else {

      /*
       * Case where enemy doesn't collide against anything, its direction will go towards player
       * Calculate the direction vector between the enemy and the player and normalize vector
       * 
       * Scale the direction by the speed to get the velocity
       * creating a copy of the direction vector to avoid progresively increase speed
       */

      velocity.set(enemyPlayerVector.x * speed, enemyPlayerVector.y * speed);
      setBulletSpeed(enemyPlayerVector);
    }
    // Update enemy position
    isOutsideMap(map);
    position.add(velocity.x * deltaTime, velocity.y * deltaTime);   
    move(position.x, position.y);
    enemyHit(player);
  }

  // With new AI logic, enemies sometimes jump out the map, this will teleport them back
  public boolean isOutsideMap(TiledMap map) {
    MapObjects colliderList = map.getLayers().get("colliders").getObjects();

    for(MapObject roomSpawn : colliderList) {
      if(roomSpawn instanceof PolygonMapObject) {
        Polygon triangleCollider = ((PolygonMapObject) roomSpawn).getPolygon();
        if(!triangleCollider.contains(this.getHitbox().getX(), this.getHitbox().getY())
        || !triangleCollider.contains(this.getHitbox().getX() + this.getHitbox().getWidth(), this.getHitbox().getY())) {
          this.moveBack(this.getPreviousPos(), this.getPreviousSprite());
          return true;
        }
      }
    }
    return false;
  }
  /*
  * Method that will return a different speed direction according to where the enemy is
  * w.r.t. the player 
  */
  public void adjustVelocity(Vector2 enemyPlayerVector) {

    if(enemyPlayerVector.x >= 0 && enemyPlayerVector.y >= 0) {
      velocity.set(enemyPlayerVector.x * -speed * 2, enemyPlayerVector.y * -speed);

    } else if (enemyPlayerVector.x < 0 && enemyPlayerVector.y >= 0) {
      velocity.set(enemyPlayerVector.x * -speed, enemyPlayerVector.y * -speed * 2);

    } else if (enemyPlayerVector.x < 0 && enemyPlayerVector.y < 0) {
      velocity.set(enemyPlayerVector.x * -speed * 5, enemyPlayerVector.y * -speed);
      
    } else if (enemyPlayerVector.x >= 0 && enemyPlayerVector.y  < 0) {
      velocity.set(enemyPlayerVector.x * -speed * 2, enemyPlayerVector.y * -speed);
    }
  }

  public Vector2 getVelocity() {
    return this.velocity;
  }
}