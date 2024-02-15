package com.mygdx.game.Screens;

// LibGDX libraries
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.helpers.SoundPaths;

public class Pause implements Screen  
{

    // Cover images
    private Texture background;
    private Texture logo;

    // Pause Screen buttons
    private TextButtonStyle exit;
    private TextButtonStyle resume;

    // Variabkes to display elements (e.g, buttons)
    private Stage stage;
    private BitmapFont font;
    private SpriteBatch batch;
    // Game object (Menu instance in the constructor)
    Menu game;
    private FoodGame foodGame;
	
	private Sound buttonSound = Gdx.audio.newSound(Gdx.files.internal(SoundPaths.BUTTON_PATH));

    // Screen constructor
    public Pause(Menu game, FoodGame foodGame) {
        this.game = game;
        this.foodGame = foodGame;
    }

    // Methods necessary to implement Screen interface
    public void pause() {}
    public void resume() {}
    public void resize(int width, int height) {}
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    // Works like create() method
    public void show() {
        
        // Variables to draw buttons and images into the screen
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();

        background = new Texture("cover/pause_background.png");
        logo = new Texture("cover/Paused_logo.png");


        exit = new TextButtonStyle();
        exit.up = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Exit_NotClicked.png")));
        exit.down = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Exit_Clicked.png")));
        exit.over = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Exit_Hover.png")));
        exit.font = font;
        TextButton exit_button = new TextButton("", exit);
        exit_button.setSize(exit_button.getWidth()/2, exit_button.getHeight()/2);
        exit_button.setPosition(300, 100);


        resume = new TextButtonStyle();
        resume.up = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/resume_NotClicked.png")));
        resume.down = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/resume_Clicked.png")));
        resume.over = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/resume_Hover.png")));
        resume.font = font;
        TextButton resume_button = new TextButton("", resume);
        resume_button.setSize(resume_button.getWidth()/2, resume_button.getHeight()/2);
        resume_button.setPosition(650, 100);


        resume_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buttonSound.play(0.3f);
				buttonSound.dispose();
				foodGame.gameMusic.play();
				foodGame.setPaused(false);
                game.setScreen(foodGame);
            }
        });

        exit_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buttonSound.play(0.3f);
				buttonSound.dispose();
				game.resetGame();
                foodGame.getEnemiesGenerator().reset();
                game.setScreen(Cover.getInstance());
            }
        });

        // Add buttons to the screen
        stage.addActor(resume_button);
        stage.addActor(exit_button);
    }

    public void render(float delta) {
        stage.act();
        batch.begin();
        ScreenUtils.clear(1, 0, 0, 1);
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(logo, 320, 270, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);


        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) game.setScreen(foodGame);
        batch.end();
        stage.draw();
    }

    public void dispose() {
        
        background.dispose();
        stage.dispose();
        font.dispose();
    }
}
