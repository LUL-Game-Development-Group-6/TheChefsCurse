package com.mygdx.game.Screens;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.gamesave.GameSaveLoader;
import com.mygdx.game.helpers.StatsHelper;
import com.mygdx.game.helpers.XpAnimationHelper;

/**
 * Entry class for the logic of the game (core)
 *
 * Please see the {@link com.mygdx.game.Screens.Menu}
 * @author Gines Moratalla
 *
 */
public class Menu extends Game {

    /**
     * Singleton declaration
     */
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

    /**
     * <p>
     *     Method that is called once the application is started
     * </p>
     * @since 1.0
     */
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

    /**
     * <p>
     *     Get round number as a String
     * </p>
     * @return Round number as String
     * @since 1.0
     */
    public String getStrRound() {
        String string = Integer.toString(round);
        return string;
    }

    /**
     * <p>
     *     Get XP as a String
     * </p>
     * @return XP as String
     * @since 1.0
     */
    public String getStrXP() {
        String string = Integer.toString(XP);
        return string;
    }

    public void incrementRound() {
        this.round++;
    }

    /**
     * <p>
     *     Resets the game's fields and settings
     * </p>
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-101">GD-101</a>
     * @since 1.0
     */
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

    /**
     * <p>
     *     Decrement user's XP score.
     *     Usually for purchases made for upgrades in health and damage before the start of next round
     * </p>
     * @param XP points used to make a purchase (default: 50)
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-156">GD-156</a>
     * @since 1.0
     */
    public void useXP(int usedXP) {
        this.XP -= usedXP;
    }

    public int getXP() {
        return this.XP;
    }

    public StatsHelper getStatsHelper() {
        return this.statsHelper;
    }

    public XpAnimationHelper getXpAnimationHelper() {
        return this.xpAnimation;
    }
}
