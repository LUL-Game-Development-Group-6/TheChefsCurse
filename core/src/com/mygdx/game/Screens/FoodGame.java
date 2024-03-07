package com.mygdx.game.Screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.Room.RoomBuilder;
import com.mygdx.game.gamesave.GameSaveLoader;
import com.mygdx.game.helpers.AnimationParameters;
import com.mygdx.game.helpers.ShadersHelper;
import com.mygdx.game.physics.Player;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.game.Room.Furniture;
import com.mygdx.game.Room.FurnitureBuilder;
import com.mygdx.game.Room.Room;
import com.mygdx.game.physics.Bullet;
import com.mygdx.game.physics.DynamicObject;
import com.mygdx.game.physics.EnemiesGenerator;
import com.mygdx.game.physics.Enemy;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.mygdx.game.helpers.SoundPaths;

/**
 * Main class of the round, game itself
 * <p>
 * Please see the {@link com.mygdx.game.Screens.FoodGame}
 *
 * @author Gines Moratalla, Juozas Skarbalius, McCarronr
 */
public class FoodGame implements Screen {
    /**
     * LIBGdx Objects
     */
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;

    /**
     * Native fields
     */
    private final Menu game;

    /**
     * Visual layer for interactive game elements such as buttons, status fields, round number and etc.
     */
    private final Overlay overlay;

    /**
     * Current room (round) reference
     */
    private final Room currentRoom;

    /**
     * Helpers and Builder fields
     */
    private final ShadersHelper shadersHelper;
    private final EnemiesGenerator enemiesGenerator;

    /**
     * Reference to Player object
     */
    private final Player player1;

    private float timePassed;

    /**
     * Indicates whether game is paused
     */
    private boolean pausedGameplay;

    /**
     * List of all dynamic entities in the game
     */
    private ArrayList<Object> entityList;

    /**
     * XP animation list
     */
    private final ArrayList<AnimationParameters> xpList;

    /**
     * Sound effects
     */
    private final Sound enemyDies = Gdx.audio.newSound(Gdx.files.internal(SoundPaths.ENEMYDEAD_PATH));
    public Music gameMusic = Gdx.audio.newMusic(Gdx.files.internal(SoundPaths.MUSIC_PATH));
    public SoundPaths soundPaths = SoundPaths.getInstance();

    public FoodGame(Menu game) {
        this.game = Menu.getInstance();
        this.entityList = new ArrayList<>();
        this.xpList = new ArrayList<>();

        shadersHelper = new ShadersHelper();
        FurnitureBuilder furnitureBuilder = new FurnitureBuilder();
        pausedGameplay = false;
        batch = new SpriteBatch();

        // Random Room from the 9 choices
        currentRoom = RoomBuilder.init().withRandomRoomType().create(game).get();

        Vector2 spawn = currentRoom.entitySpawn(currentRoom.getBackground(), 450, 0);
        player1 = new Player(spawn.x, spawn.y, 450, 500, game);
        entityList.add(player1);

        // Add music settings
        gameMusic.setLooping(true);
        gameMusic.setVolume(soundPaths.getMusicVolume());

        // Camera & Overlay
        camera = new OrthographicCamera(2560, 1440);
        overlay = new Overlay(this, gameMusic);

        // Initializing Helpers
        shapeRenderer = new ShapeRenderer();
        enemiesGenerator = new EnemiesGenerator(entityList, currentRoom, game);
        furnitureBuilder.create(currentRoom.getRoomType(), entityList);
        enemiesGenerator.generate();
    }

    public void pause() {
        pausedGameplay = !pausedGameplay;
        if (!pausedGameplay) return;
        gameMusic.pause();
        game.setScreen(new Pause(game, this));
    }

    public void resume() {
    }

    public void resize(int width, int height) {
    }

    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    public void show() {
        gameMusic.play();
    }

    public void dispose() {
        game.batch.dispose();
        player1.dispose();
        shapeRenderer.dispose();
        entityList = null;
        gameMusic.stop();
        gameMusic.dispose();
        enemyDies.dispose();
    }

    public void setPaused(boolean flag) {
        this.pausedGameplay = flag;
    }

    /**
     * <p>
     * Checks current game status and redirects to appropriate screen cover;
     * </br>
     * There are currently two game statuses supported by this method:
     * </br>
     * PAUSED - shows pause screen
     * </br>
     * DEATH - shows death screen
     * </p>
     *
     * @since 1.0
     */
    private void redirectToStateScreen() {
        // Handle Pausing the Game and showing Pause Screen
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) pause();
        if (pausedGameplay) return;

