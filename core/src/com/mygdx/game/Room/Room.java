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
 */
public class Room {
	/**
	 * Enum class for room type and it's textures
	 */
	public enum RoomType {
		KITCHEN_1 ("Tilemaps/Kitchens/Kitchen1/kitchen1.tmx"),
		KITCHEN_2 ("Tilemaps/Kitchens/Kitchen2/kitchen2.tmx"),
		KITCHEN_3 ("Tilemaps/Kitchens/Kitchen3/kitchen3.tmx"),

		RESTAURANT_1 ("Tilemaps/Restaurants/Restaurant1/restaurant1.tmx"),
		RESTAURANT_2 ("Tilemaps/Restaurants/Restaurant2/restaurant2.tmx"),
		RESTAURANT_3 ("Tilemaps/Restaurants/Restaurant3/restaurant3.tmx"),

		FREEZER_1 ("Tilemaps/Freezers/Freezer1/freezer1.tmx"),
		FREEZER_2 ("Tilemaps/Freezers/Freezer2/freezer2.tmx"),
		FREEZER_3 ("Tilemaps/Freezers/Freezer3/freezer3.tmx"),
		;

		private String path;

		RoomType(String path) {
			this.path = path;
		}

		public String getPath() {
			return path;
		}
	}
	private RoomType roomType;
	protected TiledMap background;
	protected TmxMapLoader mapLoader;
	protected OrthogonalTiledMapRenderer renderer;

	/**
	 * Max amount of enemies that can be spawned within current room
	 */
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

	/**
	 * <p>
	 *     Method that establishes room colliders (can be understood as boundaries of the room)
	 * </p>
	 * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-155">GD-155: [PHYSICS] Furniture is spawned randomly into existing maps and collision of furniture[M]</a>
	 * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-180">GD-180: [PHYSICS] [PHYSICS] Enhancing furniture collision [M]</a>
	 * @param entity - dynamic object to be checked for collision
	 * @param map - map of colliders
	 * @since 1.0
	 */
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

	/**
	 * <p>
	 *     Method that prevents player from going beyond room limits smoothly
	 * </p>
	 * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-79">GD-79: [PHYSICS] Make collision more smooth [S]</a>
	 * @param entity - dynamic object to be adjusted
	 * @param triangleCollider - specified triangle collider
	 * @param furnitureList list of furniture objects
	 * @since 1.0
	 */
	public void playerJitter(DynamicObject entity, TiledMap map, Polygon triangleCollider, MapObjects furnitureList) {

		Player player = (Player) entity;
		float testJitter = player.getSpeed()/2;

		if(checkFurnitureCollission(entity, furnitureList)) {
			// if dynamic object is colliding with furniture, smoothly knock back that object
			player.moveBack(player.getPreviousPos(), player.getPreviousSprite());
		} else if(!triangleCollider.contains(player.getHitbox().x, player.getHitbox().y)) {
			// if player collides with collider (ex. a wall), knock that player back a little
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

	/**
	 * <p>
	 *     Method that spawns objects on the room randomly (initially)
	 * </p>
	 * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-97">GD-97: [LOGIC] Spawn certain amount of enemies in a room [M]</a>
	 * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-95">GD-95: [LOGIC] Generate initial positions of dynamic objects randomly</a>
	 * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-85">GD-85: [PHYSICS] Implement custom room creation [L]</a>
	 * @param map map of the game objects
	 * @param entityWidth - wicth of a given entity
	 * @return position of entity
	 * @since 1.0
	 */
	public Vector2 entitySpawn(TiledMap map, float entityWidth, int b) {

		MapObjects spawnlist = map.getLayers().get("spawn").getObjects();
	
		Vector2 spawn = new Vector2(0, 0);
		RectangleMapObject spawnArea = (RectangleMapObject) spawnlist.get(MathUtils.random(spawnlist.getCount() - 1));
		Rectangle rectangle = spawnArea.getRectangle();

		// generate and apply random coords until they are within the boundaries of the room
		do {
			spawn.x = MathUtils.random(rectangle.x, rectangle.x + rectangle.width - entityWidth);
			spawn.y = MathUtils.random(rectangle.y, rectangle.y + rectangle.height);
		} while (!(rectangle.contains(spawn.x, spawn.y)));
	
		return spawn;
	}

	/**
	 * <p>
	 *     Method that checks for a given dynamic object is colliding with furniture objects
	 * </p>
	 * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-180">GD-180: https://lulgroupproject.atlassian.net/browse/GD-180</a>
	 * @param entity A Dynamic object that will be checked for furniture collision
	 * @param furnitureList A list of furniture objects
	 * @return true if there is a collision, false otherwise
	 * @since 1.0
	 */
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