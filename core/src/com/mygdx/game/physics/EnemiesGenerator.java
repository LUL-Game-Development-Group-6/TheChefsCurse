package com.mygdx.game.physics;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Room.Room;
import com.mygdx.game.Screens.Menu;

import java.util.List;

/**
 * Enemy Generator class. Responsible for generating random enemy batches
 * @author Juozas Skarbalius
 */
public class EnemiesGenerator {
    private final int MAX_ENEMY_POOL_SIZE;
    private int enemiesLeft;
    private final EnemyFactory factory;
    private final Room room;
    private float timeElapsedSinceLastSpawn;
    private int enemiesSpawned;
    private final List<Object> recentlySpawnedEnemies;
    private final Menu game;

    public EnemiesGenerator(List<Object> entityList, Room room, Menu game) {
        this.room = room; 
        this.game = game;
        MAX_ENEMY_POOL_SIZE = room.getPoolSize();
        enemiesLeft = MAX_ENEMY_POOL_SIZE;
        factory = EnemyFactory.getInstance();
        timeElapsedSinceLastSpawn = 0;
        enemiesSpawned = 0;
        recentlySpawnedEnemies = entityList;
    } 

    public void generate() {
        for(int i = 0; i<MAX_ENEMY_POOL_SIZE; i++) {
            Enemy enemy = factory.withRandomType()
                    .withRandomPosition(room)
                    .build(this.game);
        }
    }

    public void getNextBatchOfEnemies(float delta) {
        // every 5 seconds generate batch if pool is not full
        if(timeElapsedSinceLastSpawn >= 5 && enemiesSpawned <= MAX_ENEMY_POOL_SIZE) {
            // get random size of enemy chunk
            int enemiesToSpawn = generateNextRandomChuckSize();

            // get new number of enemies
            int diff = enemiesToSpawn + enemiesSpawned;
            for(int i = enemiesSpawned; i < diff; i++) {
                try {
                    Enemy enemyToAdd = factory.getEnemies().get(i);
                    if(!recentlySpawnedEnemies.contains(enemyToAdd)) {

                        recentlySpawnedEnemies.add(enemyToAdd);
                        enemiesSpawned++;
                    }

                } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("[WARN] Unable to spawn enemy. " + e);
                }
            }
            // reset time tracker
            timeElapsedSinceLastSpawn = 0;

        } else if(enemiesSpawned == 0) {
            
            try {
                Enemy enemyToAdd = factory.getEnemies().get(0);
                if(!recentlySpawnedEnemies.contains(enemyToAdd)) {
                    recentlySpawnedEnemies.add(enemyToAdd);
                }
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
            int maxBound = Math.min(4, MAX_ENEMY_POOL_SIZE - enemiesSpawned);
            int minBound = 1;
            try {
                return MathUtils.random(minBound, maxBound);
            } catch (IllegalArgumentException e) {
                return 0;
            }
        }
        return 0;
    }

    public int getEnemiesLeft() {
        return this.enemiesLeft;
    }

    public void enemyKilled() {
        this.enemiesLeft--;
    }

    public void reset() {
        factory.kill();
    }
}
