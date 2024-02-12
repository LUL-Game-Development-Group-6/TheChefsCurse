package com.mygdx.game.physics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EnemiesGenerator {
    private static final int MAX_ENEMY_POOL_SIZE = 10;
    private int enemiesLeft = MAX_ENEMY_POOL_SIZE;
    private EnemyFactory factory;
    private Room room;
    private float timeElapsedSinceLastSpawn;
    private int enemiesSpawned;
    private List<Object> recentlySpawnedEnemies;

    public EnemiesGenerator() {
        factory = EnemyFactory.getInstance();
        timeElapsedSinceLastSpawn = 0;
        enemiesSpawned = 0;
        recentlySpawnedEnemies = new ArrayList<>();
    }

    public EnemiesGenerator(List<Object> entityList, Room room) {
        this.room = room; 
        factory = EnemyFactory.getInstance();
        timeElapsedSinceLastSpawn = 0;
        enemiesSpawned = 0;
        recentlySpawnedEnemies = entityList;
    } 

    public void generate() {
        for(int i = 0; i<MAX_ENEMY_POOL_SIZE; i++) {
            Enemy enemy = factory.withRandomType()
                    .withRandomPosition(room)
                    .build();
        }
    }

    public void getNextBatchOfEnemies(float delta) {
        // every 15 seconds
        if(timeElapsedSinceLastSpawn >= 5 && enemiesSpawned <= MAX_ENEMY_POOL_SIZE) {
            // spawn random batch of enemies
            int enemiesToSpawn = generateNextRandomChuckSize();
            int diff = enemiesToSpawn + enemiesSpawned;
            for(int i = enemiesSpawned - 1; i < diff; i++) {
                try {
                    recentlySpawnedEnemies.add(factory.getEnemies().get(i));
                } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("[WARN] Unable to spawn enemy. " + e);
                }
                enemiesSpawned++;
            }

            // reset time tracker
            timeElapsedSinceLastSpawn = 0;
        } else if(enemiesSpawned == 0) {
            try {
                recentlySpawnedEnemies.add(factory.getEnemies().get(0));
            } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                System.out.println("[WARN] Unable to spawn enemy. " + e);
            }
            timeElapsedSinceLastSpawn = 0;
            enemiesSpawned++;
        } else {
            timeElapsedSinceLastSpawn += delta;
        }
    }

    private int generateNextRandomChuckSize() {
        if(MAX_ENEMY_POOL_SIZE - enemiesSpawned > 0) {
            int maxBound = MAX_ENEMY_POOL_SIZE - enemiesSpawned;
            int minBound = 1;
            try {
                return MathUtils.random(minBound, maxBound);
            } catch (IllegalArgumentException e) {
                return 0;
            }
        }
        return -1;
    }

    public static int getPoolSize() {
        return MAX_ENEMY_POOL_SIZE;
    }

    public int getEnemiesSpawned() {
        return enemiesSpawned;
    }

    public int getEnemiesLeft() {
        return enemiesLeft;
    }

    public void enemyKilled() {
        enemiesLeft--;
    }

    public void reset() {
        factory.kill();
    }
}
