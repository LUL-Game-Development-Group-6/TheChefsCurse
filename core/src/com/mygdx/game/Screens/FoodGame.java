package com.mygdx.game.Screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.physics.Player;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Room.Room;
import com.mygdx.game.Room.factories.KitchenRoomFactory;
import com.mygdx.game.physics.Bullet;
import com.mygdx.game.physics.DynamicObject;
import com.mygdx.game.physics.Enemy;
import java.util.Collections;
import java.util.Comparator;

public class FoodGame implements Screen
{
	private Room currentRoom;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Player player1;
	private Vector2 playerPosition;
	private Enemy enemy;
	private Enemy enemy2;

	private float timePassed;
    private float timeBetweenRenderCalls;
	// Boolean to check if the game has been paused
	private boolean pausedGameplay;

	// Game instance from Menu class to swtich between screens
	final Menu game;
	private Overlay overlay;

	private TiledMap map;
	private TmxMapLoader mapLoader;
	OrthogonalTiledMapRenderer renderer;
	ShapeRenderer shapeRenderer;


	// List of all the current enemies
	private ArrayList<Enemy> enemyList;
	private ArrayList<DynamicObject> entityList;

	public FoodGame(final Menu game) {

        this.game = game;
		this.enemyList = new ArrayList<>();
		this.entityList = new ArrayList<>();

		pausedGameplay = false;

		// Room and player creation
		currentRoom = new KitchenRoomFactory().createRoomBuilder().build();
		batch = new SpriteBatch();

		player1 = new Player(2077, 4015, 450, 500);
		createEnemies();
		entityList.add(player1);

		// Camera
		camera = new OrthographicCamera(2560,1440);

		// Temporarily holds 1 map, this will be changed
		map = new TiledMap();
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("Tilemaps/Kitchens/Kitchen1/kitchen1.tmx");
		renderer = new OrthogonalTiledMapRenderer(null);

		overlay = new Overlay();

		playerPosition = new Vector2(player1.getHitbox().getX(), player1.getHitbox().getY());

		shapeRenderer = new ShapeRenderer();

    }
	

    public void pause() {
		pausedGameplay = !pausedGameplay;
		if(pausedGameplay == false) {return;}
		game.setScreen(new Pause(game, this));
	}

    public void resume() {}
    public void resize(int width, int height) {}
    public void hide() {}
	public void show () {}
	public void dispose() {
		game.batch.dispose();
		player1.dispose();
		shapeRenderer.dispose();
	}

	public boolean getPaused() {
		return this.pausedGameplay;
	}

	public void setPaused(boolean flag) {
		this.pausedGameplay = flag;
	}

	@Override
	public void render (float delta)
	{
		// Handle Pausing the Game and showing Pause Screen
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) pause();
		if(pausedGameplay) return;

		// If player dies show death screen
		if(player1.getCurrentHealth() <= 0) {
			pausedGameplay = true;
			game.setScreen(new GameOver(game, this));
		}


		// Start rendering
		ScreenUtils.clear(0, 0, 0, 0);
        batch.begin();
		currentRoom.render(batch, Room.RoomType.KITCHEN_1, map, renderer);

		float centerX = player1.getHitbox().getX() + player1.getHitbox().getWidth() / 2;
		float centerY = player1.getHitbox().getY() + player1.getHitbox().getHeight() / 2;


		camera.position.set(centerX, centerY, 0);
		renderer.setView(camera);
		camera.update();

		// Times to get time passed and to follow the player vector
		timeBetweenRenderCalls = Gdx.graphics.getDeltaTime();
        timePassed += Gdx.graphics.getDeltaTime();

		// Get player position for enemies to track
        playerPosition.set(player1.getHitbox().getX(), player1.getHitbox().getY());
		renderEntities(playerPosition, timePassed, timeBetweenRenderCalls);
		// Render number of enemies left

		// Player's bullets
		for (Bullet bullet : player1.getAmmunition()){
			bullet.render(batch);
			for (Enemy currentEnemy : enemyList) {

				if (bullet.getHitbox().overlaps(currentEnemy.getHitbox())) {
					hitEnemy(enemy);
					bullet.setVisibility(false);
					System.out.println("Enemy Shot");
					currentEnemy.takeDamage(bullet.getDamage());
				}
			}

			if(bullet.getDespawnTime() < System.currentTimeMillis()) {
				bullet.setVisibility(false); 	
			}
		}
		
		//remove enemies from list
		for (int i = enemyList.size() - 1; i >= 0; i--){
			if (enemyList.get(i).getIsDead() == true){
				enemyList.remove(i);
			}
		}
		
		for (int i = entityList.size() - 1; i >= 0; i--){
			if (entityList.get(i) instanceof Enemy){
				Enemy temp = (Enemy)entityList.get(i);
				if (temp.getIsDead() == true){
					entityList.remove(i);
				}
			}
		}

		batch.setProjectionMatrix(camera.combined);
        batch.end();

		// Render overlay elements
		overlay.render(player1, entityList.size() - 1);

		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.rect(player1.getHitbox().getX(), player1.getHitbox().getY(), player1.getHitbox().getWidth(), player1.getHitbox().getHeight());
		shapeRenderer.end();
	}


	public float getTimePassed() {
		return timePassed;
	}

	public OrthographicCamera getCamera() {
		return this.camera;
	}

	public void hitEnemy(Enemy enemy) {
		enemy.getSprite().setColor(Color.WHITE);
	}

	// Method to be modified by Juozas random implementation of enemies
	public void createEnemies() {

		enemy = new Enemy(new Vector2(300,300), 36, 80, Enemy.EnemyType.HAMBURGER);
		enemy2 = new Enemy(new Vector2(700,300), 100, 50, Enemy.EnemyType.HOTDOG);

		enemyList.add(enemy);
		enemyList.add(enemy2);	

		entityList.add(enemy);
		entityList.add(enemy2);


	}

	// Method that renders all current entities w.r.t. their y position
	public void renderEntities(Vector2 playerPosition, float timePassed, float timeBetweenRenderCalls) {

    		
		Collections.sort(entityList, new Comparator<DynamicObject>() {

			public int compare(DynamicObject entity1, DynamicObject entity2) {
                return Double.compare(entity2.getSprite().getY(), entity1.getSprite().getY());
            }
		});

		for (DynamicObject entity : entityList) {

			if(entity.getPlayerBool()) {
				
				player1.render(batch, this, camera);

			} else {

				batch.draw(entity.getHealthSprite(), entity.getSprite().getX() - 10, entity.getSprite().getY() + 100, entity.getHealthSprite().getWidth()/5, entity.getHealthSprite().getHeight()/5);
				entity.healthPercentage();
	
				batch.draw(entity.getEnemyAnimation().getKeyFrame(timePassed, true),
				entity.getSprite().getX(), entity.getSprite().getY(), entity.getWidth(), entity.getHeight());
	
				entity.render(timeBetweenRenderCalls, playerPosition);
				entity.enemyHit(playerPosition, player1);

			}
		}

	}
}