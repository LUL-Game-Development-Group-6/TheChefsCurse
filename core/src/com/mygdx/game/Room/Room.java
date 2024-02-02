package com.mygdx.game.Room;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.physics.Player;
import com.badlogic.gdx.Gdx;
// Collider imports
import com.badlogic.gdx.maps.MapObject;
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

	protected TiledMap background;
	protected TmxMapLoader mapLoader;
	protected OrthogonalTiledMapRenderer renderer;
	protected ShapeRenderer shapeRenderer;
	protected Vector2 spawnableCoords;


	public void render(Player player1, OrthographicCamera camera)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.setMap(background);
		renderer.render();
		renderer.setView(camera);
		checkCollission(player1, background);

	}

	public void create(RoomType roomType) {

		background = new TiledMap();
		mapLoader = new TmxMapLoader();

		switch (roomType) {

			case KITCHEN_1:
				background = mapLoader.load("Tilemaps/Kitchens/Kitchen1/kitchen1.tmx");
			case KITCHEN_2:
				background = mapLoader.load("Tilemaps/Kitchens/Kitchen2/kitchen2.tmx");
				break;
			case KITCHEN_3:
				background = mapLoader.load("Tilemaps/Kitchens/Kitchen3/kitchen3.tmx");
				break;
			case FREEZER_1:
				background = mapLoader.load("Tilemaps/Freezers/Freezer1/freezer1.tmx");
				break;
			case FREEZER_2:
				background = mapLoader.load("Tilemaps/Freezers/Freezer2/freezer2.tmx");
				break;
			case FREEZER_3:
				background = mapLoader.load("Tilemaps/Freezers/Freezer3/freezer3.tmx");
				break;	
			case  RESTAURANT_1:
				background = mapLoader.load("Tilemaps/Restaurants/Restaurant1/restaurant1.tmx");
				break;
			case  RESTAURANT_2:
				background = mapLoader.load("Tilemaps/Restaurants/Restaurant2/restaurant2.tmx");
				break;
			case RESTAURANT_3:
				background = mapLoader.load("Tilemaps/Restaurants/Restaurant3/restaurant3.tmx");
				break;
			default:
				break;
		}
		renderer = new OrthogonalTiledMapRenderer(background);
	}

	public void checkCollission(Player player1, TiledMap map) {

		MapObjects colliderList = map.getLayers().get("colliders").getObjects();
	
		for(MapObject collider : colliderList) {

			// If collider is a triangle
			if(collider instanceof PolygonMapObject) {

				Polygon triangleCollider = ((PolygonMapObject) collider).getPolygon();
				
				if(!triangleCollider.contains(player1.getHitbox().x, player1.getHitbox().y) || !triangleCollider.contains(player1.getHitbox().x + player1.getHitbox().width, player1.getHitbox().y)) {
					player1.moveBack(player1.getPreviousPos(), player1.getPreviousSprite());
				}
			}
		}
	}
	// Returns a vector inside the map's collider coordinates
	public Vector2 entitySpawn(TiledMap map) {

		MapObjects colliderList = map.getLayers().get("colliders").getObjects();
		Vector2 spawn = new Vector2(0, 0);

		for(MapObject collider : colliderList) {

			if(collider instanceof PolygonMapObject) {

				Polygon triangleCollider = ((PolygonMapObject) collider).getPolygon();

				do {
					spawn.x = MathUtils.random(0, 10000);
					spawn.y = MathUtils.random(0, 10000);
					
				} while (!triangleCollider.contains(spawn) && !triangleCollider.contains(spawn.x + 450/2, spawn.y));
			}
		}
		return spawn;
	}
	public TiledMap getBackground() {
		return background;
	}
}