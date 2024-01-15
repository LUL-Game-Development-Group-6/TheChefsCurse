package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.physics.Room.factories.KitchenRoomFactory;
import com.mygdx.game.physics.Room.Room;
import com.mygdx.game.physics.Player;
import com.badlogic.gdx.Screen;
import com.mygdx.game.physics.Enemy;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.physics.Bullet;

public class FoodGame implements Screen
{
	private Room currentRoom;
	private SpriteBatch batch;
	private Player player1;
	private Enemy enemy;
	private float playerSize;
	private float timePassed;
    private float timeBetweenRenderCalls;

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
		currentRoom = new KitchenRoomFactory().createRoomBuilder().build();
		batch = new SpriteBatch();
		player1 = new Player(400, 400, 48, 150);
		enemy = new Enemy(new Vector2(300,300), 56, 185, Enemy.EnemyType.POPCORN);
		playerSize = 165;
	}

	@Override
	public void render (float delta)
	{
        // This is passed to render() so that it can calculate position
        timeBetweenRenderCalls = Gdx.graphics.getDeltaTime();
        batch.begin();
		
        player1.render();
		
        ScreenUtils.clear(0, 0, 0, 0);
        currentRoom.render(batch);
		for (Bullet current : player1.getAmmunition()){
			current.render(batch);
		}
        timePassed += Gdx.graphics.getDeltaTime();
        delta += Gdx.graphics.getDeltaTime();

        batch.draw(player1.getSprite(), player1.getSprite().getX(), player1.getSprite().getY(), playerSize, playerSize);
        Vector2 playerPosition = new Vector2(player1.getSprite().getX(), player1.getSprite().getY());
        batch.draw(enemy.getSprite(), enemy.getSprite().getX(), enemy.getSprite().getY(), playerSize, playerSize);
        enemy.render(timeBetweenRenderCalls, playerPosition);
		
		
		
        batch.end();
	}


	public void dispose()
	{
		game.batch.dispose();
		player1.dispose();

	}
}
