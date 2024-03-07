package com.mygdx.game.physics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Screens.FoodGame;
import com.mygdx.game.misc.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic Object class.
 * Inherited by Player, Enemy, Bullet and Furniture classes
 * @author Gines Moratalla, Juozas Skarbalius, Macaron
 */
public abstract class DynamicObject {
    private static final int[] healthLevels = {10,25,40,50,60,75,80,100};
    private boolean collided;
    private int MAX_HEALTH;
    private int currentHealth;
    protected float speed;
    private boolean isPlayer;

    protected Rectangle hitbox;
    protected Sprite sprite;
    protected Texture texture;

    private boolean hit;
    private long timeHit;

    private List<Pair> healthTextures;

    private Texture currentEntityHealth;

    public void takeDamage(int damage){
		this.currentHealth = this.currentHealth - damage;
    }

    public boolean getCollided() {
        return collided;
    }

    public void setCollided(boolean bool) {
        collided = bool;
    }

    public long getTimeHit() {
        return timeHit;
    }

    public void setTimeHit() {
        timeHit = System.currentTimeMillis();
    }
    
    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setPlayer(boolean player) {
        this.isPlayer = player;
    }

    public boolean getPlayerBool() {
        return this.isPlayer;
    }

    public void createHealth() {
        healthTextures = new ArrayList<>();

        for(int i = 0; i<healthLevels.length; i++) {
            healthTextures.add(new Pair(i, new Texture("health/health_" + healthLevels[i] + ".png")));
        }

        currentEntityHealth = (Texture) healthTextures.get(healthLevels.length - 1).getSecond();
    }

    // Methods to be overwritten
    public void render(SpriteBatch batch, FoodGame game, OrthographicCamera camera) {}
    public void render(float timePassed, float timeBetweenRenderCalls, SpriteBatch batch, Player player, FoodGame game, TiledMap map) {}
    public Animation<Sprite> getEnemyAnimation() {return null;}
    public float getHeight() {return 0;}
    public float getWidth() {return 0;}
    public Vector2 getPreviousPos() {return null;}
    public Vector2 getPreviousSprite() {return null;}
    public void moveBack(Vector2 Hitbox, Vector2 spriteVector) {}
    public void setHit(boolean isHit) {hit = isHit;}
    public boolean getHit() {return this.hit;}
    public Animation<Sprite> getAnimation() {return null;}

    /**
     * <p>
     *     Method that updates health bar's texture according to the amount of health left
     * </p>
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-98">GD-98: [PHYSICS & LOGIC] The enemy can damage player (through shooting & punching) [M]</a>
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-60">GD-60: [LOGIC] Health & damage mechanism [M]</a>
     * @since 1.0
     */
    public void healthPercentage() {
        if(MAX_HEALTH == 0) MAX_HEALTH = 100;
        int threshold = (100 * currentHealth) / MAX_HEALTH;

        for(int i = 0; i<healthLevels.length; i++) {
            if(threshold <= healthLevels[i]) {
                currentEntityHealth = (Texture) healthTextures.get(i).getSecond();
                break;
            }
        }
    }

    public Texture getHealthSprite() {
        return currentEntityHealth;
    }

    public void setMaxHealth(int health) {
        this.MAX_HEALTH = health;
    }

    public Sprite flipUpsideDown(Sprite frame) {
        frame.flip(false, true);
        return frame;
    }

    /**
     * <p>
     *     Method that generates sprites for supplied type of Enemy
     * </p>
     * @param quantity amount of sprites to generate
     * @param initialPath initial path (usually name of enemy type) of enemy type's textures
     * @param enemyAtlas reference to the enemy atlas
     * @return array of Sprite of fixed size determined by quantity
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-97">GD-97: [LOGIC] Spawn certain amount of enemies in a room [M]</a>
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-98">GD-98: [PHYSICS & LOGIC] The enemy can damage player (through shooting & punching) [M]</a>
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-60">GD-60: [LOGIC] Health & damage mechanism [M]</a>
     * @since 1.0
     */
    Sprite[] generateEnemyAtlasSprites(int quantity, String initialPath, TextureAtlas enemyAtlas, boolean... flip) {
        if(quantity < 1)
            throw new IllegalArgumentException("[ERROR] Quantity of sprites must be greater than 0");
        if(initialPath == null || initialPath.isEmpty())
            throw new IllegalArgumentException("[ERROR] initial path of sprites must not be empty or null");
        Sprite[] sprites = new Sprite[quantity];
        for(int i = 0; i<sprites.length; i++) {
            if(flip.length > 0 && flip[0]) {
                sprites[i] = flipUpsideDown(enemyAtlas.createSprite(initialPath+(i+1)));
            } else {
                sprites[i] = enemyAtlas.createSprite(initialPath+(i+1));
            }

        }
        return sprites;
    }
}