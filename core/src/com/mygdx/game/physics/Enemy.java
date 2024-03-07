package com.mygdx.game.physics;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Screens.FoodGame;
import com.mygdx.game.Screens.Menu;
import com.mygdx.game.helpers.SoundPaths;

/**
 * Enemy class.
 * Extends {@link com.mygdx.game.physics.DynamicObject} class.
 * Also refer to the {@link com.mygdx.game.Room.FurnitureBuilder}
 *
 * @author Gines Moratalla, Juozas Skarbalius, P. Gabrielius Modelevas
 */
public class Enemy extends DynamicObject {

    /**
     * Current position vector of the Enemy
     */
    private Vector2 position;

    /**
     * Current velocity vector (direction) of the Enemy
     */
    private Vector2 velocity;

    /**
     * Current bullet vector (shot by current Enemy)
     */
    private Vector2 bulletSpeed;

    /**
     * Previous enemy sprite (before changing direction)
     */
    private Vector2 previousSprite;

    /**
     * Previous position vector of the Enemy
     */
    private Vector2 previousPos;

    /**
     * Default speed value of the enemy
     */
    private float normalSpeed;

    /**
     * Width of the enemy
     */
    private float height;

    /**
     * Height of the enemy
     */
    private float width;

    /**
     * Current Enemy texture applied
     */
    private Texture enemyTexture;

    /**
     * Amount of damage (in percentage) that a current enemy can inflict
     * on a Player.
     */
    private int damage;

    /**
     * X Offset required to display enemy's health bar correctly
     */
    private int offsetX;

    /**
     * Y Offset required to display enemy's health bar correctly
     */
    private int offsetY;

    /**
     * Time in which last shoot action was performed.
     * In other words, time when enemy shot the last bullet
     */
    private long lastShot;

    /**
     * Cool downtime for an enemy.
     * Needed to make sure that an Enemy cannot shoot too frequently,
     * as without it game would be unplayable
     * or user experience would be significantly downgraded.
     */
    private long cooldown;

    /**
     * Distance in pixels (px) that determines of how far bullets shot
     * by current type of Enemy can reach other dynamic objects.
     * In other words, the maximum distance of a bullet that current Enemy can shoot.
     */
    private int hitDistance;

    /**
     * Stores current animation applied on an Enemy
     */
    private Animation<Sprite> enemyAnimation;

    /**
     * A list of Bullets at current Enemy's possession.
     */
    private LinkedList<Bullet> enemyAmmunition;

    /**
     * If true, the enemy was killed. Otherwise, the Enemy should remain active
     * in the gameplay
     */
    private boolean isDead;

    private Menu game;

    /**
     * Sound Effects
     */
    private SoundPaths soundPaths = SoundPaths.getInstance();
    private Sound damageSound = Gdx.audio.newSound(Gdx.files.internal(SoundPaths.ENEMYHIT_PATH));
    private Sound enemyShootSound = Gdx.audio.newSound(Gdx.files.internal(SoundPaths.ENEMYGUN_PATH));

    /**
     * Enum used to identify Enemy type
     */
    public enum EnemyType {
        HAMBURGER,
        HOTDOG,
        POPCORN,
        SODA,
        ;
    }

    /**
     * Enemy type field reference
     */
    private EnemyType enemyType;

    /**
     * <p>
     * Constructor of {@link com.mygdx.game.physics.Enemy} class.
     * Responsible for:
     *     <ul>
     *         <li>Initializing field variables</li>
     *         <li>Adjusting damage variable</li>
     *         <li>Adjusting enemy class to its assigned type</li>
     *         <li>Assigning dimensions and initial position</li>
     *         <li>Creating hitbox</li>
     *     </ul>
     * </p>
     *
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-103">GD-103: [PHYSICS] Improve and customize enemy AI [XL]</a>
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-97">GD-97: [LOGIC] Spawn certain amount of enemies in a room [M]</a>
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-95">GD-95: [LOGIC] Generate initial positions of dynamic objects randomly</a>
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-98">GD-98: [PHYSICS & LOGIC] The enemy can damage player (through shooting & punching) [M]</a>
     * @since 1.0
     */
    public Enemy(Menu game, Vector2 position, float width, float height, EnemyType enemyType) {
        super.createHealth();
        this.setPlayer(false);
        this.game = game;

        // initialize field variables with default values
        enemyAmmunition = new LinkedList<Bullet>();

        // adjust enemy's properties according to its type
        assignEnemyType(enemyType, game);

        // adjust damage variable according to current level
        damage += game.getStatsHelper().getEnemyScaler();

        this.isDead = false;
        this.setMaxHealth(this.getCurrentHealth());
        this.position = position;
        this.height = height;
        this.width = width;
        this.velocity = new Vector2(1, 1);
        this.lastShot = 0;
        this.enemyType = enemyType;
        this.setHit(false);
        normalSpeed = this.getSpeed();

        // set enemy's dimensions and initial position
        Sprite enemySprite = new Sprite(enemyTexture);
        enemySprite.setSize(width, height);
        enemySprite.setPosition(position.x, position.y);

        // set enemy's hitbox
        this.setHitbox(new Rectangle(0, 0, width - width / 5, height - height / 8));
        previousPos = new Vector2(this.getHitbox().x, this.getHitbox().y);
        previousSprite = new Vector2();
        bulletSpeed = new Vector2();

        setSprite(enemySprite);
    }

