package com.mygdx.game.Room;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.physics.DynamicObject;

/**
 * Furniture class
 * Please see the {@link com.mygdx.game.Room.Furniture}
 *
 * @author Gines Moratalla, Juozas Skarbalius
 */
public class Furniture extends DynamicObject {
    private final Rectangle furnitureHitbox;
    private final Sprite furnitureSprite;
    private final float xPosition;
    private final float yPosition;
    private float width;
    private float height;

    /**
     * <p>
     * Enum that contains crucial information for each available furniture type such as
     * width and height of the furniture object, and its texture object
     * </p>
     *
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-155">GD-155: [PHYSICS] Furniture is spawned randomly  into existing maps and collision of furniture[M]</a>
     * @see <a href="https://lulgroupproject.atlassian.net/browse/GD-85">GD-85: [PHYSICS] Implement custom room creation [L]</a>
     * @since 1.0
     */
    public enum FurnitureType {
        // Kitchen
        FRIDGE(new Texture("objects/Utensils/Fridge.png"), 150, 300), STOVE(new Texture("objects/Utensils/Stove.png")), METAL_TABLE(new Texture("objects/Metal_Table/Table_1.png"), 1000, 700), CHAIR1(new Texture("objects/Chairs/Chair_1.png"), 400, 500), CHAIR2(new Texture("objects/Chairs/Chair_2.png"), 500, 400), // Freezer
        SINGLE_SHELF(new Texture("objects/freezer_shelves/shelf_1.png"), 400, 700), DOUBLE_SHELF(new Texture("objects/freezer_shelves/shelf_2.png")), LONG_SHELF(new Texture("objects/freezer_shelves/shelf_3.png")), ICE(new Texture("objects/ice/ice.png"), 400, 350), BOX(new Texture("objects/box/box.png"), 600, 400), // Restaurant
        RESTAURANT_TABLE(new Texture("objects/Restaurant_Table/restaurant_table.png"), 370, 500),
        ;

        private Texture texture;
        private float width;
        private float height;

        FurnitureType(Texture texture, float width, float height) {
            this.texture = texture;
            this.width = width;
            this.height = height;
        }

        FurnitureType(Texture texture) {
            this.texture = texture;
        }

        public Texture getTexture() {
            return texture;
        }

        public void setTexture(Texture texture) {
            this.texture = texture;
        }

        public float getWidth() {
            return width;
        }

        public void setWidth(float width) {
            this.width = width;
        }

        public float getHeight() {
            return height;
        }

        public void setHeight(float height) {
            this.height = height;
        }
    }

    public Furniture(float xPos, float yPos, FurnitureType type) {
        Texture furnitureTexture = type.getTexture();
        if (type.getWidth() > 0 && type.getHeight() > 0) {
            width = type.getWidth();
            height = type.getHeight();
        }

        furnitureSprite = new Sprite(furnitureTexture);
        furnitureSprite.setPosition(xPos, yPos);
        xPosition = xPos;
        yPosition = yPos;
        furnitureHitbox = new Rectangle(xPos, yPos, width, height / 10f);
    }

    public Rectangle getHitbox() {
        return furnitureHitbox;
    }

    public Sprite getSprite() {
        return furnitureSprite;
    }

    public void render(SpriteBatch batch) {
        batch.draw(furnitureSprite, xPosition, yPosition, width, height);
    }
}
