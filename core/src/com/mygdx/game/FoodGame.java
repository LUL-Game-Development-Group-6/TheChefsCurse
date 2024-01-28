package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.misc.Pair;
import com.mygdx.game.physics.Room.factories.KitchenRoomFactory;
import com.mygdx.game.physics.Room.Room;
import com.mygdx.game.physics.Player;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.physics.Bullet;
import com.mygdx.game.physics.DynamicObject;
import com.mygdx.game.physics.Enemy;

import com.badlogic.gdx.math.Vector2;
import org.w3c.dom.Text;

public class FoodGame implements Screen
{
	private Room currentRoom;
	private SpriteBatch batch;
	private Player player1;
	private Enemy enemy;
	private Enemy enemy2;

	private float timePassed;
    private float timeBetweenRenderCalls;

	// Overlay
	private Texture overlayTexture;
	private Sprite overlaySprite;

	private Texture pauseTexture;
	private Sprite pauseSprite;

	// Boolean to check if the game has been paused
	private boolean pausedGameplay;

	private Texture enemiesLeftTexture;
	private Sprite enemiesLeftSprite;

	// Overlay: numbers
	private Map<Integer, Pair> buttonTextures;

	// Game instance from Menu class to swtich between screens
	final Menu game;

	// List of all the current enemies
	private ArrayList<Enemy> enemyList;
	private ArrayList<DynamicObject> entityList;

	public FoodGame(final Menu game) {

        this.game = game;
		this.enemyList = new ArrayList<>();
		this.entityList = new ArrayList<>();
		this.buttonTextures = new HashMap<>();
		pausedGameplay = false;

		// create stuff for the overlay
		overlayTexture = new Texture("buttons/Rectangle_enemies_left.png");
		overlaySprite = new Sprite(overlayTexture);

		enemiesLeftTexture = new Texture("buttons/Enemies_left_text.png");
		enemiesLeftSprite = new Sprite(enemiesLeftTexture);

		// Create number sprites
		createNumbers();

		// Room and player creation
		currentRoom = new KitchenRoomFactory().createRoomBuilder().build();
		batch = new SpriteBatch();

		player1 = new Player(400, 400, 48, 150);
		createEnemies();
		entityList.add(player1);

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
		currentRoom.render(batch);
        

		// Times to get time passed and to follow the player vector
		timeBetweenRenderCalls = Gdx.graphics.getDeltaTime();
        timePassed += Gdx.graphics.getDeltaTime();

        Vector2 playerPosition = new Vector2(player1.getHitbox().getX(), player1.getHitbox().getY());

		renderEntities(playerPosition, timePassed, timeBetweenRenderCalls);

		// Render things related to the overlay
		batch.draw(overlaySprite, 820, 570, overlaySprite.getWidth()/2 - 25, overlaySprite.getHeight()/2 - 10);
		batch.draw(enemiesLeftSprite, 843, 607, enemiesLeftSprite.getWidth()/3, enemiesLeftSprite.getHeight()/3);
		// Render number of enemies left
		batch.draw(getButtonTexture(0), 1125, 610, getButtonTexture(0).getWidth()/3, getButtonTexture(0).getHeight()/3);
		batch.draw(getButtonTexture(2), 1150, 610, getButtonTexture(9).getWidth()/3, getButtonTexture(9).getHeight()/3);

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
		
        batch.end();
	}


	public float getTimePassed() {
		return timePassed;
	}

	public void dispose()
	{
		game.batch.dispose();
		player1.dispose();
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

		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
        
		
		Collections.sort(entityList, new Comparator<DynamicObject>() {

			public int compare(DynamicObject entity1, DynamicObject entity2) {
                return Double.compare(entity2.getSprite().getY(), entity1.getSprite().getY());
            }
		});

		for (DynamicObject entity : entityList) {

			shapeRenderer.rect(entity.getHitbox().x, entity.getHitbox().y, entity.getHitbox().width, entity.getHitbox().height);

			if(entity.getPlayerBool()) {

				player1.render(batch, this);

			} else {

				batch.draw(entity.getHealthSprite(), entity.getSprite().getX() - 10, entity.getSprite().getY() + 100, entity.getHealthSprite().getWidth()/5, entity.getHealthSprite().getHeight()/5);
				entity.healthPercentage();
	
				batch.draw(entity.getEnemyAnimation().getKeyFrame(timePassed, true),
				entity.getSprite().getX(), entity.getSprite().getY(), entity.getWidth(), entity.getHeight());
	
				entity.render(timeBetweenRenderCalls, playerPosition);
				entity.enemyHit(playerPosition, player1);

			}

		}
		shapeRenderer.end();
	}

	public void createNumbers() {

		for(int i = 0; i<10; i++) {
			Texture texture = new Texture("buttons/" + i + ".png");
			buttonTextures.put(i, new Pair(texture, new Sprite(texture)));
		}
	}

	private Texture getButtonTexture(int index) {
		return (Texture) buttonTextures.get(index).getFirst();
	}
}
