package com.mygdx.game.Screens;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.gamesave.GameSaveLoader;
import com.mygdx.game.helpers.StatsHelper;
import com.mygdx.game.helpers.XpAnimationHelper;

public class Menu extends Game {

    private static Menu game;

    public static Menu getInstance() {
        if(game == null) game = new Menu();
        return game;
    }
    
    public SpriteBatch batch;
    private StatsHelper statsHelper;
    private XpAnimationHelper xpAnimation;
    private int round;
    private int XP;
    private int totalXP;


    public void create() {
        xpAnimation = new XpAnimationHelper();
        statsHelper = new StatsHelper();
        XP= 0;
        totalXP = 0;
        round = 1;
        GameSaveLoader.getInstance().load();
        batch = new SpriteBatch();
        this.setScreen(Cover.getInstance());
    }

    public void render() {
        super.render();
    }
    public void dispose() {
        super.dispose();
    }

    public int getRound() {
        return this.round;
    }

    public String getStrRound() {
        String string = Integer.toString(round);
        return string;
    }

    public String getStrXP() {
        String string = Integer.toString(XP);
        return string;
    }

    public void incrementRound() {
        this.round++;
    }

    public void resetGame() {
        this.statsHelper.resetScaler();
        this.round = 1;
        this.XP = 0;
        this.totalXP = 0;
    }

    public void increaseXP() {
        this.XP += 10;
        this.totalXP += 10;
    }

    public void useXP(int usedXP) {
        this.XP -= usedXP;
    }

    public int getXP() {
        return this.XP;
    }

    public int getTotalXP() {
        return this.totalXP;
    }

    public StatsHelper getStatsHelper() {
        return this.statsHelper;
    }

    public XpAnimationHelper getXpAnimationHelper() {
        return this.xpAnimation;
    }
}
