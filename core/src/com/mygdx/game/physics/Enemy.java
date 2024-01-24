package com.mygdx.game.physics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends DynamicObject{
  
  private Vector2 position;
  private Vector2 velocity;
  private float height;
  private float width;
  private Texture enemyTexture;
  private int damage;

  private long lastShot;
  private long cooldown;

  private int hitDistance;

  private TextureAtlas enemyAtlas;
  private Animation<Sprite> enemyAnimation;
  

  
  public static enum EnemyType{
    HAMBURGER,
    HOTDOG,
    POPCORN,
    SODA,
  }

  private EnemyType enemyType;

  public Enemy(Vector2 position, float width, float height, EnemyType enemyType){

    super.createHealth();

    switch(enemyType){

      case HAMBURGER:
        this.damage = 20;
        this.hitDistance = 10;
        this.cooldown = 2000;
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
        setSpeed(40);
        break;

      case HOTDOG:
        this.damage = 25;
        this.hitDistance = 200;
        this.cooldown = 5000;
        enemyAtlas = new TextureAtlas("enemies/Hotdog/hotdog.atlas");
        enemyAnimation = new Animation<>(
        1/8f,
        enemyAtlas.createSprite("hotdog1"),
        enemyAtlas.createSprite("hotdog2"),
        enemyAtlas.createSprite("hotdog3"),
        enemyAtlas.createSprite("hotdog4"));

        enemyTexture = new Texture("enemies/Hotdog/hotdog_still.png");
		    setCurrentHealth(60);
        setSpeed(20);
        break;

      case POPCORN:
        this.damage = 10;
        enemyTexture = new Texture("enemies/Popcorn/Popcorn.png");
		    setCurrentHealth(40);
        setSpeed(65);
        break;

      case SODA:
        this.damage = 15;
        enemyTexture = new Texture("enemies/Soda/Soda_Standing.png");
		    setCurrentHealth(70);
        setSpeed(60);
        break;

      default:
        this.damage = 20;
        enemyTexture = new Texture("enemies/Hamburguer/Hamburguer_Standing.png");
        setCurrentHealth(100);
        setSpeed(50);

        break;
    }

    this.setMaxHealth(this.getCurrentHealth());
    this.position = position;
    this.height = enemyTexture.getHeight()/6;
    this.width = enemyTexture.getWidth()/6;
    this.velocity = new Vector2(1, 1);
    this.lastShot = 0;

    Sprite enemySprite = new Sprite(enemyTexture);
    enemySprite.setSize(width, height);
    enemySprite.setPosition(position.x + 100, position.y);
    
    setSprite(enemySprite);
    setHitbox(new Rectangle(position.x, position.y, width, height));

  }


  public void update(float deltaTime, Vector2 playerPosition) {
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
  
  public void render(float deltaTime, Vector2 playerPosition){
    update(deltaTime, playerPosition);
  }

  public Vector2 getPosition() {
      return position;
  }

  public float getHeight() {
      return this.height;
  }
  public float getWidth() {
      return this.width;
  }


  private void move(float x, float y)
	{
    // This works, but the sprite is not updated
    //System.out.println("Enemy position (x = " + x + " , y = " + y + ")");

		sprite.setPosition(x - sprite.getWidth() / 2, y - sprite.getHeight() / 4);
		hitbox.setPosition(x, y);
	} 
    // make enemy move towards player, but ranged enemy should stop at a certain distance
    // note to juozas 

  public Animation<Sprite> getEnemyAnimation() {
    return this.enemyAnimation;
  }

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
