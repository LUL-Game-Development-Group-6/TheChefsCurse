package com.mygdx.game.physics;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EnemyFactory {
    private static EnemyFactory singletonEnemyFactory;
    private List<Enemy> enemies;
    private Enemy.EnemyType type;
    private Vector2 position;
    private float width;
    private float height;

    // constructor that resets values
    public EnemyFactory() {
        enemies = new ArrayList<>();
        position = null;
        width = 0;
        height = 0;
        type = null;
    }

    // an enemy factory must be singleton
    public static EnemyFactory getInstance() {
        if(singletonEnemyFactory == null) {
            singletonEnemyFactory = new EnemyFactory();
        }
        return singletonEnemyFactory;
    }

    public EnemyFactory withType(Enemy.EnemyType type) {
        this.type = type;
        return this;
    }

    public EnemyFactory withRandomType() {
        int randomID = ThreadLocalRandom.current().nextInt(0, 3);
        if(randomID == 0) type = Enemy.EnemyType.HAMBURGER;
        if(randomID == 1) type = Enemy.EnemyType.HOTDOG;
        if(randomID == 2) type = Enemy.EnemyType.SODA;
        if(randomID == 3) type = Enemy.EnemyType.POPCORN;
        return this;
    }

    public EnemyFactory withPosition(Vector2 position) {
        this.position = position;
        return this;
    }

    public EnemyFactory withRandomPosition()
    {
        this.position = generateRandomCoords();
        return this;
    }

    public EnemyFactory withDimensions(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public Enemy build() {
        Enemy enemy = new Enemy(position, width, height, type);
        enemies.add(enemy);
        return enemy;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    private Vector2 generateRandomCoords() {
        float x = ThreadLocalRandom.current().nextFloat(400, 800);
        float y = ThreadLocalRandom.current().nextFloat(100, 600);

        for(Enemy enemy : enemies) {
            if(areObjectsNear(enemy.getSprite().getX(), enemy.getSprite().getY(), x, y, 20)) {
                generateRandomCoords();
            }
        }
        return new Vector2(x,y);
    }

    // Method to check if two objects are near each other
    public static boolean areObjectsNear(float x1, float y1, float x2, float y2, double radius) {
        // Calculate the distance between the two points using the distance formula
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

        // Check if the distance is less than or equal to the specified radius
        return distance <= radius;
    }
}