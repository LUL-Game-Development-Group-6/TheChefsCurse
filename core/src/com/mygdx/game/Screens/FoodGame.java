package com.mygdx.game.Screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.Room.RoomBuilder;
import com.mygdx.game.gamesave.GameSaveLoader;
import com.mygdx.game.helpers.AnimationParameters;
import com.mygdx.game.helpers.ShadersHelper;
import com.mygdx.game.physics.Player;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.game.Room.Furniture;
import com.mygdx.game.Room.FurnitureBuilder;
import com.mygdx.game.Room.Room;
import com.mygdx.game.physics.Bullet;
import com.mygdx.game.physics.DynamicObject;
import com.mygdx.game.physics.EnemiesGenerator;
import com.mygdx.game.physics.Enemy;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.mygdx.game.helpers.SoundPaths;

public class FoodGame implements Screen
{
	/*
	 * LibGDX Objects 
	 */
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	/*
	* Native Objects
	 */
	private Menu game;
	private Overlay overlay;
	private Room currentRoom;
	/*
	 * Helpers
	 * & Builders
	 */
	private ShadersHelper shadersHelper;
	private EnemiesGenerator enemiesGenerator;
	private FurnitureBuilder furnitureBuilder;

	private Player player1;
	private float timePassed;
	private boolean pausedGameplay;

	/*
	 * Lists
	 */
	private ArrayList<Object> entityList;
	private ArrayList<AnimationParameters> xpList;

	// Sound Effects
	private Sound enemyDies = Gdx.audio.newSound(Gdx.files.internal(SoundPaths.ENEMYDEAD_PATH));
	public Music gameMusic = Gdx.audio.newMusic(Gdx.files.internal(SoundPaths.MUSIC_PATH));
	public SoundPaths soundPaths = SoundPaths.getInstance();
	

	public FoodGame(Menu game) {

        this.game = Menu.getInstance();
		this.entityList = new ArrayList<>();
		this.xpList = new ArrayList<>();

		shadersHelper = new ShadersHelper();
		furnitureBuilder = new FurnitureBuilder();
		pausedGameplay = false;
		batch = new SpriteBatch();

		// Random Room from the 9 choices
		currentRoom = RoomBuilder.init().withRandomRoomType().create(game).get();

		Vector2 spawn = currentRoom.entitySpawn(currentRoom.getBackground(), 450, 0);
		player1 = new Player(spawn.x, spawn.y, 450, 500, game);
		entityList.add(player1);

		// Add music settings
		gameMusic.setLooping(true);
		gameMusic.setVolume(soundPaths.getMusicVolume());

		// Camera & Overlay
		camera = new OrthographicCamera(2560,1440);
		overlay = new Overlay(this, gameMusic);

		// Initializing Helpers
		shapeRenderer = new ShapeRenderer();
		enemiesGenerator = new EnemiesGenerator(entityList, currentRoom, game);
		furnitureBuilder.create(currentRoom.getRoomType(), entityList);
		enemiesGenerator.generate();

    }

    public void pause() {
		pausedGameplay = !pausedGameplay;
		if(!pausedGameplay) return;
		gameMusic.pause();
		game.setScreen(new Pause(game, this));
	}

    public void resume() {}

    public void resize(int width, int height) {}

    public void hide() {
		Gdx.input.setInputProcessor(null);
	}
	public void show () {
		gameMusic.play();
	}

