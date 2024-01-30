package com.mygdx.game.Room;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.physics.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
// Collider imports
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;


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


	public void render(Player player1, SpriteBatch batch, RoomType roomType, TiledMap map, OrthogonalTiledMapRenderer renderer)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		switch (roomType) {

			case KITCHEN_1:

				renderer.setMap(map);
				renderer.render();
				checkCollission(player1, map);
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

	public void checkCollission(Player player1, TiledMap map) {

		MapObjects colliderList = map.getLayers().get("colliders").getObjects();

	
		for(MapObject collider : colliderList) {

			// If collider is a rectangle
			if(collider instanceof RectangleMapObject) {
				Rectangle rectangleCollider = ((RectangleMapObject) collider).getRectangle();
				if(player1.getHitbox().overlaps(rectangleCollider)) {
					player1.moveBack(player1.getPreviousPos(), player1.getPreviousSprite());
				}
			}
			// If collider is a triangle
			if(collider instanceof PolygonMapObject) {

				Polygon triangleCollider = ((PolygonMapObject) collider).getPolygon();
				
				if(!triangleCollider.contains(player1.getHitbox().x, player1.getHitbox().y) || !triangleCollider.contains(player1.getHitbox().x + player1.getHitbox().width, player1.getHitbox().y)) {
					player1.moveBack(player1.getPreviousPos(), player1.getPreviousSprite());
				}
			}
		}
	}

	public boolean polygonCollission(Rectangle playerHitbox, Polygon triangle) {

		float[] vertices = triangle.getTransformedVertices();

		for (int i = 0; i < vertices.length; i += 2) {
			float x = vertices[i];
			float y = vertices[i + 1];
	
			if (playerHitbox.contains(x, y)) {
				return true;
			}
		}
		return false;

	}


	public void coordCollission(Player player1) {

		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			player1.move(0, 2f);
		}

	}

	public Texture getBackground()
	{
			return background;
	}
}