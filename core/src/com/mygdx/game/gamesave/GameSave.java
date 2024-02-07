package com.mygdx.game.gamesave;

public class GameSave {
    public String version;
    public Integer xp;
    public Integer lastRoundPlayed;
    public long timePlayed;
    public Integer lastPlayerHealth;
    public Integer enemiesLeftInLastRound;

    public GameSave() {
    }

    public GameSave(String version, Integer xp, Integer lastRoundPlayed, long timePlayedInMinutes, Integer lastPlayerHealth, Integer enemiesLeftInLastRound) {
        this.version = version;
        this.xp = xp;
        this.lastRoundPlayed = lastRoundPlayed;
        this.timePlayed = timePlayedInMinutes;
        this.lastPlayerHealth = lastPlayerHealth;
        this.enemiesLeftInLastRound = enemiesLeftInLastRound;
    }

    protected void updateHealth(int health) {
        this.lastPlayerHealth = health;
    }

    protected void updateEnemiesLeft(int enemiesLeftInLastRound) {
        this.enemiesLeftInLastRound = enemiesLeftInLastRound;
    }
}