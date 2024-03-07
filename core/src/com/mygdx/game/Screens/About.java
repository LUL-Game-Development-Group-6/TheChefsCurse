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
 * Class for about menu screen
 *
 * Please refer to {@link com.mygdx.game.Screens.About}
 * @author Gines Moratalla, Juozas Skarbalius
 *
 */
public class About implements Screen  
{
    /**
     * Singleton declaration
     */
    private static About instance;

    public static About getInstance() {
        if(instance == null) instance = new About();
        return instance;
    }

    /**
     * Covers' current background
     */
    private Texture background;

    /**
     * AboutStatus enum used for identifying current about sub-screen. Also contains it's background texture.
     *
     * Please refer to {@link com.mygdx.game.Screens.About.AboutStatus}
     * @author Gines Moratalla, Juozas Skarbalius
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-181">[Logic] Options screen [S]</a>
     */
    private enum AboutStatus {
        AB0 (new Texture("cover/about_0.png")),
        AB01 (new Texture("cover/about_01.png")),
        AB1 (new Texture("cover/about_1.png")),
        AB2 (new Texture("cover/about_2.png")),
        AB3 (new Texture("cover/about_3.png")),
        ;

        private Texture background;

        AboutStatus(Texture background) {
            this.background = background;
        }

        public Texture getBackground() {
            return background;
        }
    }

    /**
     * Current about status screen displayed
     */
    private AboutStatus aboutStatus;

    /**
     * Variables to display elements (e.g, buttons)
     */
    private Stage stage;
    private BitmapFont font;

    /**
     * Game object reference
     */
    final Menu game;

    /**
     * Sound effects
     */
    private Sound buttonSound = Gdx.audio.newSound(Gdx.files.internal(SoundPaths.BUTTON_PATH));
    private SoundPaths soundPaths = SoundPaths.getInstance();

    public About() {
        this.game = Menu.getInstance();
    }

    /**
     * Methods necessary to implement Screen interface
     */
    public void pause() {}
    public void resume() {}
    public void resize(int width, int height) {}
    public void hide() {}

    public void show() {
        // Variables to draw buttons and images into the screen
        game.batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();
        aboutStatus = AboutStatus.AB0;
        background = AboutStatus.AB0.getBackground();
        createButtons();
    }
    
    public void render(float delta) {
        renderScreenBackground(stage, game, background);
    }

    /**
     * <p>
     *     Renders screen background
     * </p>
     * @since 1.0
     */
    protected static void renderScreenBackground(Stage stage, Menu game, Texture background) {
        stage.act();
        ScreenUtils.clear(1, 0, 0, 1);
        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();
        stage.draw();
    }

    /**
     * <p>
     *     Creates and renders buttons on about screen
     * </p>
     * @since 1.0
     */
    public void createButtons() {
        TextButton prev_button = Cover.createButton("buttons/left_arrow.png", "buttons/left_arrow_clicked.png", "buttons/left_arrow_hover.png", font, 1030, 570);
        prev_button.setSize(prev_button.getWidth()/2.5f, prev_button.getHeight()/2.5f);
        prev_button.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                moveThroughClicksReversed();
            }
        });

        TextButton next_button = Cover.createButton("buttons/arrow.png", "buttons/arrow_clicked.png", "buttons/arrow_hover.png", font, 1100, 570);
        next_button.setSize(next_button.getWidth()/2.5f, next_button.getHeight()/2.5f);
        next_button.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                moveThroughClicks();
            }
        });

        // Add button to the screen
        stage.addActor(prev_button);
        stage.addActor(next_button);
    }
    public void dispose() {
        background.dispose();
        stage.dispose();
        font.dispose();
        buttonSound.dispose();
    }

    public void moveThroughClicks() {
        switch (aboutStatus) {
            case AB0:
                aboutStatus = AboutStatus.AB01;
                break;
            case AB01:
                aboutStatus = AboutStatus.AB1;
                break;
            case AB1:
                aboutStatus = AboutStatus.AB2;
                break;
            case AB2:
                aboutStatus = AboutStatus.AB3;
                break;
            case AB3:
                aboutStatus = AboutStatus.AB0;
                game.setScreen(Cover.getInstance());
                break;
            default:
                break;
        }
        background = aboutStatus.getBackground();
        buttonSound.play(soundPaths.getVolume());
    }

    public void moveThroughClicksReversed() {
        switch (aboutStatus) {
            case AB0:
                game.setScreen(Cover.getInstance()); 
                break;
            case AB01:
                aboutStatus = AboutStatus.AB0;
                break;
            case AB1:
                aboutStatus = AboutStatus.AB01;
                break;
            case AB2:
                aboutStatus = AboutStatus.AB1;
                break;
            case AB3:
                aboutStatus = AboutStatus.AB2;
                break;
            default:
                break;
        }
        background = aboutStatus.getBackground();
        buttonSound.play(soundPaths.getVolume());
    }
    
}