package com.mygdx.game.Room;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.Gdx;

public class Room {


	public static enum RoomType {
		
		KITCHEN_1,
		KITCHEN_2,
		KITCHEN_3,

		RESTAURANT_1,
		RESTAURANT_2,
		RESTAURANT_3,

		FREEZER_1,
		FREEZER_2,
		FREEZER_3,
	}

	Texture background;


	public void render(SpriteBatch batch, RoomType roomType, TiledMap map, OrthogonalTiledMapRenderer renderer)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		switch (roomType) {

			case KITCHEN_1:

				renderer.setMap(map);
				renderer.render();
				break;
		
			case KITCHEN_2:
				batch.draw(background, 0, 0, Gdx.graphics.getWidth() * 3, Gdx.graphics.getHeight() * 2);
				break;

			case KITCHEN_3:
			batch.draw(background, 0, 0, Gdx.graphics.getWidth() * 3, Gdx.graphics.getHeight() * 2);
				break;
			

			case FREEZER_1:
				batch.draw(background, 0, 0, Gdx.graphics.getWidth() * 3, Gdx.graphics.getHeight() * 2);
				break;

			case FREEZER_2:
				batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				break;
		
			case FREEZER_3:
				batch.draw(background, 0, 0, Gdx.graphics.getWidth() * 3, Gdx.graphics.getHeight() * 2);
				break;

					
			case  RESTAURANT_1:
				batch.draw(background, 0, 0, Gdx.graphics.getWidth() * 3, Gdx.graphics.getHeight() * 2);
				break;

					
			case  RESTAURANT_2:
				batch.draw(background, 0, 0, Gdx.graphics.getWidth() * 3, Gdx.graphics.getHeight() * 2);
				break;

					
			case RESTAURANT_3:
				batch.draw(background, 0, 0, Gdx.graphics.getWidth() * 3, Gdx.graphics.getHeight() * 2);
				break;
				
			default:
				break;
		}	
	}



	public Texture getBackground()
	{
			return background;
	}
}