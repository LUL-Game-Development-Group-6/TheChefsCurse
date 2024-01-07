package com.mygdx.game.physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends DynamicObject{
  private Vector2 position;
  private Vector2 velocity;

  public Enemy(Vector2 position, float width, float height)
  {
      this.position = position;
      this.velocity = new Vector2(-20000, 10000);
      setSpeed(2);
      Texture playerTexture = new Texture("Chef_Still_Image.png");
      setTexture(playerTexture);

      Sprite playerSprite = new Sprite(playerTexture);
      playerSprite.setSize(width, height);
      playerSprite.setPosition(position.x, position.y);
      setSprite(playerSprite);

      setHitbox(new Rectangle(position.x + width + 22, position.y, width, height));
  }


  public void update(float deltaTime, Vector2 playerPosition) {
      // Calculate the direction vector between the enemy and the player
      Vector2 direction = new Vector2(playerPosition.x - position.x, playerPosition.y - position.y);

      // Normalize the direction vector (to get a unit vector)
      direction.nor();

      // Scale the direction by the speed to get the velocity
      velocity.set(direction.scl(speed));

      // Update the enemy's position
      position.add(velocity.x * deltaTime, velocity.y * deltaTime);
  }
  
  public void render(float deltaTime, Vector2 playerPosition){
    update(deltaTime, playerPosition);
  }

  public Vector2 getPosition() {
      return position;
  }

    
    // make enemy move towards player, but ranged enemy should stop at a certain distance
    // note to juozas 

    
}
