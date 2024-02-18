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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class About implements Screen  
{
    private static About instance;

    public static About getInstance() {
        if(instance == null) instance = new About();
        return instance;
    }

    // Cover images
    private Texture background;
    private Texture background0;
    private Texture background01;
    private Texture background1;
    private Texture background2;
    private Texture background3;

    private static enum AboutStatus {
        AB0,
        AB01,
        AB1,
        AB2,
        AB3,
    }

    private AboutStatus aboutStatus;

    // Pause Screen buttons
    private TextButtonStyle prev;
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
    public About() {
        this.game = Menu.getInstance();
        background0 = new Texture("cover/about_0.png");
        background01 = new Texture("cover/about_01.png");
        background1 = new Texture("cover/about_1.png");
        background2 = new Texture("cover/about_2.png");
        background3 = new Texture("cover/about_3.png");
    }

    // Methods necessary to implement Screen interface
    public void pause() {}
    public void resume() {}
    public void resize(int width, int height) {}
    public void hide() {}

    // Works like create() method
    public void show() {
        // Variables to draw buttons and images into the screen
        game.batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();
        aboutStatus = AboutStatus.AB0;
        background = background0;
        createButtons();
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

        prev = new TextButtonStyle();
        prev.up = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/left_arrow.png")));
        prev.down = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/left_arrow_clicked.png")));
        prev.over = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/left_arrow_hover.png")));
        prev.font = font;

        TextButton prev_button = new TextButton("", prev);
        prev_button.setSize(prev_button.getWidth()/2.5f, prev_button.getHeight()/2.5f);
        prev_button.setPosition(1030, 570);
        prev_button.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                moveThroughClicksReversed();
            }
        });

        
        next = new TextButtonStyle();
        next.up = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/arrow.png")));
        next.down = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/arrow_clicked.png")));
        next.over = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/arrow_hover.png")));
        next.font = font;

        TextButton next_button = new TextButton("", next);
        next_button.setSize(next_button.getWidth()/2.5f, next_button.getHeight()/2.5f);
        next_button.setPosition(1100, 570);
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
                background = background01;
                break;
            case AB01:
                aboutStatus = AboutStatus.AB1;
                background = background1;
                break;
            case AB1:
                aboutStatus = AboutStatus.AB2;
                background = background2; 
                break;
            case AB2:
                aboutStatus = AboutStatus.AB3;
                background = background3;
                break;
            case AB3:
                aboutStatus = AboutStatus.AB0;
                game.setScreen(Cover.getInstance());
                break;
            default:
                break;
        }
        buttonSound.play(soundPaths.getVolume());
    }

    public void moveThroughClicksReversed() {
        switch (aboutStatus) {
            case AB0:
                game.setScreen(Cover.getInstance()); 
                break;
            case AB01:
                aboutStatus = AboutStatus.AB0;
                background = background0;
                break;
            case AB1:
                aboutStatus = AboutStatus.AB01;
                background = background01;
                break;
            case AB2:
                aboutStatus = AboutStatus.AB1;
                background = background1;
                break;
            case AB3:
                aboutStatus = AboutStatus.AB2;
                background = background2;
                break;
            default:
                break;
        }
        buttonSound.play(soundPaths.getVolume());
    }
    
}