
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;



public class Room
{
	public static enum RoomType{
		KITCHEN_DEMO,
	}
	
	private RoomType roomType;
	private boolean hasBoss;
	private int enemiesNumber;
	private Texture background;
	
	
	public Room(RoomType type, boolean boss, int enemyNum)
	{	
		roomType = type;
		hasBoss = boss;
		enemiesNumber = enemyNum;
		
		
		if (roomType == RoomType.KITCHEN_DEMO){
			//background = new Texture("kitchen_size1.png");
			background = new Texture("kitchen_size1_With_Objects.png");
			
		}
	
		
	}
	
	public void render(SpriteBatch batch)
	{
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());	
	}
	
	
	public boolean getHasBoss()
	{
		return hasBoss;
	}
	public int getEnemiesNumber()
	{
		return enemiesNumber;
	}
	
	public Texture getBackground()
	{
			return background;
	}
	
}