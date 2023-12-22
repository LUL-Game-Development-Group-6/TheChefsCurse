package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.physics.Room;
import com.mygdx.game.physics.Player;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;

//testing stuff for the rectangle goes here
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class FoodGame extends ApplicationAdapter 
{
	private Room kitchen1;
	private SpriteBatch batch;
	private Player player1;
	private ShapeRenderer shapeRenderer;//remove this later
	private float playerSize;
	
	@Override
	public void create () 
	{
		shapeRenderer = new ShapeRenderer();//remove this later
		kitchen1 = new Room(Room.RoomType.KITCHEN_DEMO, false, 0);
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
		kitchen1.render(batch);
		batch.draw(player1.getSprite(), player1.getSprite().getX(), player1.getSprite().getY(), playerSize, playerSize);
		batch.end();
		
		
		/*
		//rectangle stuff. remove it all later.
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
		player1.renderHitbox(shapeRenderer);
		
		//stuff for working out where the lines are
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(1, 0, 0, 1);
		//shapeRenderer.line(85, 270, 1230, 270);
		//shapeRenderer.line(685, 0, 685, 720);
		//shapeRenderer.line(0, 45, 1280, 45);
		shapeRenderer.line(85, 270, 630, 45);//bottom left
		shapeRenderer.line(1230, 290, 630, 45);//bottom right
		shapeRenderer.line(85, 460, 685, 700);//top left
		shapeRenderer.line(1230, 480, 685, 700);//top right
		shapeRenderer.end();
		*/

	}
	@Override
	public void dispose()
	{
		batch.dispose();
		//shapeRenderer.dispose();//remove this later
	}


}
