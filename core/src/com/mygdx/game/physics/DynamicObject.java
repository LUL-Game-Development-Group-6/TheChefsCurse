package com.mygdx.game.physics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Screens.FoodGame;
import com.mygdx.game.misc.Coords;
import com.mygdx.game.misc.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class DynamicObject {
    private static int[] healthLevels = {0,10,25,40,50,60,75,80,100};
    private int MAX_HEALTH;
    private int currentHealth;
    protected float speed;
    private double attack;
    private Coords coordinates;
    private int ID;
    private boolean isPlayer;

    protected Rectangle hitbox;
    protected Sprite sprite;
    protected Texture texture;

    private List<Pair> healthTextures;

    private Texture currentEntityHealth;

    public void takeDamage(int damage){
		this.currentHealth = this.currentHealth - damage;
		if (this.currentHealth <= 0){
			System.out.println("Entity should die");
		}
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public Coords getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coords coordinates) {
        this.coordinates = coordinates;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    // more rectangle stuff
    // Outline hitbox for testing
    public void renderHitbox(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
        shapeRenderer.end();
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
    public void render(float timePassed, float timeBetweenRenderCalls, Vector2 playerPosition, SpriteBatch batch, Player player) {}
    public Animation<Sprite> getEnemyAnimation() {return null;}
    public float getHeight() {return 0;}
    public float getWidth() {return 0;}
    public Vector2 getPreviousPos() {return null;}
    public Vector2 getPreviousSprite() {return null;}
    public void moveBack(Vector2 Hitbox, Vector2 spriteVector) {}

    // Can't do switch case with ranges, thats why I used this disposition
    public void healthPercentage() {

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

}