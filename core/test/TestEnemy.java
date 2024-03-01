import org.junit.jupiter.api.Test;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.physics.Enemy;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestEnemy {

    @Test
    public void randomizeEnemyDirection() {

        Game game = Mockito.mock(Game.class);
        Vector2 postition = new Vector2(0, 0);
        Vector2 playerPosition = new Vector2(10, 10);
        Enemy test_enemy = new Enemy(game, postition, 300, 320, Enemy.EnemyType.HAMBURGER);

        test_enemy.whereIsPlayer(playerPosition);
        Vector2 test_velocity = test_enemy.getVelocity();

        
        Assertions.assertEquals(test_velocity.x, 5);
        assertTrue(true);

    }

}