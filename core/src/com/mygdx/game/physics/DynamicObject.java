package com.mygdx.game.physics;

import com.mygdx.game.misc.Coords;

public abstract class DynamicObject {
    protected final static int MAX_HEALTH = 100;

    private int currentHealth;
    private double speed;
    private double attack;
    private Coords coordinates;
    private int ID;

    public void move(Coords to) {

    }

    public void takeDamage(int damage){

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

    public void setSpeed(double speed) {
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
}
