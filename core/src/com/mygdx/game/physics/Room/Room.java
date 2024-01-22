package com.mygdx.game.physics.Room;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;

public class Room
{
	public static enum RoomType{
		KITCHEN_DEMO,
	}

	Texture background;
	
	public void render(SpriteBatch batch)
	{
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());	
	}

	public Texture getBackground()
	{
			return background;
	}
}