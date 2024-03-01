package com.mygdx.game.Screens;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.helpers.SoundPaths;
/**
 * Entry class for the logic of the game (core)
 *
 * Please see the {@link com.mygdx.game.Screens.Cover}
 * @author Gines Moratalla, Juozas Skarbalius
 *
 */
public class Cover implements Screen  
{
    /**
     * Singleton declaration
     */
    private static Cover instance;

    public static Cover getInstance() {
        if(instance == null) instance = new Cover();
        return instance;
    }

    /**
     * Background texture of the menu
     */
    private Texture background;

    /**
     * Logo texture
     */
    private Texture logo;

    /**
     * Variables for animation
     */
    private TextureAtlas logoAtlas;
    private Animation<Sprite> logo_animation;
    private float timePassed;

    /**
     * Variables for display elements
     */
    private Stage stage;
    private BitmapFont font;

    /**
     * Game object (Menu instance in the constructor)
     */
    private Menu game;

    /**
     * Sound Effects
     */
    private Sound buttonSound = Gdx.audio.newSound(Gdx.files.internal(SoundPaths.BUTTON_PATH));
    private SoundPaths soundPaths = SoundPaths.getInstance();

    public Cover() {
        this.game = Menu.getInstance();
        createLogoAnimation();
    }

    /**
     * Methods that must be implemented from Screen interface
     */
    public void pause() {}
    public void resume() {}
    public void resize(int width, int height) {}
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * <p>
     *     Works similar like create() method from Game interface
     * </p>
     * @since 1.0
     */
    public void show() {
        // Variables to draw buttons and images into the screen
        game.batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();

        background = new Texture("cover/Cheffs_Curse_Cover.png");
        logo = new Texture("cover/logo_static.png");
        createButtons();
    }

    public void render(float delta) {
        stage.act();
        game.batch.begin();
        ScreenUtils.clear(1, 0, 0, 1);
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    
        // Draw logo animation in case the start button was pressed 
        if (logo_animation.getPlayMode() == Animation.PlayMode.NORMAL) {
            game.batch.draw(logo_animation.getKeyFrame(timePassed), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            timePassed += delta;
        // Draw the static logo if else
        } else {
            game.batch.draw(logo, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    
        game.batch.end();
        stage.draw();
    }

    public void dispose() {
        background.dispose();
        game.batch.dispose();
        stage.dispose();
        font.dispose();
        logoAtlas.dispose();
        buttonSound.dispose();
    }

    /**
     * <p>
     *     Creates logo animation used after start button is clicked
     * </p>
     * @since 1.0
     */
    private void createLogoAnimation() {
        logoAtlas = new TextureAtlas(Gdx.files.internal("cover/Logo.atlas"));

        Sprite[] spriteArray = new Sprite[12];
        for(int i = 0; i<12; i++) {
            spriteArray[i] = logoAtlas.createSprite("logo"+(i+1));
        }
        logo_animation = new Animation<Sprite>(1/15f, spriteArray);

        // Prevent animation to show at runtime (opening the game)
        logo_animation.setPlayMode(Animation.PlayMode.REVERSED);
    }

    /**
     * <p>
     *     Creates and returns a TextButton
     * </p>
     * @param upTexturePath
     * @param downTexturePath
     * @param overTexturePath
     * @param font used on TextButton
     * @param x coordinate (position) of the button
     * @param y coordinate (position) of the button
     * @return created TextButton according to parameters
     * @since 1.0
     */
    private static TextButton createButton(String upTexturePath, String downTexturePath, String overTexturePath, BitmapFont font, int x, int y) {
        TextButtonStyle buttonSkin = new TextButtonStyle();
        buttonSkin.up = new TextureRegionDrawable(new TextureRegion((new Texture(upTexturePath))));
        buttonSkin.down = new TextureRegionDrawable(new TextureRegion((new Texture(downTexturePath))));
        buttonSkin.over = new TextureRegionDrawable(new TextureRegion((new Texture(overTexturePath))));
        buttonSkin.font = font;
        TextButton button = new TextButton("", buttonSkin);
        button.setSize(button.getWidth()/2, button.getHeight()/2);
        button.setPosition(x, y);
        return button;
    }

    /**
     * <p>
     *     Creates all menu buttons with their respective event listeners
     * </p>
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-92">GD-92: Review Menu Prototype</a>
     * @since 1.0
     */
    private void createButtons() {
        TextButton start_button = createButton(
                "buttons/Start_NotClicked.png",
                "buttons/Start_Clicked.png",
                "buttons/Start_Hover.png",
                font, 120, 350);
        TextButton options_button = createButton(
                "buttons/Options_NotClicked.png",
                "buttons/Option_Clicked.png",
                "buttons/Options_Hover.png",
                font, 120, 250);
        TextButton about_button = createButton(
                "buttons/About_NotClicked.png",
                "buttons/About_Clicked.png",
                "buttons/About_Hover.png",
                font, 120, 150);
        TextButton exit_button = createButton(
                "buttons/Exit_NotClicked.png",
                "buttons/Exit_Clicked.png",
                "buttons/Exit_Hover.png",
                font, 120, 50);

        exit_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buttonSound.play(soundPaths.getVolume());
                Gdx.app.exit();
            }
        });

        about_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buttonSound.play(soundPaths.getVolume());
                game.setScreen(About.getInstance());
            }
        });

        options_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buttonSound.play(soundPaths.getVolume());
                game.setScreen(Options.getInstance());
            }
        });

        start_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                // Start the animation
                buttonSound.play(soundPaths.getVolume());
                logo_animation.setPlayMode(Animation.PlayMode.NORMAL);
                timePassed = 0;

                // Switch the screen after the animation is finished
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        if (logo_animation.isAnimationFinished(timePassed)) {
                            logo_animation.setPlayMode(Animation.PlayMode.REVERSED);
                            game.dispose();
                            game.setScreen(new FoodGame(game));
                        }
                    }
                }, logo_animation.getAnimationDuration());
            }
        });

        // Add buttons to the screen
        stage.addActor(start_button);
        stage.addActor(about_button);
        stage.addActor(exit_button);
        stage.addActor(options_button);
    }
}
