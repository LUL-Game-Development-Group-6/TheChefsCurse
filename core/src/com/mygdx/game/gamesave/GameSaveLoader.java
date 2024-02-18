package com.mygdx.game.gamesave;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

public class GameSaveLoader {
    private GameSave gameSave;
    private ObjectMapper objectMapper;
    private long gameStartTimestamp;
    private long firstTimeStamp;
    private static GameSaveLoader gameSaveLoader;
    private static final String FILENAME = "gamesave.json";

    public static GameSaveLoader getInstance() {
        if(gameSaveLoader == null) gameSaveLoader = new GameSaveLoader();
        return gameSaveLoader;
    }
    public GameSaveLoader() {
        this.gameSave = new GameSave();
        this.objectMapper = new ObjectMapper();
        this.firstTimeStamp = System.currentTimeMillis();
    }

    public GameSave get() {
        return gameSave;
    }

    private void init() {
        
        GameSave InitgameSave = new GameSave("1", 0, 0, 0, 100, 0);
        try {
            File gamesaveAsFile = new File(FILENAME);
            if(gamesaveAsFile.createNewFile()) {
                updateGamesaveFile(InitgameSave);
            }
        } catch (FileNotFoundException e) {
            System.out.println("[ERROR] Cannot find gamesave file");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to initialize gamesave. Exiting.");
            throw new RuntimeException(e);
        }
    }

    public void load() {
        try {
            GameSave loadedGameSave = objectMapper.readValue(new File("gamesave.json")
                    , GameSave.class);
            System.out.println(loadedGameSave.version);
        } catch (IOException e) {
            System.out.println("[ERROR] Game save file does not exist in its default location.");
            init();
        }
    }
    public GameSaveLoader health(int health) {
        if(gameSave != null) gameSave.updateHealth(health);
        return this;
    }

    public GameSaveLoader enemiesLeft(int enemiesLeft) {
        if(gameSave != null) gameSave.updateEnemiesLeft(enemiesLeft);
        return this;
    }

    public GameSaveLoader withTimestamp(long timestamp) {
        this.gameStartTimestamp = timestamp;
        if(gameSave != null) {
            gameSave.timePlayed += gameStartTimestamp - firstTimeStamp;
        }
        return this;
    }

    public void update() {
        updateGamesaveFile(gameSave);
    }

    private void updateGamesaveFile(GameSave gameSave) {
        try {
            String gameSaveAsJSON = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(gameSave);
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME));
            writer.write(gameSaveAsJSON);
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("[ERROR] Cannot find gamesave file");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to initialize gamesave. Exiting.");
            throw new RuntimeException(e);
        }
    }

}