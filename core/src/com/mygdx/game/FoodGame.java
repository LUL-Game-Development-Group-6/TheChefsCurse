package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.physics.Room.KitchenRoomFactory;
import com.mygdx.game.physics.Room.Room;
import com.mygdx.game.physics.Player;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;

public class FoodGame extends ApplicationAdapter 
{
	private Room currentRoom;
	private SpriteBatch batch;
	private Player player1;
	private float playerSize;
	
	@Override
	public void create () 
	{
		currentRoom = new KitchenRoomFactory().createRoomBuilder().build();
		batch = new SpriteBatch();
		player1 = new Player(400, 400, 56, 185);//first two are position. second two are for size of the hitbox
		playerSize = 200;
	}
	
	@Override
	public void render ()
	{
		float previousX = player1.getSprite().getX();
		float previousY = player1.getSprite().getY();
		
		float speed = 5;
		
		if (Gdx.input.isKeyPressed(Input.Keys.A)){
			player1.testMove(-speed, 0);
			if (player1.getFace()){
				player1.getSprite().flip(true, false);
				player1.setFace(false);
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)){
			player1.testMove(speed, 0);
			if (!player1.getFace()){
				player1.getSprite().flip(true, false);
				player1.setFace(true);
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)){
			player1.testMove(0, speed);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)){
			player1.testMove(0, -speed);
		}
		
		if (Intersector.intersectSegmentRectangle(85, 270, 630, 45, player1.getHitbox())){//this could all be much neater, but it will work for now.
			player1.testMove(previousX - player1.getSprite().getX(), previousY - player1.getSprite().getY());//moves the player back to previous position
			if (Gdx.input.isKeyPressed(Input.Keys.A)){//these bits aren't really needed but it means the player slides a bit on the wall if they keep trying to walk into it.
				player1.testMove(0, 1);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.S)){
				player1.testMove(1, 0);
			}
		}
		
		if (Intersector.intersectSegmentRectangle(1230, 290, 630, 45, player1.getHitbox())){
			player1.testMove(previousX - player1.getSprite().getX(), previousY - player1.getSprite().getY());
			if (Gdx.input.isKeyPressed(Input.Keys.D)){
				player1.testMove(0, 1);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.S)){
				player1.testMove(-1, 0);
			}
		}
		
		if (Intersector.intersectSegmentRectangle(1230, 0, 1230, 720, player1.getHitbox())){
			player1.testMove(previousX - player1.getSprite().getX(), previousY - player1.getSprite().getY());
		}
		if (Intersector.intersectSegmentRectangle(85, 0, 85, 720, player1.getHitbox())){
			player1.testMove(previousX - player1.getSprite().getX(), previousY - player1.getSprite().getY());
		}
		
		if (Intersector.intersectSegmentRectangle(1230, 480, 685, 700, player1.getHitbox())){
			player1.testMove(previousX - player1.getSprite().getX(), previousY - player1.getSprite().getY());
			if (Gdx.input.isKeyPressed(Input.Keys.D)){
				player1.testMove(0, -1);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.W)){
				player1.testMove(-1, 0);
			}
		}
		
		if (Intersector.intersectSegmentRectangle(85, 460, 685, 700, player1.getHitbox())){
			player1.testMove(previousX - player1.getSprite().getX(), previousY - player1.getSprite().getY());
			if (Gdx.input.isKeyPressed(Input.Keys.A)){
				player1.testMove(0, -1);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.W)){
				player1.testMove(1, 0);
			}
		}
		
		batch.begin();
		ScreenUtils.clear(0, 0, 0, 0);
		currentRoom.render(batch);
		batch.draw(player1.getSprite(), player1.getSprite().getX(), player1.getSprite().getY(), playerSize, playerSize);
		batch.end();
	}

	@Override
	public void dispose()
	{
		batch.dispose();
	}
}
