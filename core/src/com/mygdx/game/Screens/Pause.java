package com.mygdx.game.Screens;

// LibGDX libraries

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.helpers.SoundPaths;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Class pause screen
 * Please refer to {@link com.mygdx.game.Screens.Pause}
 *
 * @author Gines Moratalla, Juozas Skarbalius
 */
public class Pause implements Screen {

    /**
     * Cover images
     */
    private Texture background;
    private Texture logo;

    /**
     * Variables to display elements (e.g, buttons)
     */
    private Stage stage;
    private BitmapFont font;
    private SpriteBatch batch;
    private final Menu game;
    private final FoodGame foodGame;

    /**
     * Sound Effects
     */
    private final Sound buttonSound = Gdx.audio.newSound(Gdx.files.internal(SoundPaths.BUTTON_PATH));
    private final SoundPaths soundPaths = SoundPaths.getInstance();

    public Pause(Menu game, FoodGame foodGame) {
        this.game = game;
        this.foodGame = foodGame;
    }

    // Methods necessary to implement Screen interface
    public void pause() {
    }

    public void resume() {
    }

    public void resize(int width, int height) {
    }

    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    public void show() {

        // Variables to draw buttons and images into the screen
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();

        background = new Texture("cover/pause_background.png");
        logo = new Texture("cover/Paused_logo.png");

        /**
         * Cover images
         */
        TextButton exit_button =
                Cover.createButton("buttons/Exit_NotClicked.png", "buttons/Exit_Clicked.png", "buttons/Exit_Hover.png", font, 490, 200);
        exit_button.setSize(exit_button.getWidth() / 2, exit_button.getHeight() / 2);

        TextButton resume_button =
                Cover.createButton("buttons/resume_NotClicked.png", "buttons/resume_Clicked.png", "buttons/resume_Hover.png", font, 490, 100);
        resume_button.setSize(resume_button.getWidth() / 2, resume_button.getHeight() / 2);

        resume_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buttonSound.play(soundPaths.getVolume());
                foodGame.setPaused(false);
                game.setScreen(foodGame);
            }
        });

        exit_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buttonSound.play(soundPaths.getVolume());
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
        batch.draw(logo, 320, 350, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        batch.end();
        stage.draw();
    }

    public void dispose() {
        background.dispose();
        stage.dispose();
        font.dispose();
        buttonSound.dispose();
    }
}