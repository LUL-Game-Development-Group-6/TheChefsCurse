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
import com.mygdx.game.Screens.Menu;
import com.mygdx.game.physics.DynamicObject;
import com.mygdx.game.physics.Player;

import com.badlogic.gdx.Gdx;
// Collider imports
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;


public class Room {
	// TODO: optimize
	// https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
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
	private RoomType roomType;

	private int pool_size;

	public void render(Player player1, OrthographicCamera camera)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.setMap(background);
		renderer.render();
		renderer.setView(camera);
	}

	
	public void create(RoomType roomType, Menu game) {

		background = new TiledMap();
		mapLoader = new TmxMapLoader();

		switch (roomType) {

			case KITCHEN_1:
				background = mapLoader.load("Tilemaps/Kitchens/Kitchen1/kitchen1.tmx");
				break;

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

		this.roomType = roomType;
		pool_size = 10 + game.getStatsHelper().getEnemyScaler();
		renderer = new OrthogonalTiledMapRenderer(background);

	}

	public RoomType getRoomType() {
		return this.roomType;
	}
	public void checkCollission(DynamicObject entity, TiledMap map) {

		MapObjects colliderList = map.getLayers().get("colliders").getObjects();
		MapObjects furnitureList = map.getLayers().get("furniture").getObjects();
	
		for(MapObject collider : colliderList) {

			// If collider is a triangle
			if(collider instanceof PolygonMapObject) {

				Polygon triangleCollider = ((PolygonMapObject) collider).getPolygon();
				
				if(!triangleCollider.contains(entity.getHitbox().x, entity.getHitbox().y) ||
				 !triangleCollider.contains(entity.getHitbox().x + entity.getHitbox().width, entity.getHitbox().y) || checkFurnitureCollission(entity, furnitureList)) {
					entity.moveBack(entity.getPreviousPos(), entity.getPreviousSprite());
					entity.setCollided(true);
				} else {
					entity.setCollided(false);
				}
			}
		}
	}
	// Returns a vector inside the map's collider coordinates
	public Vector2 entitySpawn(TiledMap map, float entityWidth) {

		MapObjects colliderList = map.getLayers().get("colliders").getObjects();
		MapObjects furnitureList = map.getLayers().get("furniture").getObjects();

		Vector2 spawn = new Vector2(0, 0);

		for(MapObject collider : colliderList) {

			if(collider instanceof PolygonMapObject) {

				Polygon triangleCollider = ((PolygonMapObject) collider).getPolygon();

				while (!triangleCollider.contains(spawn)
				|| !triangleCollider.contains(spawn.x + entityWidth, spawn.y) 
				|| checkFurnitureSpawn(spawn, furnitureList, entityWidth)) 
				
				{
					spawn.x = MathUtils.random(0, 10000);
					spawn.y = MathUtils.random(0, 10000);
				}
			}
		}
		return spawn;
	}

	public boolean checkFurnitureCollission(DynamicObject entity, MapObjects furnitureList) {

		for(MapObject furniture : furnitureList) {

			if(furniture instanceof PolygonMapObject) {

				Polygon furniturePolygon = ((PolygonMapObject) furniture).getPolygon();

				if(furniturePolygon.contains(entity.getHitbox().x, entity.getHitbox().y) ||
				furniturePolygon.contains(entity.getHitbox().x + entity.getHitbox().width, entity.getHitbox().y)) {
					return true;
				}
			}
		}
		return false;
	}


	public boolean checkFurnitureSpawn(Vector2 spawn, MapObjects furnitureList, float entityWidth) {

		for(MapObject furniture : furnitureList) {

			if(furniture instanceof PolygonMapObject) {
				
				Polygon furniturePolygon = ((PolygonMapObject) furniture).getPolygon();
				if(furniturePolygon.contains(spawn) || furniturePolygon.contains(spawn.x + entityWidth, spawn.y)) {
					return true;
				}
			}
		}
		return false;
	}

	public TiledMap getBackground() {
		return background;
	}
	public int getPoolSize() {
		return this.pool_size;
	}

	public TiledMap gTiledMap() {
		return this.background;
	}
}