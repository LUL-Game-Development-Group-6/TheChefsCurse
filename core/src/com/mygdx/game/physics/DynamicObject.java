package com.mygdx.game.physics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.misc.Coords;

public abstract class DynamicObject {
        
    private int currentHealth;
    protected float speed;
    private double attack;
    private Coords coordinates;
    private int ID;
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

    public void healthPercentage() {

        int threshold = currentHealth / 10 * 10;

        switch (threshold) {
            case 0:
                currentEntityHealth = health_0;
                break;
            case 10:
                currentEntityHealth = health_10;
                break;
            case 20:
                currentEntityHealth = health_25;
                break;
            case 30:
                currentEntityHealth = health_40;
                break;
            case 40:
                currentEntityHealth = health_50;
                break;
            case 50:
                currentEntityHealth = health_60;
                break;
            case 60:
                currentEntityHealth = health_75;
                break;
            case 70:
                currentEntityHealth = health_80;
                break;
            default:
                currentEntityHealth = health_100;
                break;
        }
    }

    public Texture getHealthSprite() {
        return currentEntityHealth;
    }
}