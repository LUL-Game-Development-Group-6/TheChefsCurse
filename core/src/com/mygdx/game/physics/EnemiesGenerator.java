package com.mygdx.game.physics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class EnemiesGenerator {
    private EnemyFactory factory;

    public EnemiesGenerator() {
        factory = EnemyFactory.getInstance();
    }

    public void generate(int amount) {
        for(int i = 0; i<amount; i++) {
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
