package com.mygdx.game.physics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends DynamicObject{
  
  private Vector2 position;
  private Vector2 velocity;
  private int type; // values 0 - 3, 0 is melee, 1 is ranged, 2 is boss, 3 is boss minion

  public Enemy(Vector2 position, float width, float height, int type)
  {
    switch(type){
      case 0:
        // melee enemy 1
        break;
      case 1:
        // melee enemy 2
        break;
      case 2:
        // ranged enemy 1
        break;
      case 3:
        // ranged enemy 2
        break;
      case 4:
        // boss enemy
        break;
      case 5:
        // boss minion
        break;
      default:  
        // default is melee enemy
        break;
    }
      this.position = position;
      this.velocity = new Vector2(1, 1);
      setSpeed(50);
      Texture enemyTexture = new Texture("enemies/Hamburguer/Hamburguer_Standing.png");
      setTexture(enemyTexture);

      Sprite enemySprite = new Sprite(enemyTexture);
      enemySprite.setSize(width, height);
      enemySprite.setPosition(position.x, position.y);
      setSprite(enemySprite);

      setHitbox(new Rectangle(position.x + width + 22, position.y, width, height));
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


  private void move(float x, float y)
	{
    // This works, but the sprite is not updated
    //System.out.println("Enemy position (x = " + x + " , y = " + y + ")");

		sprite.setPosition(x, y);
		hitbox.setPosition(x, y);
	}


    
    // make enemy move towards player, but ranged enemy should stop at a certain distance
    // note to juozas 

    
}
