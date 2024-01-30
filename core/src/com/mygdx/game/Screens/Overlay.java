package com.mygdx.game.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.physics.Player;

public class Overlay {
    
    private Texture OverlayTexture;
    private Sprite OverlaySprite;

    // Textures for standing sprites (3 weapons)
    private Texture RedGunTexture;
    private Sprite RedGunSprite;

    private Texture ShotGunTexture;
    private Sprite ShotGunSprite;

    private Texture HandTexture;
    private Sprite HandSprite;

    // Enemies left
    private Texture enemiesLeftTexture;
	private Sprite enemiesLeftSprite;


	private Texture overlayTexture;
	private Sprite overlaySprite;

    private BitmapFont font;


    SpriteBatch batch;

    public Overlay() {

        // Overlay
		OverlayTexture = new Texture("cheff/Weapon_Overlay.png");
		OverlaySprite = new Sprite(OverlayTexture);

		RedGunTexture = new Texture("cheff/weapons/RedGun.png");
		RedGunSprite = new Sprite(RedGunTexture);
		
		ShotGunTexture = new Texture("cheff/weapons/Shotgun.png");
		ShotGunSprite = new Sprite(ShotGunTexture);

		HandTexture = new Texture("cheff/weapons/Hand.png");
		HandSprite = new Sprite(HandTexture);


        // Panel that showcases the enemies left
        overlayTexture = new Texture("buttons/Rectangle_enemies_left.png");
		overlaySprite = new Sprite(overlayTexture);

		enemiesLeftTexture = new Texture("buttons/Enemies_left_text.png");
		enemiesLeftSprite = new Sprite(enemiesLeftTexture);

        // Handle font
		font = new BitmapFont();
		font.getData().setScale(3);
		font.setColor(Color.BLACK);

        batch = new SpriteBatch();
            
    }

    public void render(Player player1, int enemiesLeft) {

        batch.begin();
        
        
        // Render player's health and overlay at the top left of the screen

        batch.draw(OverlaySprite, 0, 550, OverlaySprite.getWidth()/2, OverlaySprite.getHeight()/2);
        batch.draw(player1.getHealthSprite(), 155, 643, player1.getHealthSprite().getWidth()/2 - 20, player1.getHealthSprite().getHeight()/2 - 10);
		player1.healthPercentage();


        if(player1.getWeaponType() == Player.WeaponType.FIST) {
			batch.draw(HandSprite, 45, 585, HandSprite.getWidth()/2, HandSprite.getHeight()/2);
		}

		if(player1.getWeaponType() == Player.WeaponType.REDGUN) {
			batch.draw(RedGunSprite, 30, 590, RedGunSprite.getWidth()/2, RedGunSprite.getHeight()/2);
		}

		if(player1.getWeaponType() == Player.WeaponType.SHOTGUN) {	
			batch.draw(ShotGunSprite, 22, 600, ShotGunSprite.getWidth()/3, ShotGunSprite.getHeight()/3);	
		}


        // Render things related to the overlay
		batch.draw(overlaySprite, 845, 580, overlaySprite.getWidth()/2 - 25, overlaySprite.getHeight()/2 - 10);
		batch.draw(enemiesLeftSprite, 870, 620, enemiesLeftSprite.getWidth()/3f, enemiesLeftSprite.getHeight()/3f);


        String enemiesLeftStr = Integer.toString(enemiesLeft);
		font.draw(batch, enemiesLeftStr,1165,  655);

        batch.end();

    }
}
