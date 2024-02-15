package com.mygdx.game.Screens;

// LibGDX libraries
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

public class Cover implements Screen  
{
    private static Cover instance;

    public static Cover getInstance() {
        if(instance == null) instance = new Cover();
        return instance;
    }

    // Cover images
    private Texture background;
    private Texture logo;

    // Variables for animation
    private TextureAtlas logoAtlas;
    private Animation<Sprite> logo_animation;
    private float timePassed;

    // Cover buttons
    private TextButtonStyle exit;
    private TextButtonStyle about;
    private TextButtonStyle start;
    private TextButtonStyle options;

    // Variabkes to display elements (e.g, buttons)
    private Stage stage;
    private BitmapFont font;

    // Game object (Menu instance in the constructor)
    private Menu game;

    // Sound Effects
    private Sound buttonSound = Gdx.audio.newSound(Gdx.files.internal(SoundPaths.BUTTON_PATH));
    private SoundPaths soundPaths = SoundPaths.getInstance();

    // Screen constructor
    public Cover() {
        this.game = Menu.getInstance();
        logoAnimated();
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
    }
 
    private void logoAnimated() {

        logoAtlas = new TextureAtlas(Gdx.files.internal("cover/Logo.atlas"));
	    logo_animation = new Animation<Sprite>(
			1/15f,
            // TODO; FOR LOOP INSTEAD OF THIS
			logoAtlas.createSprite("logo1"),
			logoAtlas.createSprite("logo2"),
			logoAtlas.createSprite("logo3"),
			logoAtlas.createSprite("logo4"),
			logoAtlas.createSprite("logo5"),
			logoAtlas.createSprite("logo6"),
			logoAtlas.createSprite("logo7"),
            logoAtlas.createSprite("logo8"),
            logoAtlas.createSprite("logo9"),
            logoAtlas.createSprite("logo10"),
            logoAtlas.createSprite("logo11"),
			logoAtlas.createSprite("logo12"));

        // Prevent animation to show at runtime (opening the game)
        logo_animation.setPlayMode(Animation.PlayMode.REVERSED);
    }  
    
    private void createButtons() {

        start = new TextButtonStyle();
        start.up = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Start_NotClicked.png")));
        start.down = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Start_Clicked.png")));
        start.over = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Start_Hover.png")));
        start.font = font;
        TextButton start_button = new TextButton("", start);
        start_button.setSize(start_button.getWidth()/2, start_button.getHeight()/2);
        start_button.setPosition(120, 350);

        options = new TextButtonStyle();
        options.up = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Options_NotClicked.png")));
        options.down = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Option_Clicked.png")));
        options.over = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Options_Hover.png")));
        options.font = font;
        TextButton options_button = new TextButton("", options);
        options_button.setSize(options_button.getWidth()/2, options_button.getHeight()/2);
        options_button.setPosition(120, 250);

        about = new TextButtonStyle();
        about.up = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/About_NotClicked.png")));
        about.down = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/About_Clicked.png")));
        about.over = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/About_Hover.png")));
        about.font = font;
        TextButton about_button = new TextButton("", about);
        about_button.setSize(about_button.getWidth()/2, about_button.getHeight()/2);
        about_button.setPosition(120, 150);

        exit = new TextButtonStyle();
        exit.up = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Exit_NotClicked.png")));
        exit.down = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Exit_Clicked.png")));
        exit.over = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Exit_Hover.png")));
        exit.font = font;
        TextButton exit_button = new TextButton("", exit);
        exit_button.setSize(exit_button.getWidth()/2, exit_button.getHeight()/2);
        exit_button.setPosition(120, 50);


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
