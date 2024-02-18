package com.mygdx.game.physics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Room.Room;
import com.mygdx.game.Screens.Menu;

import java.util.ArrayList;
import java.util.List;

public class EnemyFactory {
    private static EnemyFactory singletonEnemyFactory;
    private List<Enemy> enemies;
    private Enemy.EnemyType type;
    private Room room;
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
        int randomID = MathUtils.random(0, 3);

        if(randomID == 0) {
            type = Enemy.EnemyType.HAMBURGER;
            this.width = 300;
            this.height = 320;
        }
        if(randomID == 1) {
            type = Enemy.EnemyType.HOTDOG;
            this.width = 400;
            this.height = 300;
        }
        if(randomID == 2) {
            type = Enemy.EnemyType.POPCORN;
            this.width = 280;
            this.height = 300;
        }

        if(randomID == 3) {
            type = Enemy.EnemyType.SODA;
            this.width = 250;
            this.height = 330;
        }

        dimensionsSetByType = true;
        return this;
    }

    public EnemyFactory withPosition(Vector2 position) {
        this.position = position;
        return this;
    }

    public EnemyFactory withRandomPosition(Room room) {
        this.position = room.entitySpawn(room.getBackground());
        return this;
    }

    public EnemyFactory withDimensions(float width, float height) {
        if(dimensionsSetByType) throw new IllegalStateException();
        this.width = width;
        this.height = height;
        return this;
    }

    public Enemy build(Menu game) {
        Enemy enemy = new Enemy(game, position, width, height, type);
        enemies.add(enemy);
        return enemy;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void kill() {
        enemies = new ArrayList<>();
    }
}
