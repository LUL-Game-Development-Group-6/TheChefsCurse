package com.mygdx.game.Screens;

// LibGDX libraries
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.helpers.AnimationParameters;
import com.mygdx.game.helpers.Fonts;
import com.mygdx.game.helpers.SoundPaths;
import com.mygdx.game.helpers.StatsHelper;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import java.util.ArrayList;
import java.util.Iterator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class NewRound implements Screen  
{

    // Cover images
    private Texture background;

    // Pause Screen buttons
    private TextButtonStyle add;
    private TextButton add_health;
    private TextButton add_damage;
    private TextButtonStyle nextRound;

    private Texture damageTexture;
	private Sprite damageIcon;

    private Texture healthTexture;
	private Sprite healthIcon;

    // Variabkes to display elements (e.g, buttons)
    private Stage stage;
    private BitmapFont font;
    private BitmapFont font1;
    private BitmapFont font2;
    private BitmapFont font3;
    private Fonts myFont;
    private SpriteBatch batch;
    // Game object (Menu instance in the constructor)
    Menu game;
    private StatsHelper statsHelper;
    private ArrayList<AnimationParameters> xpAnimation;
    private float timePassed;

    // Sound effects
    private Sound buySound = Gdx.audio.newSound(Gdx.files.internal(SoundPaths.BUYUPGRADE_PATH));
    private Sound buttonSound = Gdx.audio.newSound(Gdx.files.internal(SoundPaths.BUTTON_PATH));
    private SoundPaths soundPaths = SoundPaths.getInstance();

    // Screen constructor
    public NewRound(Menu game) {
        this.game = game;
        this.statsHelper = game.getStatsHelper();
        xpAnimation = new ArrayList<>();
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

        background = new Texture("cover/next_round.png");

        createButtons();
        statsHelper.increaseEnemyScaler();
    }

    public void render(float delta) {
        stage.act();
        batch.begin();
        ScreenUtils.clear(1, 0, 0, 1);
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(damageIcon, 120, 250, Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/8);
        batch.draw(healthIcon, 120, 150, Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/8);
        font1.draw(batch, "Gear Up!\n\nupgrade your stats using \nthe XP points you gained.",120,  500);

        font3.draw(batch, "Cost:        XP left: ",120,  380);
        font2.draw(batch, "50xp",230,  380);
        font2.draw(batch, game.getStrXP(),490,  380);

        timePassed += Gdx.graphics.getDeltaTime();

        Iterator<AnimationParameters> iterator = xpAnimation.iterator();
		while (iterator.hasNext()) {
    		AnimationParameters params = iterator.next();
    		long elapsedTime = System.currentTimeMillis() - params.getTimeStarted();
    		if (elapsedTime < 400) {
        		batch.draw(params.geAnimation().getKeyFrame(timePassed, true),
                params.getX(), params.getY(), 120, 100);
    		} else {
        		iterator.remove();
    		}
		}

        Gdx.input.setInputProcessor(stage);
        batch.end();
        stage.draw();
        adderVisibility();
    }



    public void dispose() {
        
        background.dispose();
        stage.dispose();
        font.dispose();
    }

    public void createButtons() {

        myFont = new Fonts();
        font1 = myFont.getFont(Color.WHITE, 23, 3);
        font2 = myFont.getFont(Color.RED, 23, 3);
        font3 = myFont.getFont(Color.GREEN, 23, 3);

        // Health and damage Icon
        damageTexture = new Texture("buttons/damage.png");
		damageIcon = new Sprite(damageTexture);

        healthTexture = new Texture("buttons/health.png");
		healthIcon = new Sprite(healthTexture);

        // Adder buttons
        add = new TextButtonStyle();
        add.up = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/add_NoClick.png")));
        add.down = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/add_Clicked.png")));
        add.over = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/add_Hover.png")));
        add.font = font;
        add_damage = new TextButton("", add);
        add_damage.setSize(add_damage.getWidth()/2, add_damage.getHeight()/2);
        add_damage.setPosition(470, 250);

        add_health = new TextButton("", add);
        add_health.setSize(add_health.getWidth()/2, add_health.getHeight()/2);
        add_health.setPosition(470, 150);


        add_damage.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buySound.play(soundPaths.getVolume());
                AnimationParameters animation = new AnimationParameters(
                    game.getXpAnimationHelper().get50xp(),
                    -10,
                    250,
                    System.currentTimeMillis());
                
                xpAnimation.add(animation);

                statsHelper.incrementDamageScaler();
                game.useXP(50);
                System.out.println("Bought damage");
            }
        });

        add_health.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buySound.play(soundPaths.getVolume());
                AnimationParameters animation = new AnimationParameters(
                    game.getXpAnimationHelper().get50xp(),
                    -10,
                    150,
                    System.currentTimeMillis());
                
                xpAnimation.add(animation);

                statsHelper.incrementHealthScaler();
                game.useXP(50);
                System.out.println("Bought health");
            }
        });

        nextRound = new TextButtonStyle();
        nextRound.up = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/next_round_NoClick.png")));
        nextRound.down = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/next_round_Clicked.png")));
        nextRound.over = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/next_round_Hover.png")));
        nextRound.font = font;
        TextButton nextRound_button = new TextButton("", nextRound);
        nextRound_button.setSize(nextRound_button.getWidth()/2, nextRound_button.getHeight()/2);
        nextRound_button.setPosition(120, 30);

        nextRound_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buttonSound.play(soundPaths.getVolume());
                stage.dispose();
                //foodGame.getEnemiesGenerator().reset();
                game.incrementRound();
                game.setScreen(new FoodGame(game));
            }
        });



        // Add buttons to the screen
        stage.addActor(add_health);
        stage.addActor(add_damage);
        stage.addActor(nextRound_button);

    }

    private void adderVisibility() {

    boolean hideButtons = game.getXP() < 50;

    for (Actor actor : stage.getActors()) {
        if (actor instanceof TextButton) {
            if (actor == add_damage || actor == add_health) {
                actor.setVisible(!hideButtons);
            }
        }
    }
}
}