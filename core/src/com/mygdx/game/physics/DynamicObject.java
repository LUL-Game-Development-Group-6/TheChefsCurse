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

public abstract class DynamicObject {
    
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

    private Texture health_0;
    private Texture health_10;
    private Texture health_25;
    private Texture health_40;
    private Texture health_50;
    private Texture health_60;
    private Texture health_75;
    private Texture health_80;
    private Texture health_100;

    private Texture currentEntityHealth;

    public void move(Coords to) {

    }

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

        health_0 = new Texture("health/health_0.png");
        health_10 = new Texture("health/health_10.png");
        health_25 = new Texture("health/health_25.png");
        health_40 = new Texture("health/health_40.png");
        health_50 = new Texture("health/health_50.png");
        health_60 = new Texture("health/health_60.png");
        health_75 = new Texture("health/health_75.png");
        health_80 = new Texture("health/health_80.png");
        health_100 = new Texture("health/health_100.png");

        currentEntityHealth = health_100;
    }

    // Methods to be overwritten
    public void render(SpriteBatch batch, FoodGame game, OrthographicCamera camera) {}
    public void render(float deltaTime, Vector2 playerPosition) {}
    public Animation<Sprite> getEnemyAnimation() {return null;}
    public void enemyHit(Vector2 playerPosition, Player player) {}
    public float getHeight() {return 0;}
    public float getWidth() {return 0;}

    // Can't do switch case with ranges, thats why I used this disposition
    public void healthPercentage() {

        int threshold = (100 * currentHealth) / MAX_HEALTH;

        if (threshold <= 0) {
            currentEntityHealth = health_0;
        } else if (threshold > 0 && threshold <= 10) {
            currentEntityHealth = health_10;
        } else if (threshold > 10 && threshold <= 25) {
            currentEntityHealth = health_25;
        } else if (threshold > 25 && threshold <= 40) {
            currentEntityHealth = health_40;
        } else if (threshold > 40 && threshold <= 50) {
            currentEntityHealth = health_50;
        } else if (threshold > 50 && threshold <= 60) {
            currentEntityHealth = health_60;
        } else if (threshold > 60 && threshold <= 75) {
            currentEntityHealth = health_75;
        } else if (threshold > 75 && threshold <= 80) {
            currentEntityHealth = health_80;
        } else if (threshold > 80 && threshold <= 100) {
            currentEntityHealth = health_100;
        }
        
    }

    public Texture getHealthSprite() {
        return currentEntityHealth;
    }

    public void setMaxHealth(int health) {
        this.MAX_HEALTH = health;
    }

}