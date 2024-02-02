package com.mygdx.game.physics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Screens.FoodGame;

public class Enemy extends DynamicObject{
  
  private Vector2 position;
  private Vector2 velocity;
  private float height;
  private float width;
  private Texture enemyTexture;
  private int damage;
  private int offsetX;
  private int offsetY;

  private long lastShot;
  private long cooldown;

  private int hitDistance;

  private TextureAtlas enemyAtlas;
  private Animation<Sprite> enemyAnimation;
  
  private boolean isDead;
  
  public static enum EnemyType{
    HAMBURGER,
    HOTDOG,
    POPCORN,
    SODA,
  }

  private EnemyType enemyType;

  public Enemy(Vector2 position, float width, float height, EnemyType enemyType){

    super.createHealth();
    this.setPlayer(false);
	
	
    switch(enemyType){

      case HAMBURGER: // Mele enemy
        this.damage = 10;
        this.hitDistance = 10;
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
		    setCurrentHealth(100);
        setSpeed(200);
        break;

      case HOTDOG:
        this.damage = 20;
        this.hitDistance = 200;
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
		    setCurrentHealth(60);
        setSpeed(50);
        break;

      case POPCORN:
        this.damage = 10;
        offsetX = -20;
        offsetY = 20;
        enemyTexture = new Texture("enemies/Popcorn/Popcorn.png");
		    setCurrentHealth(40);
        setSpeed(65);
        break;

      case SODA:
        this.damage = 15;
        offsetX = -20;
        offsetY = 20;
        enemyTexture = new Texture("enemies/Soda/Soda_Standing.png");
		    setCurrentHealth(70);
        setSpeed(60);
        break;

      default:
        break;
    }

	  this.isDead = false;
    this.setMaxHealth(this.getCurrentHealth());
    this.position = position;
    this.height = height;
    this.width = width;
    this.velocity = new Vector2(1, 1);
    this.lastShot = 0;

    Sprite enemySprite = new Sprite(enemyTexture);
    enemySprite.setSize(width, height);
    enemySprite.setPosition(position.x, position.y);
    
    setSprite(enemySprite);
    this.setHitbox(new Rectangle(0, 0, width - width/5, height - height/5));

  }


  public void update(float timePassed, float deltaTime, Vector2 playerPosition, SpriteBatch batch) {

      // draw enemy
      batch.draw(this.getHealthSprite(), this.getHitbox().getX() + offsetX , this.getHitbox().getY() 
      + this.getHitbox().getHeight() + offsetY, this.getHealthSprite().getWidth()/2, this.getHealthSprite().getHeight()/2);

      this.healthPercentage();

      batch.draw(this.getEnemyAnimation().getKeyFrame(timePassed, true),
      this.getSprite().getX(), this.getSprite().getY(), this.getWidth(), this.getHeight());


      // Calculate the direction vector between the enemy and the player
      Vector2 direction = new Vector2(playerPosition.x - position.x, playerPosition.y - position.y);

      // Normalize vector
      direction.nor();
      /*
       * Scale the direction by the speed to get the velocity
       * creating a copy of the direction vector to avoid progresively increase speed
       */
      velocity.set(direction.x * speed, direction.y * speed);
      // Update the enemy's position
      position.add(velocity.x * deltaTime, velocity.y * deltaTime);

      move(position.x, position.y);
  }

  @Override
  public void render(float timePassed, float timeBetweenRenderCalls, Vector2 playerPosition, SpriteBatch batch) {
    this.update(timePassed, timeBetweenRenderCalls, playerPosition, batch);
  }

  public Vector2 getPosition() {
      return position;
  }

  @Override
  public float getHeight() {
      return this.height;
  }
  @Override
  public float getWidth() {
      return this.width;
  }
  
  @Override//had to move this into enemy because it was more trouble than it was worth to try and change the one in dynamic object
  public void takeDamage(int damage)
  {
	  this.setCurrentHealth(this.getCurrentHealth() - damage);
	  if(this.getCurrentHealth() <= 0){
		  System.out.println("Enemy should die");
		  isDead = true;
	  }
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
  public Animation<Sprite> getEnemyAnimation() {
    return this.enemyAnimation;
  }

  @Override
  public void enemyHit(Vector2 playerPosition, Player player) {

    long currentTime = System.currentTimeMillis();

    long timeSinceLastShot = currentTime - lastShot; 

    if(this.position.dst(playerPosition) <= this.hitDistance) {

      if(timeSinceLastShot >= this.cooldown) {
        player.takeDamage(this.damage);	
        System.out.println("Player Hit by enemy");
        lastShot = currentTime;
      }
    }
  }
}
