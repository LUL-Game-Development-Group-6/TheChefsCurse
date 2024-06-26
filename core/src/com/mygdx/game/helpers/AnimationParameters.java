package com.mygdx.game.helpers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class AnimationParameters {
    private final Animation<Sprite> animation;
    private final float x;
    private final float y;
    private final long timeStarted;

    public AnimationParameters(Animation<Sprite> animation, float x, float y, long timeStarted) {
        this.animation = animation;
        this.x = x;
        this.y = y;
        this.timeStarted = timeStarted;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public long getTimeStarted() {
        return timeStarted;
    }

    public Animation<Sprite> geAnimation() {
        return animation;
    }

}