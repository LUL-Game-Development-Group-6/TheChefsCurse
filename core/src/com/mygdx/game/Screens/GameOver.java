package com.mygdx.game.Screens;

// LibGDX libraries

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.helpers.SoundPaths;
import com.badlogic.gdx.audio.Sound;

/**
 * Class for game over screen
 * <p>
 * Please see the {@link com.mygdx.game.Screens.GameOver}
 *
 * @author Gines Moratalla, Juozas Skarbalius
 */
public class GameOver implements Screen {
    /**
     * Cover images
     */
    private Texture background;
    private Animation<Sprite> gameOverAnimation;

    /**
     * Variables to display elements (e.g, buttons)
     */
    private Stage stage;
    private BitmapFont font;

    private float renderOpacity;
    private float timePassed;

    final Menu game;
    private FoodGame foodGame;

    // Sound effects
    private final Sound deathSound = Gdx.audio.newSound(Gdx.files.internal(SoundPaths.PLAYERDEAD_PATH));
    private final Sound buttonSound = Gdx.audio.newSound(Gdx.files.internal(SoundPaths.BUTTON_PATH));
    private final SoundPaths soundPaths = SoundPaths.getInstance();

    public GameOver(final Menu game, FoodGame foodGame) {
        this.renderOpacity = 0f;
        this.game = game;
        this.foodGame = foodGame;
    }

    public void pause() {
    }

    public void resume() {
    }

    public void resize(int width, int height) {
    }

    public void hide() {
    }

    public void show() {
        // Variables to draw buttons and images into the screen
        deathSound.play(soundPaths.getVolume());
        game.batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();

        background = new Texture("cover/black.png");
        Texture logo = new Texture("cover/GameOver_logo.png");

        TextureAtlas gameOverAtlas = new TextureAtlas("cover/gameOver.atlas");

        gameOverAnimation = new Animation<>(
                1 / 8f,
                gameOverAtlas.createSprite("GameOver_logo1"),
                gameOverAtlas.createSprite("GameOver_logo2"));

        // Pause Screen buttons
        TextButton exit_button =
                Cover.createButton("buttons/Exit_NotClicked.png", "buttons/Exit_Clicked.png", "buttons/Exit_Hover.png", font, 500, 100);
        exit_button.setSize(exit_button.getWidth() / 2, exit_button.getHeight() / 2);

        exit_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buttonSound.play(soundPaths.getVolume());
                deathSound.stop();
                foodGame.dispose();
                foodGame.getEnemiesGenerator().reset();
                game.resetGame();
                foodGame = null;
                game.setScreen(Cover.getInstance());
            }
        });

        // Add button to the screen
        stage.addActor(exit_button);
    }

    public void render(float delta) {

        renderOpacity = Math.min(renderOpacity + delta / 2, 1f);
        timePassed += delta;

        stage.act();
        game.batch.begin();
        ScreenUtils.clear(1, 0, 0, 1);

        game.batch.setColor(1f, 1f, 1f, renderOpacity);

        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(gameOverAnimation.getKeyFrame(timePassed, true), 320, 270, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        game.batch.end();

        game.batch.setColor(1f, 1f, 1f, 1f);
        stage.draw();
    }

    public void dispose() {
        background.dispose();
        stage.dispose();
        font.dispose();
        deathSound.dispose();
        buttonSound.dispose();
    }
}
