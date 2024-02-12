package com.mygdx.game.Room;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Room.Room.RoomType;
import com.mygdx.game.misc.Coords;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.mygdx.game.physics.DynamicObject;

public class Furniture extends DynamicObject
{
	private Rectangle furnitureHitbox;
	private Sprite furnitureSprite;
	private Texture furnitureTexture;
	private float xPosition;
	private float yPosition;
	private float width;
	private float height;
	
    public static enum FurnitureType {
        // Kitchen
        FRIDGE,
        STOVE,
      METAL_TABLE,
      CHAIR1,
      CHAIR2,
      // Freezer
      SINGLE_SHELF,
      DOUBLE_SHELF,
      LONG_SHELF,
      ICE,
      BOX,

      // Restaurant
      RESTAURANT_TABLE,
	}

	public Furniture(float xPos, float yPos, FurnitureType type)
	{

        switch (type) {
            case FRIDGE:
                furnitureTexture = new Texture("objects/Utensils/Fridge.png");
                width = 150;
                height = 300;
                break;

            case STOVE:
                furnitureTexture = new Texture("objects/Utensils/Stove.png");
                break;

            case METAL_TABLE:
                furnitureTexture = new Texture("objects/Metal_Table/Table_1.png");
                width = 1000;
                height = 700;
                break;

            case CHAIR1:
                furnitureTexture = new Texture("objects/Chairs/Chair_1.png");
                width = 400;
                height = 500;
                break;

            case CHAIR2:
                furnitureTexture = new Texture("objects/Chairs/Chair_2.png");
                width = 500;
                height = 400;
                break;

            case SINGLE_SHELF:
                furnitureTexture = new Texture("objects/freezer_shelves/shelf_1.png");
                width = 400;
                height = 700;

                break;
            case DOUBLE_SHELF:
                furnitureTexture = new Texture("objects/freezer_shelves/shelf_2.png");
                break;
            case LONG_SHELF:
                furnitureTexture = new Texture("objects/freezer_shelves/shelf_3.png");
                break;
            case ICE:
                furnitureTexture = new Texture("objects/ice/ice.png");
                width = 400;
                height = 350;
                break;
            case BOX:
                furnitureTexture = new Texture("objects/box/box.png");
                width = 600;
                height = 400;
                break;
            case RESTAURANT_TABLE:
                furnitureTexture = new Texture("objects/Restaurant_Table/restaurant_table.png");
                width = 370;
                height = 500;
                break;
            default:
                break;
        }
		
		furnitureSprite = new Sprite(furnitureTexture);
		furnitureSprite.setPosition(xPos, yPos);
		xPosition = xPos;
		yPosition = yPos;
		furnitureHitbox = new Rectangle(xPos, yPos, width, height / 10f);
	}

	
	public float getXPosition()
	{
		return xPosition;
	}
	
	public float getYPosition()
	{
		return yPosition;
	}
	
	public Rectangle getHitbox()
	{
		return furnitureHitbox;
	}
	
	public Sprite getSprite()
	{
		return furnitureSprite;
	}
	
	public void render(SpriteBatch batch)
	{
		batch.draw(furnitureSprite, xPosition, yPosition, width, height);
	}
}
