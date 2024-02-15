package com.mygdx.game.Screens;

// LibGDX libraries
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.helpers.SoundPaths;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;



public class Options implements Screen  
{
    private static Options instance;

    public static Options getInstance() {
        if(instance == null) instance = new Options();
        return instance;
    }

    // Volume Slider
    private Slider musicSlider;
    private Slider sfxSlider;


    // Cover images
    private Texture background;

    // Pause Screen buttons
    private TextButtonStyle next;

    // Variabkes to display elements (e.g, buttons)
    private Stage stage;
    private BitmapFont font;

    // Game object (Menu instance in the constructor)
    Menu game;

    // Sound Effects
    private Sound buttonSound = Gdx.audio.newSound(Gdx.files.internal(SoundPaths.BUTTON_PATH));
    private SoundPaths soundPaths = SoundPaths.getInstance();

    // Screen constructor
    public Options() {
        this.game = Menu.getInstance();
        background = new Texture("cover/options_1.png");

        // Variables to draw buttons and images into the screen
        game.batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont();
        createButtons();
    }

    // Methods necessary to implement Screen interface
    public void pause() {}
    public void resume() {}
    public void resize(int width, int height) {}
    public void hide() {}

    // Works like create() method
    public void show() {

        Gdx.input.setInputProcessor(stage);

    }
    
    public void render(float delta) {

        stage.act();
        ScreenUtils.clear(1, 0, 0, 1);
        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();
        stage.draw();
    }

    public void createButtons() {

        // Add slider style
        Texture volumeBar = new Texture(Gdx.files.internal("buttons/volume_bar.png"));
        TextureRegionDrawable sliderBarDrawable = new TextureRegionDrawable(new TextureRegion(volumeBar));

        Texture knob = new Texture(Gdx.files.internal("buttons/knob.png"));
        TextureRegionDrawable knobDrawable = new TextureRegionDrawable(new TextureRegion(knob));

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(sliderBarDrawable, knobDrawable);

        musicSlider = new Slider(0f, 1f, 0.01f, false, sliderStyle);
        sfxSlider = new Slider(0f, 1f, 0.01f, false, sliderStyle);

        sfxSlider.setPosition(120, 180);
        sfxSlider.setSize(600, 10);
        sfxSlider.setValue(soundPaths.getVolume());

        musicSlider.setPosition(120, 335);
        musicSlider.setSize(600, 10);
        musicSlider.setValue(soundPaths.getMusicVolume());
        

        next = new TextButtonStyle();
        next.up = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Normal_X.png")));
        next.down = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Clicked_X.png")));
        next.over = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Hover_X.png")));
        next.font = font;

        TextButton next_button = new TextButton("", next);
        next_button.setSize(next_button.getWidth()/2f, next_button.getHeight()/2f);
        next_button.setPosition(1050, 540);
        next_button.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                buttonSound.play(soundPaths.getVolume());
                game.setScreen(Cover.getInstance());
            }
        });
        
        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = sfxSlider.getValue();
                soundPaths.setVolume(volume);
            }
        });
        
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = musicSlider.getValue();
                soundPaths.setMusicVolume(volume);
            }
        });
        
        

        // Add button to the screen
        stage.addActor(next_button);
        stage.addActor(sfxSlider);
        stage.addActor(musicSlider);
    }
    public void dispose() {
        
        background.dispose();
        stage.dispose();
        font.dispose();
    }
}