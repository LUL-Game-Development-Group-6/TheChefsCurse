package com.mygdx.game.physics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EnemiesGenerator {
    private static final int MAX_ENEMY_POOL_SIZE = 10;
    private EnemyFactory factory;
    private float timeElapsedSinceLastSpawn;
    private int enemiesSpawned;
    private List<Enemy> recentlySpawnedEnemies;

    public EnemiesGenerator() {
        factory = EnemyFactory.getInstance();
        timeElapsedSinceLastSpawn = 0;
        enemiesSpawned = 0;
        recentlySpawnedEnemies = new ArrayList<>();
    }

    public void generate() {
        for(int i = 0; i<MAX_ENEMY_POOL_SIZE; i++) {
            Enemy enemy = factory.withRandomType()
                    .withDimensions(56,185)
                    .withRandomPosition()
                    .build();
        }
    }

    public void renderAllEnemies(float renderTime, Vector2 playerPosition, SpriteBatch batch, float playerSize,
                                 float delta) {
        // every 15 seconds
        if(timeElapsedSinceLastSpawn >= 5 && enemiesSpawned <= MAX_ENEMY_POOL_SIZE) {
            // spawn random batch of enemies
            int enemiesToSpawn = generateNextRandomChuckSize();
            System.out.println("Spawning... === " + enemiesToSpawn + " ...... " + enemiesSpawned + " ---- " + recentlySpawnedEnemies.size());
            int diff = enemiesToSpawn + enemiesSpawned;
            for(int i = enemiesSpawned - 1; i < diff; i++) {
                try {
                    Enemy enemy = factory.getEnemies().get(i);
                    renderEnemy(enemy, batch, renderTime, playerPosition, playerSize);
                } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("[WARN] Unable to spawn enemy. " + e);
                }
                enemiesSpawned++;
            }

            // reset time tracker
            timeElapsedSinceLastSpawn = 0;
        } else if(enemiesSpawned == 0){
            try {
                Enemy enemy = factory.getEnemies().get(0);
                renderEnemy(enemy, batch, renderTime, playerPosition, playerSize);
            } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                System.out.println("[WARN] Unable to spawn enemy. " + e);
            }
            timeElapsedSinceLastSpawn = 0;
            enemiesSpawned++;
        } else {
            timeElapsedSinceLastSpawn += delta;
        }

        // render all drawn enemies
        renderRecentlySpawnedEnemies(batch, renderTime, playerPosition, playerSize);
    }

    private void renderEnemy(Enemy enemy, SpriteBatch batch, float renderTime, Vector2 playerPosition,
                             float playerSize) {
        System.out.println("adding + " + recentlySpawnedEnemies.size());
        recentlySpawnedEnemies.add(enemy);
    }

    private void renderRecentlySpawnedEnemies(SpriteBatch batch, float renderTime, Vector2 playerPosition,
                                              float playerSize) {
        for(Enemy enemy : recentlySpawnedEnemies) {
            batch.draw(enemy.getSprite(), enemy.getSprite().getX(), enemy.getSprite().getY(), playerSize, playerSize);
            enemy.render(renderTime, playerPosition);
        }
    }

    private int generateNextRandomChuckSize() {
        if(MAX_ENEMY_POOL_SIZE - enemiesSpawned > 0) {
            int maxBound = MAX_ENEMY_POOL_SIZE - enemiesSpawned;
            int minBound = 1;
            try {
                return ThreadLocalRandom.current().nextInt(minBound, maxBound);
            } catch (IllegalArgumentException e) {
                return 0;
            }
        }
        return -1;
    }
}
