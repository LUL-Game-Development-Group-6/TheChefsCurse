package com.mygdx.game.physics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Room.Room;
import com.mygdx.game.Screens.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Enemy Factory/Builder class. Responsible for generating Enemy according to specifications.
 *
 * @author Juozas Skarbalius, Gines Moratalla
 */
public class EnemyFactory {
    private static EnemyFactory singletonEnemyFactory;
    private List<Enemy> enemies;
    private Enemy.EnemyType type;
    private Vector2 position;
    private float width;
    private float height;
    private boolean dimensionsSetByType;

    // constructor that resets values
    public EnemyFactory() {
        enemies = new ArrayList<>();
        position = null;
        width = 0;
        height = 0;
        type = null;
        dimensionsSetByType = false;
    }

    // an enemy factory must be singleton
    public static EnemyFactory getInstance() {
        if (singletonEnemyFactory == null) {
            singletonEnemyFactory = new EnemyFactory();
        }
        return singletonEnemyFactory;
    }

    protected EnemyFactory withRandomType() {
        int randomID = MathUtils.random(0, 3);

        if (randomID == 0) {
            type = Enemy.EnemyType.HAMBURGER;
            this.width = 300;
            this.height = 320;
        }
        if (randomID == 1) {
            type = Enemy.EnemyType.HOTDOG;
            this.width = 400;
            this.height = 300;
        }
        if (randomID == 2) {
            type = Enemy.EnemyType.POPCORN;
            this.width = 280;
            this.height = 300;
        }

        if (randomID == 3) {
            type = Enemy.EnemyType.SODA;
            this.width = 250;
            this.height = 330;
        }

        dimensionsSetByType = true;
        return this;
    }

    protected EnemyFactory withRandomPosition(Room room) {
        this.position = room.entitySpawn(room.getBackground(), width, 0);
        return this;
    }

    protected Enemy build(Menu game) {
        Enemy enemy = new Enemy(game, position, width, height, type);
        enemies.add(enemy);
        return enemy;
    }

    protected List<Enemy> getEnemies() {
        return enemies;
    }

    protected void kill() {
        enemies = new ArrayList<>();
    }
}
