package com.mygdx.game.Room;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Screens.Menu;
import com.mygdx.game.physics.DynamicObject;
import com.mygdx.game.physics.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
// Collider imports
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;

/**
 * Class of a game room
 *
 * Please see the {@link com.mygdx.game.Room.Room}
 * @author Gines Moratalla, Juozas Skarbalius
 *
 */
public class Room {
	public enum RoomType {
		
		KITCHEN_1 ("Tilemaps/Kitchens/Kitchen1/kitchen1.tmx"),
		KITCHEN_2 ("Tilemaps/Kitchens/Kitchen2/kitchen2.tmx"),
		KITCHEN_3 ("Tilemaps/Kitchens/Kitchen3/kitchen3.tmx"),

		RESTAURANT_1 ("Tilemaps/Restaurants/Restaurant1/restaurant1.tmx"),
		RESTAURANT_2 ("Tilemaps/Restaurants/Restaurant1/restaurant2.tmx"),
		RESTAURANT_3 ("Tilemaps/Restaurants/Restaurant1/restaurant3.tmx"),

		FREEZER_1 ("Tilemaps/Freezers/Freezer1/freezer1.tmx"),
		FREEZER_2 ("Tilemaps/Freezers/Freezer1/freezer2.tmx"),
		FREEZER_3 ("Tilemaps/Freezers/Freezer1/freezer3.tmx"),
		;

		private String path;

		RoomType(String path) {
			this.path = path;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}
	}
	protected TiledMap background;
	protected TmxMapLoader mapLoader;
	protected OrthogonalTiledMapRenderer renderer;
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

		background = mapLoader.load(roomType.getPath());

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
				
				if(!triangleCollider.contains(entity.getHitbox().x, entity.getHitbox().y)
				|| !triangleCollider.contains(entity.getHitbox().x + entity.getHitbox().width, entity.getHitbox().y)
				|| checkFurnitureCollission(entity, furnitureList)) {
					if(entity.getPlayerBool()) playerJitter(entity, map, triangleCollider, furnitureList);
					entity.setCollided(true);

				} else {
					entity.setCollided(false);
				}
			}
		}
	}
	// Roddys function (cleaned) to collide against walls
	public void playerJitter(DynamicObject entity, TiledMap map, Polygon triangleCollider, MapObjects furnitureList) {

		Player player = (Player) entity;
		float testJitter = player.getSpeed()/2;

		if( checkFurnitureCollission(entity, furnitureList)) {
			player.moveBack(player.getPreviousPos(), player.getPreviousSprite());

		} else if(!triangleCollider.contains(player.getHitbox().x, player.getHitbox().y)) {
			player.moveBack(player.getPreviousPos(), player.getPreviousSprite());

			if(Gdx.input.isKeyPressed(Input.Keys.S)) {
				player.move(player.getSpeed(), -testJitter);

			} else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
				player.move(player.getSpeed(), testJitter);

			} else if (Gdx.input.isKeyPressed(Input.Keys.A)) {

				if(triangleCollider.contains(player.getHitbox().x, player.getHitbox().y - 10)) {
					player.move(-testJitter * 2, -player.getSpeed());
				} else {
					player.move(-testJitter * 2, player.getSpeed());
				}

			}

		} else if (!triangleCollider.contains(player.getHitbox().x + player.getHitbox().width, player.getHitbox().y)) {

			player.moveBack(player.getPreviousPos(), player.getPreviousSprite());
			if(Gdx.input.isKeyPressed(Input.Keys.S)){
				player.move(-player.getSpeed(), -testJitter);

			}
			else if(Gdx.input.isKeyPressed(Input.Keys.W)) {				
				player.move(-player.getSpeed(), testJitter);
			}
			else if(Gdx.input.isKeyPressed(Input.Keys.D)){
				
				if (triangleCollider.contains(player.getHitbox().x + player.getHitbox().width, player.getHitbox().y - 10)){
					player.move(testJitter * 2, -player.getSpeed());
				}
				else {
					player.move(testJitter * 2, player.getSpeed());
				}
			}
		}
	}

	public Vector2 entitySpawn(TiledMap map, float entityWidth, int b) {

		MapObjects spawnlist = map.getLayers().get("spawn").getObjects();
	
		Vector2 spawn = new Vector2(0, 0);
		RectangleMapObject spawnArea = (RectangleMapObject) spawnlist.get(MathUtils.random(spawnlist.getCount() - 1));
		Rectangle rectangle = spawnArea.getRectangle();
	
		do {

			spawn.x = MathUtils.random(rectangle.x, rectangle.x + rectangle.width - entityWidth);
			spawn.y = MathUtils.random(rectangle.y, rectangle.y + rectangle.height);

		} while (!(rectangle.contains(spawn.x, spawn.y)));
	
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

	public TiledMap getBackground() {
		return background;
	}
	public int getPoolSize() {
		return this.pool_size;
	}
}