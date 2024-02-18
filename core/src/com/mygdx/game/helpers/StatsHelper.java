package com.mygdx.game.helpers;

public class StatsHelper {

    private int healthScaler;
    private int damageScaler;
    private int enemyScaler;

    public StatsHelper() {
        healthScaler = 0;
        damageScaler = 0;
        enemyScaler = 0;
    }

    public int getHealthScaler() {
        return this.healthScaler;
    }
    public int getDamageScaler() {
        return this.damageScaler;
    }

    public void incrementHealthScaler() {
        this.healthScaler += 2;
    }
    public void incrementDamageScaler() {
        this.damageScaler += 2;
    }

    
    // Increase enemy Damage and health by 3 points every round
    public void increaseEnemyScaler() {
        enemyScaler += 3;
    }

    public int getEnemyScaler() {
        return this.enemyScaler;
    }

    // We use the same instance of game always, so we reset round scalers
    public void resetScaler() {
        this.healthScaler = 0;
        this.damageScaler = 0;
        this.enemyScaler = 0;
    }
    
}
