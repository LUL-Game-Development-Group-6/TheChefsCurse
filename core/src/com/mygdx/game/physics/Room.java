package com.mygdx.game.physics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;



public class Room
{
	private int roomType;
	private boolean hasBoss;
	private int enemiesNumber;
	private int roomID;
	private Texture background;
	
	
	public Room(int type, boolean boss, int enemyNum)
	{
		roomID = 1;
		
		roomType = type;
		hasBoss = boss;
		enemiesNumber = enemyNum;
		
		
		if (roomID == 1){
			background = new Texture("kitchen_size1.png");
		}
	
		
	}
	
	public void render(SpriteBatch batch)
	{
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());	
	}
	
	public int getID()
	{
			return roomID;
	}
	
	public boolean getHasBoss()
	{
		return hasBoss;
	}
	public int getEnemiesNumber()
	{
		return enemiesNumber;
	}
	
	public Texture getBackground()
	{
			return background;
	}
	
}