	public void dispose() {
		game.batch.dispose();
		player1.dispose();
		shapeRenderer.dispose();
		entityList = null;
		gameMusic.stop();
		gameMusic.dispose();
		enemyDies.dispose();
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
		currentRoom.render(player1, camera);

		// Moving the camera
		float centerX = player1.getHitbox().getX() + player1.getHitbox().getWidth() / 2;
		float centerY = player1.getHitbox().getY() + player1.getHitbox().getHeight() / 2;
		camera.position.set(centerX, centerY, 0);
		camera.update();

		// Times to get current delta and to follow the player vector for the enemy
		float timeBetweenRenderCalls = Gdx.graphics.getDeltaTime();
        timePassed += Gdx.graphics.getDeltaTime();

		// Get and render next batch of random enemies
		enemiesGenerator.getNextBatchOfEnemies(timeBetweenRenderCalls);
		renderEntities(player1, timePassed, timeBetweenRenderCalls);

		// Player's bullets
		for (Bullet bullet : player1.getAmmunition()) {
			bullet.update();
			bullet.render(batch);

			for (Object entity : entityList) {

				if(!(entity instanceof Enemy)) continue;
				Enemy currentEnemy = (Enemy) entity;

				if (bullet.getHitbox().overlaps(currentEnemy.getHitbox())) {

					currentEnemy.setHit(true);
					currentEnemy.setTimeHit();
					bullet.setVisibility(false);
					System.out.println("Enemy Shot");
					currentEnemy.takeDamage(bullet.getDamage());
					player1.getAmmunition().remove(bullet);
				}
			}
			if(bullet.getDespawnTime() < System.currentTimeMillis()) {
				bullet.setVisibility(false); 	
				player1.getAmmunition().remove(bullet);
			}
		}

		// Dead enemies (create XP animation)
		for (int i = entityList.size() - 1; i >= 0; i--){
			if (entityList.get(i) instanceof Enemy){
				Enemy temp = (Enemy)entityList.get(i);
				if (temp.getIsDead()) {
					enemyDies.play(soundPaths.getVolume());
					// CreateXP Animation
					AnimationParameters animation = new AnimationParameters(
						game.getXpAnimationHelper().get10xp(),
						temp.getHitbox().getX() + temp.getHitbox().getWidth()/2,
						temp.getHitbox().getY() + temp.getHitbox().getHeight()/2,
						System.currentTimeMillis()
					);
					
					xpList.add(animation);
					game.getXpAnimationHelper().get10xp();

					entityList.remove(temp);
					enemiesGenerator.enemyKilled();
					game.increaseXP();

				}
			}
		}
		
		// Draw xp animation if an enemy is killed
		Iterator<AnimationParameters> iterator = xpList.iterator();
		while (iterator.hasNext()) {
    		AnimationParameters params = iterator.next();
    		long elapsedTime = System.currentTimeMillis() - params.getTimeStarted();
    		if (elapsedTime < 500) {
        		batch.draw(params.geAnimation().getKeyFrame(timePassed, true),
                params.getX(), params.getY(), 200, 170);
    		} else {
				System.out.println("Bingo");
        		iterator.remove();
    		}
		}	
        batch.end();
		
		
		// Here is where hitboxes are rendered, this will eventually be deleted
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.setProjectionMatrix(camera.combined);
		// Render entity hitboxes
		// for(Object entity_ : entityList) {
		// 	DynamicObject entity = (DynamicObject) entity_;
		// 	shapeRenderer.rect(entity.getHitbox().getX(), entity.getHitbox().getY(), entity.getHitbox().getWidth(), entity.getHitbox().getHeight());
		// }
		shapeRenderer.end();
		
		GameSaveLoader.getInstance()
			.health(player1.getCurrentHealth())
			.enemiesLeft(enemiesGenerator.getEnemiesLeft())
			.withTimestamp(System.currentTimeMillis())
			.update();

		// Render shaders (red hitmarker when you hit an entity)
		for (Object object : entityList) {
			if(object instanceof DynamicObject) {
				DynamicObject entity = (DynamicObject) object;
				if(entity.getHit()) {
					shadersHelper.drawshader(entity, timePassed, camera);
				}
			}
		}

		// Render overlay elements
		overlay.render(player1, entityList.size() - 1, enemiesGenerator, enemiesGenerator.getEnemiesLeft());
	}

	
	public float getTimePassed() {
		return timePassed;
	}
	public OrthographicCamera getCamera() {
		return this.camera;
	}

	// Method that renders all current entities w.r.t. their y position
	public void renderEntities(Player player, float timePassed, float timeBetweenRenderCalls) {

		// Order the list of entities by yPos
		Collections.sort(entityList, new Comparator<Object>() {
			public int compare(Object entity1, Object entity2) {
				DynamicObject dynamicObject1 = (DynamicObject) entity1;
				DynamicObject dynamicObject2 = (DynamicObject) entity2;
                return Double.compare(dynamicObject2.getSprite().getY(), dynamicObject1.getSprite().getY());
            }
		});

		for (Object entity : entityList) {
			if(entity instanceof Player) {
				System.out.println("x= " + player1.getHitbox().getX() + " y= " + player1.getHitbox().getY());
				player1.render(batch, this, camera);

			} else if(entity instanceof Enemy){
				Enemy enemy = (Enemy) entity;
				
				enemy.render(
					timePassed,
					timeBetweenRenderCalls,
					batch,
					player,
					this,
					currentRoom.getBackground()
				);
			}
			DynamicObject collission = (DynamicObject) entity;
			currentRoom.checkCollission(collission, currentRoom.getBackground());

			if(entity instanceof Furniture) {
				Furniture furniture = (Furniture) entity;
				furniture.render(batch);
			}
		}
	}

	public EnemiesGenerator getEnemiesGenerator() {
		return enemiesGenerator;
	}
	public ShadersHelper getShadersHelper() {
		return shadersHelper;
	}
}