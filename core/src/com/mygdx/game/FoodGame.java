package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.physics.Room.factories.KitchenRoomFactory;
import com.mygdx.game.physics.Room.Room;
import com.mygdx.game.physics.Player;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.physics.Bullet;
import com.mygdx.game.physics.Enemy;
import com.badlogic.gdx.math.Vector2;

public class FoodGame implements Screen
{
	private Room currentRoom;
	private SpriteBatch batch;
	private Player player1;
	private Enemy enemy;
	private float playerSize;
	private float timePassed;
    private float timeBetweenRenderCalls;


	// Overlay
	private Texture overlayTexture;
	private Sprite overlaySprite;

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

	final Menu game;
	public FoodGame(final Menu game) {
        this.game = game;
    }

    public void pause() {}
    public void resume() {}
    public void resize(int width, int height) {}
    public void hide() {}

	@Override
	public void show ()
	{
		// create stuff for the overlay
		overlayTexture = new Texture("buttons/Rectangle_enemies_left.png");
		overlaySprite = new Sprite(overlayTexture);

		enemiesLeftTexture = new Texture("buttons/Enemies_left_text.png");
		enemiesLeftSprite = new Sprite(enemiesLeftTexture);


		createNumbers();

		// Room and player creation
		currentRoom = new KitchenRoomFactory().createRoomBuilder().build();
		batch = new SpriteBatch();
		player1 = new Player(400, 400, 48, 150);
		enemy = new Enemy(new Vector2(300,300), 56, 100, Enemy.EnemyType.POPCORN);
		playerSize = 165;
	}

	@Override
	public void render (float delta)
	{

		ScreenUtils.clear(0, 0, 0, 0);
		
        // This is passed to render() so that it can calculate position
        
        batch.begin();
		currentRoom.render(batch);

		/*ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
        
        shapeRenderer.rect(enemy.getHitbox().x, enemy.getHitbox().y, enemy.getHitbox().width, enemy.getHitbox().height);
		shapeRenderer.rect(player1.getHitbox().x, player1.getHitbox().y, player1.getHitbox().width, player1.getHitbox().height);*/
        

		timeBetweenRenderCalls = Gdx.graphics.getDeltaTime();
        timePassed += Gdx.graphics.getDeltaTime();

        //batch.draw(player1.getSprite(), player1.getSprite().getX(), player1.getSprite().getY(), playerSize, playerSize);
        Vector2 playerPosition = new Vector2(player1.getHitbox().getX(), player1.getHitbox().getY());
        batch.draw(enemy.getSprite(), enemy.getSprite().getX(), enemy.getSprite().getY(), enemy.getWidth(), enemy.getHeight());
        enemy.render(timeBetweenRenderCalls, playerPosition);
		player1.render(batch, this);

		// Render bullet

		// Render things related to the overlay
		batch.draw(overlaySprite, 820, 570, overlaySprite.getWidth()/2 - 25, overlaySprite.getHeight()/2 - 10);
		batch.draw(enemiesLeftSprite, 843, 607, enemiesLeftSprite.getWidth()/3, enemiesLeftSprite.getHeight()/3);

		// Render number of enemies left
		batch.draw(zero, 1125, 610, zero.getWidth()/3, zero.getHeight()/3);
		batch.draw(nine, 1150, 610, nine.getWidth()/3, nine.getHeight()/3);

		//shapeRenderer.end();
		

		// Player's bullets
		for (Bullet current : player1.getAmmunition()){
			current.render(batch);
			if (current.getHitbox().overlaps(enemy.getHitbox())) {
				current.setVisibility(false);
				System.out.println("Done");
			}
			if(current.getDespawnTime() < System.currentTimeMillis()) {
				current.setVisibility(false); 	
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
