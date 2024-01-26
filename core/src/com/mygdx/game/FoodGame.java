package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.physics.Room.factories.KitchenRoomFactory;
import com.mygdx.game.physics.Room.Room;
import com.mygdx.game.physics.Player;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.physics.Bullet;
import com.mygdx.game.physics.DynamicObject;
import com.mygdx.game.physics.Enemy;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.math.Vector2;

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
	private Texture zeroTexture;
	private Sprite zero;

	private Texture oneTexture;
	private Sprite one;

	private Texture twoTexture;
	private Sprite two;

	private Texture threeTexture;
	private Sprite three;

	private Texture fourTexture;
	private Sprite four;

	private Texture fiveTexture;
	private Sprite five;

	private Texture sixTexture;
	private Sprite six;

	private Texture sevenTexture;
	private Sprite seven;

	private Texture eightTexture;
	private Sprite eight;

	private Texture nineTexture;
	private Sprite nine;

	// Game instance from Menu class to swtich between screens
	final Menu game;

	// List of all the current enemies
	private ArrayList<Enemy> enemyList;
	private ArrayList<DynamicObject> entityList;

	public FoodGame(final Menu game) {

        this.game = game;
		this.enemyList = new ArrayList<>();
		this.entityList = new ArrayList<>();
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

		/*ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
        
        shapeRenderer.rect(enemy2.getHitbox().x, enemy2.getHitbox().y, enemy2.getHitbox().width, enemy2.getHitbox().height);
		shapeRenderer.rect(player1.getHitbox().x, player1.getHitbox().y, player1.getHitbox().width, player1.getHitbox().height);*/
        

		// Times to get time passed and to follow the player vector
		timeBetweenRenderCalls = Gdx.graphics.getDeltaTime();
        timePassed += Gdx.graphics.getDeltaTime();

        Vector2 playerPosition = new Vector2(player1.getHitbox().getX(), player1.getHitbox().getY());

		renderEntities(playerPosition, timePassed, timeBetweenRenderCalls);

		// Render things related to the overlay
		batch.draw(overlaySprite, 820, 570, overlaySprite.getWidth()/2 - 25, overlaySprite.getHeight()/2 - 10);
		batch.draw(enemiesLeftSprite, 843, 607, enemiesLeftSprite.getWidth()/3, enemiesLeftSprite.getHeight()/3);
		// Render number of enemies left
		batch.draw(zero, 1125, 610, zero.getWidth()/3, zero.getHeight()/3);
		batch.draw(two, 1150, 610, nine.getWidth()/3, nine.getHeight()/3);

		//shapeRenderer.end();

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
		
		Collections.sort(entityList, new Comparator<DynamicObject>() {

			public int compare(DynamicObject entity1, DynamicObject entity2) {
                return Double.compare(entity2.getSprite().getY(), entity1.getSprite().getY());
            }
		});

		for (DynamicObject entity : entityList) {

			if(entity.getPlayerBool()) {

				player1.render(batch, this);

			} else {

				batch.draw(entity.getHealthSprite(), entity.getHitbox().x - 20, entity.getHitbox().y + 80, entity.getHealthSprite().getWidth()/5, entity.getHealthSprite().getHeight()/5);
				entity.healthPercentage();
	
				batch.draw(entity.getEnemyAnimation().getKeyFrame(timePassed, true),
				entity.getSprite().getX(), entity.getSprite().getY(), entity.getWidth(), entity.getHeight());
	
				entity.render(timeBetweenRenderCalls, playerPosition);
				entity.enemyHit(playerPosition, player1);

			}

		}
	}


	public void createNumbers() {

		zeroTexture = new Texture("buttons/0.png");
		zero = new Sprite(zeroTexture);

		oneTexture = new Texture("buttons/1.png");
		one = new Sprite(oneTexture);

		twoTexture = new Texture("buttons/2.png");
		two = new Sprite(twoTexture);
		
		zeroTexture = new Texture("buttons/3.png");
		three = new Sprite(zeroTexture);

		fourTexture = new Texture("buttons/4.png");
		four = new Sprite(fourTexture);
		
		fiveTexture = new Texture("buttons/5.png");
		five = new Sprite(fiveTexture);

		sixTexture = new Texture("buttons/6.png");
		six = new Sprite(sixTexture);

		sevenTexture = new Texture("buttons/7.png");
		seven = new Sprite(sevenTexture);

		eightTexture = new Texture("buttons/8.png");
		eight = new Sprite(eightTexture);

		nineTexture = new Texture("buttons/9.png");
		nine = new Sprite(nineTexture);
	}
}