    /**
     * <p>
     * Method that assigns the right values of fields according to EnemyType. For example,
     * this method adjusts the following (but not limited to):
     * Responsible for:
     *     <ul>
     *         <li>Damage</li>
     *         <li>Dimensions (width and height)</li>
     *         <li>Offsets</li>
     *         <li>Textures and sprites</li>
     *         <li>Speed</li>
     *         <li>Maximum health value</li>
     *     </ul>
     * </p>
     *
     * @param enemyType type of enemy
     * @param game      reference to the entry game class (Menu)
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-97">GD-97: [LOGIC] Spawn certain amount of enemies in a room [M]</a>
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-98">GD-98: [PHYSICS & LOGIC] The enemy can damage player (through shooting & punching) [M]</a>
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-60">GD-60: [LOGIC] Health & damage mechanism [M]</a>
     * @since 1.0
     */
    private void assignEnemyType(EnemyType enemyType, Menu game) {
        switch (enemyType) {
            case HAMBURGER:
                this.damage = 10;
                this.hitDistance = 50;
                this.cooldown = 2000;
                offsetX = -25;
                offsetY = 20;
                TextureAtlas enemyAtlas = new TextureAtlas("enemies/Hamburguer/hamburguer.atlas");

                this.enemyAnimation = new Animation<>(
                        1 / 15f,
                        generateEnemyAtlasSprites(10, "Hamburguer_Sprite", enemyAtlas));

                enemyTexture = new Texture("enemies/Hamburguer/Hamburguer_Standing.png");
                setCurrentHealth(100 + game.getStatsHelper().getEnemyScaler());
                setSpeed(200);
                break;

            case HOTDOG:
                this.damage = 8;
                this.hitDistance = 1050;
                this.cooldown = 5000;
                offsetX = 20;
                offsetY = -10;
                enemyAtlas = new TextureAtlas("enemies/Hotdog/hotdog.atlas");
                enemyAnimation = new Animation<>(
                        1 / 8f,
                        generateEnemyAtlasSprites(4, "hotdog", enemyAtlas));

                enemyTexture = new Texture("enemies/Hotdog/hotdog_still.png");
                setCurrentHealth(60 + game.getStatsHelper().getEnemyScaler());
                setSpeed(50);
                break;

            case POPCORN:
                this.damage = 5;
                this.hitDistance = 800;
                this.cooldown = 3000;
                offsetX = -20;
                offsetY = 20;
                enemyAtlas = new TextureAtlas("enemies/Popcorn/popcorn.atlas");
                enemyAnimation = new Animation<>(
                        1 / 12f,
                        generateEnemyAtlasSprites(5, "Popcorn", enemyAtlas));

                enemyTexture = new Texture("enemies/Popcorn/Popcorn_Standing.png");
                setCurrentHealth(40 + game.getStatsHelper().getEnemyScaler());
                setSpeed(100);
                break;

            case SODA:
                this.damage = 15;
                this.hitDistance = 50;
                this.cooldown = 3000;
                offsetX = -20;
                offsetY = 20;
                enemyAtlas = new TextureAtlas("enemies/Soda/soda.atlas");
                enemyAnimation = new Animation<>(
                        1 / 20f,
                        generateEnemyAtlasSprites(14, "Soda_Sprite", enemyAtlas));


                enemyTexture = new Texture("enemies/Soda/Soda_Standing.png");

                setCurrentHealth(100 + game.getStatsHelper().getEnemyScaler());
                setSpeed(150);
                break;

            default:
                break;
        }
    }

