package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.physics.Room;

public class FoodGame extends ApplicationAdapter 
{
	private Room kitchen1;
	private SpriteBatch batch;
	
	
	@Override
	public void create () 
	{
		kitchen1 = new Room(Room.RoomType.KITCHEN_DEMO, false, 0);
		batch = new SpriteBatch();
	}
	@Override
	public void render ()
	{
		batch.begin();
		ScreenUtils.clear(0, 0, 0, 0);
		kitchen1.render(batch);
		batch.end();

	}
	@Override
	public void dispose()
	{
		batch.dispose();
	}


}
