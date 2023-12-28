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
		player1.render();
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