    /**
     * <p>
     * Method that updates Enemy fields, position and other parameters.
     * </p>
     *
     * @param timePassed time passed between different calls of this method
     * @param deltaTime  time passed between different frames
     * @param player     reference to the Player object
     * @param batch      sprite batch of Enemy object
     * @param map        time map reference
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-97">GD-97: [LOGIC] Spawn certain amount of enemies in a room [M]</a>
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-98">GD-98: [PHYSICS & LOGIC] The enemy can damage player (through shooting & punching) [M]</a>
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-60">GD-60: [LOGIC] Health & damage mechanism [M]</a>
     * @since 1.0
     */
    public void update(float timePassed, float deltaTime, Player player, SpriteBatch batch, TiledMap map) {
        float distance = player.getPreviousPos().dst(position);

        // ENEMY ANIMATION
        // Draw enemy standing still if is close enough
        if (distance < this.hitDistance) {
            setSpeed(0);
            batch.draw(enemyTexture, this.getSprite().getX(), this.getSprite().getY(), this.getWidth(), this.getHeight());
        } else {
            setSpeed(normalSpeed);
            batch.draw(this.getEnemyAnimation().getKeyFrame(timePassed, true),
                    this.getSprite().getX(), this.getSprite().getY(), this.getWidth(), this.getHeight());
        }

        // draw enemie's health
        batch.draw(this.getHealthSprite(), this.getHitbox().getX() + offsetX, this.getHitbox().getY()
                + this.getHitbox().getHeight() + offsetY, this.getHealthSprite().getWidth() / 2, this.getHealthSprite().getHeight() / 2);
        this.healthPercentage();

        // Check enemy collision
        randomizeEnemyDirection(player, deltaTime, map);
    }

    /**
     * <p>
     * Method that renders (or re-renders) the Enemy object
     * </p>
     *
     * @param timePassed             time passed between different frames
     * @param timeBetweenRenderCalls time passed between different calls of this method
     * @param player                 reference to the Player object
     * @param batch                  sprite batch of Enemy object
     * @param map                    time map reference
     * @since 1.0
     */
    @Override
    public void render(float timePassed, float timeBetweenRenderCalls, SpriteBatch batch, Player player, FoodGame game, TiledMap map) {
        // Get previous position for colliders
        previousPos.set(this.getHitbox().x, this.getHitbox().y);
        previousSprite.set(this.getSprite().getX(), this.getSprite().getY());
        this.update(timePassed, timeBetweenRenderCalls, player, batch, map);
        System.out.println(this.enemyType);

        // Render shot bullets by the current Enemy
        for (Bullet bullet : enemyAmmunition) {
            bullet.update();
            bullet.renderEnemyBullet(batch, game);
            if (bullet.getHitbox().overlaps(player.getHitbox())) {
                player.setHit(true);
                player.setTimeHit();
                player.takeDamage(this.damage);
                this.getAmmunition().remove(bullet);
            }
            if (bullet.getDespawnTime() < System.currentTimeMillis()) {
                this.getAmmunition().remove(bullet);
            }
        }
    }

    /**
     * <p>
     * Method that applies damage to the Enemy inflicted by the player.
     * Note that there isn't friendly fire support.
     * </p>
     *
     * @param damage amount of damage inflicted to the current Enemy.
     * @since 1.0
     */
    @Override
    public void takeDamage(int damage) {
        damageSound.play(soundPaths.getVolume());
        this.setCurrentHealth(this.getCurrentHealth() - damage);
        if (this.getCurrentHealth() <= 0) {
            damageSound.dispose();
            enemyShootSound.dispose();
            System.out.println("Enemy should die");
            isDead = true;
        }
    }

    @Override
    public Animation<Sprite> getAnimation() {
        return enemyAnimation;
    }

    public LinkedList<Bullet> getAmmunition() {
        return this.enemyAmmunition;
    }

    @Override
    public float getHeight() {
        return this.height;
    }

    @Override
    public float getWidth() {
        return this.width;
    }

    @Override
    public Vector2 getPreviousPos() {
        return this.previousPos;
    }

    @Override
    public Vector2 getPreviousSprite() {
        return this.previousSprite;
    }

    public boolean getIsDead() {
        return isDead;
    }

    /**
     * <p>
     * Method that moves enemy to the new position supplied as arguments
     * </p>
     *
     * @param x new X coordinate of Enemy
     * @param y new Y coordinate of Enemy
     * @since 1.0
     */
    private void move(float x, float y) {
        System.out.println(this.getClass() + "  =====  " + x + ", " + y);
        sprite.setPosition(x - sprite.getWidth() / 2, y - sprite.getHeight() / 4);
        hitbox.setPosition(x - hitbox.getWidth() / 2, y - hitbox.getHeight() / 4);
    }

    /**
     * <p>
     * Method that moves enemy to the previous position
     * </p>
     *
     * @param Hitbox       hitbox of Enemy
     * @param spriteVector sprite vector of Enemy
     * @since 1.0
     */
    @Override
    public void moveBack(Vector2 Hitbox, Vector2 spriteVector) {
        sprite.setPosition(spriteVector.x, spriteVector.y);
        hitbox.setPosition(Hitbox.x, Hitbox.y);
    }

    @Override
    public Animation<Sprite> getEnemyAnimation() {
        return this.enemyAnimation;
    }

