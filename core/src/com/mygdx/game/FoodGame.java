package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.physics.Room;
import com.mygdx.game.physics.Player;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Intersector;


public class FoodGame implements Screen
{
	private Room kitchen1;
	private Player player1;
	private float playerSize;
	private float timePassed;

	final Menu game;
	public FoodGame(final Menu game) {
        this.game = game;
    }

    public void pause() {}
    public void resume() {}
    public void resize(int width, int height) {}
    public void hide() {}
	
	@Override
	public void show () 
	{
		kitchen1 = new Room(Room.RoomType.KITCHEN_DEMO, false, 0);
		game.batch = new SpriteBatch();
		player1 = new Player(400, 400, 56, 185);			//first two are position. second two are for size of the hitbox
		player1.create();
		playerSize = 200;
		
	}
	
	@Override
	public void render (float delta)
	{
		game.batch.begin();
		ScreenUtils.clear(0, 0, 0, 0);
		timePassed += Gdx.graphics.getDeltaTime();
		delta += Gdx.graphics.getDeltaTime();
		kitchen1.render(game.batch);
		drawPlayer();
		game.batch.end();
	}
	

	public void dispose()
	{
		game.batch.dispose();
		player1.dispose();
		
	}

	public void drawPlayer() {

		float previousX = player1.getSprite().getX();
		float previousY = player1.getSprite().getY();
		boolean isKeyPressed = false;
		float speed = 5;

		if (Gdx.input.isKeyPressed(Input.Keys.A)){
			player1.testMove(-speed, 0);
			if (player1.getFace()){
				player1.flipAnimation();
				player1.getSprite().flip(true, false);
				player1.setFace(false);
			}
			isKeyPressed = true;

		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)){
			player1.testMove(speed, 0);
			if (!player1.getFace()){
				player1.flipAnimation();
				player1.getSprite().flip(true, false);
				player1.setFace(true);
			}
			isKeyPressed = true;
		}

		/*
		 * Had to add condition to not be able to press W and S at the same time
		 * to avoid overlapping the animations
		 */
		if (Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S)){
			player1.testMove(0, speed);
			game.batch.draw(player1.getAnimation().getKeyFrame(timePassed, true),
			player1.getSprite().getX(), player1.getSprite().getY(),
			playerSize, playerSize);
			isKeyPressed = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.W)){
			player1.testMove(0, -speed);
			game.batch.draw(player1.getAnimation().getKeyFrame(timePassed, true),
			player1.getSprite().getX(), player1.getSprite().getY(),
			playerSize, playerSize);
			isKeyPressed = true;
		}

		/*
		 * Draw player animation if any key is pressed.
		 * Did this way so overlapping animations can be avoided.
		 */
		if(!isKeyPressed) {
			game.batch.draw(player1.getSprite(), player1.getSprite().getX(), player1.getSprite().getY(), playerSize, playerSize);
		} else {
			game.batch.draw(player1.getAnimation().getKeyFrame(timePassed, true),
			player1.getSprite().getX(), player1.getSprite().getY(),
			playerSize, playerSize);
		}

		// Room walkale limits
		
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
	}

}
