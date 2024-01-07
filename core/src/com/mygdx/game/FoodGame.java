package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.physics.Room.factories.KitchenRoomFactory;
import com.mygdx.game.physics.Room.Room;
import com.mygdx.game.physics.Player;
import com.mygdx.game.physics.Enemy;
import com.badlogic.gdx.math.Vector2;

public class FoodGame extends ApplicationAdapter 
{
	private Room currentRoom;
	private SpriteBatch batch;
	private Player player1;
	private Enemy enemy;
	private float playerSize;
	private float time;
	
	@Override
	public void create () 
	{
		currentRoom = new KitchenRoomFactory().createRoomBuilder().build();
		batch = new SpriteBatch();
		player1 = new Player(400, 400, 56, 185);
		enemy = new Enemy(new Vector2(300,300), 56, 185);
		playerSize = 200;
	}
	
	@Override
	public void render ()
	{
		time += Gdx.graphics.getDeltaTime();
		batch.begin();
		player1.render();
		ScreenUtils.clear(0, 0, 0, 0);
		currentRoom.render(batch);
		

		batch.draw(player1.getSprite(), player1.getSprite().getX(), player1.getSprite().getY(), playerSize, playerSize);
		Vector2 playerPosition = new Vector2(player1.getSprite().getX(), player1.getSprite().getY());
		batch.draw(enemy.getSprite(), enemy.getSprite().getX(), enemy.getSprite().getY(), playerSize, playerSize);
		enemy.render(time, playerPosition);
		batch.end();
	}

	@Override
	public void dispose()
	{
		batch.dispose();
	}
}