    /**
     * <p>
     * Method that establishes a routine of action when the player is hit
     * by the Player. In general, it does the following (but not limited to):
     *     <ul>
     *         <li>Sets player to its hit</li>
     *         <li>Initialized cooldown</li>
     *         <li>Plays sound indicating that player was hit</li>
     *     </ul>
     * </p>
     *
     * @param player reference of the Player object
     * @since 1.0
     */
    public void enemyHit(Player player) {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastShot = currentTime - lastShot;

        if (this.position.dst(player.getPreviousPos()) <= this.hitDistance && timeSinceLastShot >= this.cooldown) {

            switch (this.enemyType) {
                case HAMBURGER:
                    player.takeDamage(this.damage);
                    player.setHit(true);
                    player.setTimeHit();
                    break;
                case SODA:
                    player.takeDamage(this.damage);
                    player.setHit(true);
                    player.setTimeHit();
                    break;
                case POPCORN:
                    enemyShootSound.play(soundPaths.getVolume());
                    Bullet nextBullet1 = new Bullet(bulletSpeed.x, bulletSpeed.y, position.x, position.y + 50, Bullet.EnemyBullet.POPCORN_BULLET);
                    nextBullet1.setSpeed(0.15f);
                    nextBullet1.setDespawnTime(Bullet.EnemyBullet.HOTDOG_BULLET);
                    enemyAmmunition.add(nextBullet1);
                    break;
                case HOTDOG:
                    enemyShootSound.play(soundPaths.getVolume());
                    Bullet nextBullet2 = new Bullet(bulletSpeed.x, bulletSpeed.y, position.x, position.y + 50, Bullet.EnemyBullet.HOTDOG_BULLET);
                    nextBullet2.setSpeed(0.2f);
                    nextBullet2.setDespawnTime(Bullet.EnemyBullet.HOTDOG_BULLET);
                    enemyAmmunition.add(nextBullet2);
                    break;
                default:
                    break;
            }
            lastShot = currentTime;
        }
    }

    public void setBulletSpeed(Vector2 direction) {
        bulletSpeed.set(direction.x * normalSpeed, direction.y * normalSpeed);
    }

    /**
     * Method that randomizes enemy's direction. Generally, it does the following:
     * <ul>
     *     <li>Updates direction vector</li>
     *     <li>Checks for collision for the new collision vector</li>
     *     <li>Updates the speed of bullets</li>
     * </ul>
     *
     * @param player    Player reference
     * @param deltaTime time between render calls
     * @param map       tile map reference
     */
    public void randomizeEnemyDirection(Player player, float deltaTime, TiledMap map) {

        Vector2 direction = new Vector2(player.getPreviousPos().x - position.x, player.getPreviousPos().y - position.y);
        direction.nor();

        if (this.getCollided()) {
            whereIsPlayer(direction);
            if (isOutsideMap(map)) velocity.set(direction.x * -speed * 5, direction.y * -speed * 5);
        } else {

            /*
             * Case where enemy doesn't collide against anything, its direction will go towards player
             * Calculate the direction vector between the enemy and the player and normalize vector
             *
             * Scale the direction by the speed to get the velocity
             * creating a copy of the direction vector to avoid progresively increase speed
             */

            velocity.set(direction.x * speed, direction.y * speed);
            setBulletSpeed(direction);
        }
        // Update enemy position
        isOutsideMap(map);
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        move(position.x, position.y);
        enemyHit(player);
    }

    /**
     * With new AI logic, enemies sometimes jump out the map, this will teleport them back
     *
     * @param map tile map reference
     * @return true if enemy is outside the map
     */
    public boolean isOutsideMap(TiledMap map) {
        MapObjects colliderList = map.getLayers().get("colliders").getObjects();

        for (MapObject roomSpawn : colliderList) {
            if (roomSpawn instanceof PolygonMapObject) {
                Polygon triangleCollider = ((PolygonMapObject) roomSpawn).getPolygon();
                if (!triangleCollider.contains(this.getHitbox().getX(), this.getHitbox().getY())
                        || !triangleCollider.contains(this.getHitbox().getX() + this.getHitbox().getWidth(), this.getHitbox().getY())) {
                    this.moveBack(this.getPreviousPos(), this.getPreviousSprite());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method that will return a different speed direction according to where the enemy is
     * w.r.t. the player
     *
     * @param direction direction vector of current Enemy
     */
    public void whereIsPlayer(Vector2 direction) {
        if (direction.x >= 0 && direction.y >= 0) {
            velocity.set(direction.x * -speed * 2, direction.y * -speed);

        } else if (direction.x < 0 && direction.y >= 0) {
            velocity.set(direction.x * -speed, direction.y * -speed * 2);

        } else if (direction.x < 0 && direction.y < 0) {
            velocity.set(direction.x * -speed * 5, direction.y * -speed);

        } else if (direction.x >= 0 && direction.y < 0) {
            velocity.set(direction.x * -speed * 2, direction.y * -speed);
        }
    }
}