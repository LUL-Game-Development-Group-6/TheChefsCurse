package com.mygdx.game.helpers;

import java.util.Vector;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;
import com.mygdx.game.physics.DynamicObject;

public class ShadersHelper {

    private String fragmentShader;
    private String vertexShader;
    private ShaderProgram shader;
    private SpriteBatch shaderBatch;
    private Sprite currentFrame;

    public ShadersHelper() {

        shaderBatch = new SpriteBatch();

        // Read glsl shader files
        FileHandle vertexHandler = Gdx.files.internal("shaders/vertexShader.glsl");
        FileHandle fragmentHandler = Gdx.files.internal("shaders/fragmentShader.glsl");
        vertexShader = vertexHandler.readString();
        fragmentShader = fragmentHandler.readString();
        shader = new ShaderProgram(vertexShader, fragmentShader);
    }


    public void drawshader(DynamicObject entity, float timePassed, OrthographicCamera camera) {

        shaderBatch.begin();

        // Get frame to apply shader and coordinates
        currentFrame = entity.getAnimation().getKeyFrame(timePassed, true);   
        currentFrame.setSize(entity.getSprite().getWidth()/2, entity.getSprite().getHeight()/2);
    
        // Convert map coordinates into screen coordinates to get entity position
        Vector3 screenCoords = new Vector3(entity.getSprite().getX(), entity.getSprite().getY(), 0);
        Vector3 worldCoords = camera.project(screenCoords);
        currentFrame.setPosition(worldCoords.x, worldCoords.y);

        long elapsedTime = System.currentTimeMillis() - entity.getTimeHit();

        if (elapsedTime < 400) {

            shader.bind();
            shader.setUniformf("u_time", elapsedTime / 400f);
            shaderBatch.setShader(shader);
            currentFrame.draw(shaderBatch);
        
        } else {
            shaderBatch.setShader(null);
            entity.setHit(false);
        }
        shaderBatch.end();
    }
    
}