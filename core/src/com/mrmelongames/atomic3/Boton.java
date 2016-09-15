package com.mrmelongames.atomic3;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Boton {
    int x;
    int y;
    int ancho;
    int alto;
    int estado;

    boolean touchDown;
    boolean touchUp;

    boolean containsPoint(float tx, float ty){
        return tx > x - ancho && tx < x + ancho && ty < y + ancho && ty > y - ancho;
    }

    void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, int teamTurn){
        if(teamTurn == 0) {
            shapeRenderer.setColor(0.4f, 0.79f, 1, 0.5f);
        } else {
            shapeRenderer.setColor(1, 0, 0, 0.5f);
        }
        shapeRenderer.circle(x, y, ancho);
    }
}
