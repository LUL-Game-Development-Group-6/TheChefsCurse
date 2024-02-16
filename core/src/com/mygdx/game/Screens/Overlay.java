package com.mygdx.game.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.helpers.Fonts;
import com.mygdx.game.physics.EnemiesGenerator;
import com.mygdx.game.physics.Player;

public class Overlay implements Screen {

    public Overlay() {}
    
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
    private BitmapFont roundFont;
    private BitmapFont roundNumber;
    private Fonts myFont;

    private TextButtonStyle nextRound;
    private TextButton nextRound_button;
    private Stage stage;
    private FoodGame foodGame;
    private Menu game;

    private Music gameMusic;
    

    SpriteBatch batch;

    public Overlay(FoodGame foodGame, Music gameMusic) {
        this.gameMusic = gameMusic;
        this.foodGame = foodGame;
        this.game = Menu.getInstance();
        myFont = new Fonts();
        batch = new SpriteBatch();

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
		font = myFont.getFont(Color.WHITE, 35, 2);
        roundFont = myFont.getFont(Color.WHITE, 45, 3);
        roundNumber = myFont.getFont(Color.RED, 45, 3);

        // Next round button
        stage = new Stage();
        nextRound = new TextButtonStyle();
        nextRound.up = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/next_round_NoClick.png")));
        nextRound.down = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/next_round_Clicked.png")));
        nextRound.over = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/next_round_Hover.png")));
        nextRound.font = font;
        nextRound_button = new TextButton("", nextRound);
        nextRound_button.setSize(nextRound_button.getWidth()/2, nextRound_button.getHeight()/2);
        nextRound_button.setPosition(500, 30);
        stage.addActor(nextRound_button);
        show();

        batch = new SpriteBatch();
            
    }
 
    public void show() {
        nextRound_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                gameMusic.stop();
                stage.dispose();
                foodGame.getEnemiesGenerator().reset();
                game.setScreen(new NewRound(game));
            }
        });
    }

    public void render(Player player1, int enemiesLeft, EnemiesGenerator generator, int counter) {

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



        String enemiesLeftStr = Integer.toString(counter);
        //System.out.println(counter);

		font.draw(batch, enemiesLeftStr,centerX(enemiesLeftStr),  650);
        roundFont.draw(batch, "xp:",30,  150);
        roundFont.draw(batch, "ROUND:",30, 80);
        roundNumber.draw(batch, game.getStrRound(),250,  80);
        roundNumber.draw(batch, game.getStrXP(),150,  150);

        batch.end();

        if(counter <= 0) nextRound();
    }

    public void nextRound() {
        
        stage.act();
        Gdx.input.setInputProcessor(stage);
        stage.draw();
    }

    /*
     * This only returns the x coordinate depending on the ammount of enemies
     * to center the numbers
     */
    private int centerX(String numberString) {

        if(numberString.length() == 1) {
            return 1160;
        } else if (numberString.length() == 2) {
            return 1150;
        }
        return 1140;
    } 

    @Override
    public void render(float delta) {
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
    }
}