        // If player dies show death screen
        if (player1.getCurrentHealth() <= 0) {
            pausedGameplay = true;
            game.setScreen(new GameOver(game, this));
        }
    }

    /**
     * <p>
     * Renders the room and moves game camera to it's right position
     * </p>
     *
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-131">GD-101: [PHYSICS] Camera should follow the player [XS]</a>
     * @since 1.0
     */
    private void renderCurrentRoom() {
        // Start rendering
        ScreenUtils.clear(0, 0, 0, 0);
        batch.begin();
        currentRoom.render(player1, camera);

        // Moving the camera
        float centerX = player1.getHitbox().getX() + player1.getHitbox().getWidth() / 2;
        float centerY = player1.getHitbox().getY() + player1.getHitbox().getHeight() / 2;
        camera.position.set(centerX, centerY, 0);
        camera.update();
    }

    /**
     * <p>
     * Renders bullets
     * </p>
     *
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-92">[PHYSICS & LOGIC] The enemy can damage player (through shooting & punching) [M]</a>
     * @since 1.0
     */
    private void renderBullets(List<Object> entityList, Player player1) {
        for (Bullet bullet : player1.getAmmunition()) {
            bullet.update();
            bullet.render(batch);

            for (Object entity : entityList) {

                if (!(entity instanceof Enemy)) continue;
                Enemy currentEnemy = (Enemy) entity;

                if (bullet.getHitbox().overlaps(currentEnemy.getHitbox())) {

                    currentEnemy.setHit(true);
                    currentEnemy.setTimeHit();
                    currentEnemy.takeDamage(bullet.getDamage());
                    player1.getAmmunition().remove(bullet);
                }
            }
            if (bullet.getDespawnTime() < System.currentTimeMillis()) {
                player1.getAmmunition().remove(bullet);
            }
        }
    }

    /**
     * <p>
     * Shows and renders animations when the enemy is killed
     * </p>
     *
     * @since 1.0
     */
    private void showDeadEnemyAnimations(List<Object> entityList) {
        // Dead enemies (create XP animation)
        for (int i = entityList.size() - 1; i >= 0; i--) {
            if (entityList.get(i) instanceof Enemy) {
                Enemy temp = (Enemy) entityList.get(i);
                if (temp.getIsDead()) {
                    enemyDies.play(soundPaths.getVolume());
                    // CreateXP Animation
                    AnimationParameters animation = new AnimationParameters(
                            game.getXpAnimationHelper().get10xp(),
                            temp.getHitbox().getX() + temp.getHitbox().getWidth() / 2,
                            temp.getHitbox().getY() + temp.getHitbox().getHeight() / 2,
                            System.currentTimeMillis()
                    );

                    xpList.add(animation);
                    game.getXpAnimationHelper().get10xp();

                    entityList.remove(temp);
                    enemiesGenerator.enemyKilled();
                    game.increaseXP();
                }
            }
        }

        // Draw xp animation if an enemy is killed
        Iterator<AnimationParameters> iterator = xpList.iterator();
        while (iterator.hasNext()) {
            AnimationParameters params = iterator.next();
            long elapsedTime = System.currentTimeMillis() - params.getTimeStarted();
            if (elapsedTime < 500) {
                batch.draw(params.geAnimation().getKeyFrame(timePassed, true),
                        params.getX(), params.getY(), 200, 170);
            } else {
                iterator.remove();
            }
        }
    }

    /**
     * <p>
     * Renders red shaders when enemy gets damaged by the player
     * </p>
     *
     * @since 1.0
     */
    private void renderShaders() {
        for (Object object : entityList) {
            if (object instanceof DynamicObject) {
                DynamicObject entity = (DynamicObject) object;
                if (entity.getHit()) {
                    shadersHelper.drawshader(entity, timePassed, camera);
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        redirectToStateScreen();

        renderCurrentRoom();

        // Times to get current delta and to follow the player vector for the enemy
        float timeBetweenRenderCalls = Gdx.graphics.getDeltaTime();
        timePassed += Gdx.graphics.getDeltaTime();

        // Render dynamic objects on the screen
        renderEntities(enemiesGenerator, entityList, player1, timePassed, timeBetweenRenderCalls);

        // Player's bullets
        renderBullets(entityList, player1);

        showDeadEnemyAnimations(entityList);

        batch.end();

        GameSaveLoader.getInstance()
                .health(player1.getCurrentHealth())
                .enemiesLeft(enemiesGenerator.getEnemiesLeft())
                .withTimestamp(System.currentTimeMillis())
                .update();

        // Render shaders (red hitmarker when you hit an entity)
        renderShaders();

        // Render overlay elements
        overlay.render(player1, enemiesGenerator.getEnemiesLeft());
    }

    public float getTimePassed() {
        return timePassed;
    }

    /**
     * <p>
     * Renders all <i>static</i> and <i>dynamic</i> objects. Firstly, this method request next batch of enemies
     * from enemiesGenerator, then we sort all objects by yPos, then by applying casting we render them on screen
     * and check for collision between different objects.
     * </p>
     *
     * @param enemiesGenerator       - reference to EnemiesGenerator obj
     * @param entityList             - list of all static and dynamic entities (objects) in the game
     * @param player                 - reference to Player obj
     * @param timePassed             - current timestamp delta
     * @param timeBetweenRenderCalls - time passed between different render calls
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-183">GD-183</a>
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-104">GD-104: Make interceptor logic global to dynamic objects</a>
     * @since 1.0
     */
    public void renderEntities(EnemiesGenerator enemiesGenerator, List<Object> entityList, Player player, float timePassed, float timeBetweenRenderCalls) {
        // Get and render next batch of random enemies
        enemiesGenerator.getNextBatchOfEnemies(timeBetweenRenderCalls);

        // Order the list of entities by yPos
        entityList.sort(new Comparator<Object>() {
            public int compare(Object entity1, Object entity2) {
                DynamicObject dynamicObject1 = (DynamicObject) entity1;
                DynamicObject dynamicObject2 = (DynamicObject) entity2;
                return Double.compare(dynamicObject2.getSprite().getY(), dynamicObject1.getSprite().getY());
            }
        });

        for (Object entity : entityList) {
            if (entity instanceof Player) {
                player1.render(batch, this, camera);
            } else if (entity instanceof Enemy) {
                Enemy enemy = (Enemy) entity;

                enemy.render(
                        timePassed,
                        timeBetweenRenderCalls,
                        batch,
                        player,
                        this,
                        currentRoom.getBackground()
                );
            }
            DynamicObject collission = (DynamicObject) entity;
            currentRoom.checkCollission(collission, currentRoom.getBackground());

            if (entity instanceof Furniture) {
                Furniture furniture = (Furniture) entity;
                furniture.render(batch);
            }
        }
    }

    public EnemiesGenerator getEnemiesGenerator() {
        return enemiesGenerator;
    }
}