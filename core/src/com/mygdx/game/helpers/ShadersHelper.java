package com.mygdx.game.helpers;

import java.util.Vector;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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

        vertexShader = 
            "attribute vec4 a_position;\n" +
            "attribute vec4 a_color;\n" +
            "attribute vec2 a_texCoord0;\n" +
            "uniform mat4 u_projTrans;\n" +
            "varying vec4 v_color;\n" +
            "varying vec2 v_texCoords;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "   v_color = a_color;\n" +
            "   v_texCoords = a_texCoord0;\n" +
            "   gl_Position =  u_projTrans * a_position;\n" +
            "}";
        fragmentShader = 
            "uniform float u_time;\n" +
            "varying vec4 v_color;\n" +
            "varying vec2 v_texCoords;\n" +
            "uniform sampler2D u_texture;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "   vec4 texColor = texture2D(u_texture, v_texCoords);\n" +
            "   if (texColor.a > 0.5) {\n" +
            "       float fadeDuration = 0.5; // Time in seconds for the fade effect to complete\n" +
            "       float normalizedTime = min(u_time / fadeDuration, 1.0); // Ensure time doesn't exceed fadeDuration\n" +
            "       float startAlpha = 0.8; // Starting alpha value\n" +
            "       float endAlpha = 0.0; // Ending alpha value\n" +
            "       float alpha = mix(startAlpha, endAlpha, normalizedTime); // Linearly interpolate alpha from startAlpha to endAlpha over fadeDuration\n" +
            "       gl_FragColor = vec4(1, 0, 0, max(alpha, 0.0)); // Ensure alpha doesn't go below 0\n" +
            "   } else {\n" +
            "       gl_FragColor = texColor;\n" +
            "   }\n" +
            "}";
        
        

        shader = new ShaderProgram(vertexShader, fragmentShader);
    }


    public void drawshader(DynamicObject entity, float timePassed, OrthographicCamera camera) {

        shaderBatch.begin();
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