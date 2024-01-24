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

    public void renderAllEnemies(float renderTime, Vector2 playerPosition, SpriteBatch batch, float playerSize) {
        for(Enemy enemy : factory.getEnemies()) {
            batch.draw(enemy.getSprite(), enemy.getSprite().getX(), enemy.getSprite().getY(), playerSize, playerSize);
            enemy.render(renderTime, playerPosition);
        }
    }
}
