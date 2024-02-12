package com.mygdx.game.Screens;

// LibGDX libraries
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class About implements Screen  
{

    // Cover images
    private Texture background;

    // Pause Screen buttons
    private TextButtonStyle exit;

    // Variabkes to display elements (e.g, buttons)
    private Stage stage;
    private BitmapFont font;

    private float timePassed;

    // Game object (Menu instance in the constructor)
    final Menu game;

    // Screen constructor
    public About(final Menu game) {
        this.game = game;
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

        background = new Texture("cover/about_1.png");

        exit = new TextButtonStyle();
        exit.up = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Exit_NotClicked.png")));
        exit.down = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Exit_Clicked.png")));
        exit.over = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/Exit_Hover.png")));
        exit.font = font;
        TextButton exit_button = new TextButton("", exit);
        exit_button.setSize(exit_button.getWidth()/2, exit_button.getHeight()/2);
        exit_button.setPosition(800, 700);

        exit_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(Cover.getInstance());
            }
        });

        // Add button to the screen
        stage.addActor(exit_button);
    }

    public void render(float delta) {

        stage.act();
        ScreenUtils.clear(1, 0, 0, 1);
        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        game.batch.end();
        stage.draw();
    }

    public void dispose() {
        
        background.dispose();
        stage.dispose();
        font.dispose();
    }
}
