package com.mrmelongames.atomic3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.mrmelongames.atomic3.ShapeRenderer.ShapeType;

public class CustomDraw {

    SpriteBatch spriteBatch;
    ShapeRenderer shapeRenderer;
    
    public CustomDraw(SpriteBatch sb, ShapeRenderer sr){
        spriteBatch = sb;
        shapeRenderer = sr;
    }

    public void beginShapeRenderer(ShapeType shapeType){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(shapeType);
    }

    public void beginSpriteBatch(){
        spriteBatch.begin();
    }

    public void changeToSpriteBatch(){
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        spriteBatch.begin();
    }

    public void changeToShapeRenderer(ShapeType shapeType){
        spriteBatch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(shapeType);
    }

    public void endSpriteBatch(){
        spriteBatch.end();
    }

    public void endShapeRenderer(){
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

}