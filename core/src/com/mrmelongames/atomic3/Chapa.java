package com.mrmelongames.atomic3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

public class Chapa {

    Sprite sprite;

    List<Chapa> colidingWithChapas;
    List<Orbe> colidingWithOrbes;

    BitmapFont font;

    int screenWidth;
    int screenHeight;
    float designScale;

    float initVelocidad;
    float velocidad;

    float startX;
    float startY;

    int initX;
    int initY;
    int deltaX;
    int deltaY;
    Vector2 direction;
    float mass;

    int ataque;
    int vidaInit;
    int vida;
    int attackRadius;

    int teamID;

    Color initColor;

    float initialDeltaTime;

    boolean canCollide;

    Chapa(String path, int sw, int sh, float ds)
    {
        canCollide = true;

        mass = 1;

        Texture t = new Texture(Gdx.files.internal(path), true);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        sprite = new Sprite(t);
        direction = new Vector2();
        deltaX = 0;
        deltaY = 0;
        initialDeltaTime = 0;
        initColor = sprite.getColor();

        colidingWithChapas = new ArrayList<Chapa>();
        colidingWithOrbes = new ArrayList<Orbe>();

        screenWidth = sw;
        screenHeight = sh;
        designScale = ds;

        sprite.setSize(sprite.getWidth() * designScale, sprite.getHeight() * designScale);
        attackRadius = (int) (1000 * designScale);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/RobotoCondensed-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        int pixelsFont = Gdx.graphics.getWidth() / 30;
        int sizeFont = (int) ((pixelsFont * 101d) / 72d);
        param.size = sizeFont;
        param.color = Color.WHITE;
        param.borderColor = Color.BLACK;
        param.borderWidth = 10 * designScale;

        font = generator.generateFont(param);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

    }

    void setParams(int _vida, int _ataque){
        vida = _vida;
        vidaInit = _vida;
        ataque = _ataque;
    }

    void draw(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
        font.draw(spriteBatch, "HP: " + vida + "\nATK: " + ataque, getX(), getY() + getHeight(), getWidth(), Align.center, false);

    }

    void update(float deltaTime)
    {
        deltaTime = deltaTime - initialDeltaTime;

        velocidad *= 0.965f;
        //velocidad = 1f;
        sprite.setPosition(
                sprite.getX() + direction.x * velocidad,
                sprite.getY() + direction.y * velocidad
        );
        if(sprite.getX() < 0){ sprite.setX(0); direction.x *= -1;}
        if(sprite.getY() < 0){ sprite.setY(0); direction.y *= -1;}
        if(sprite.getX() + sprite.getWidth() > screenWidth){ sprite.setX(screenWidth - sprite.getWidth()); direction.x *= -1;}
        if(sprite.getY() + sprite.getHeight() > screenHeight){ sprite.setY(screenHeight - sprite.getHeight()); direction.y *= -1;}

        if(velocidad < 0.05f) {velocidad = 0; canCollide = true;}
        if(vida <= 0) reset();
    }

    void addColligingWith(Chapa c){
        colidingWithChapas.add(c);
    }

    void addColligingWith(Orbe o){
        colidingWithOrbes.add(o);
    }

    void setPath(float x, float y, float initialDeltaTime, float potencia){
        velocidad = initVelocidad * potencia;
        this.initialDeltaTime = initialDeltaTime;
        direction = new Vector2(x - sprite.getWidth() / 2, y - sprite.getHeight() / 2).sub(sprite.getX(), sprite.getY()).nor();
        initX = (int) sprite.getX();
        initY = (int) sprite.getY();
        deltaX = (int) (x - initX - sprite.getWidth()  / 2);
        deltaY = (int) (y - initY - sprite.getHeight() / 2);
    }

    boolean isColliding(Chapa c){
        //if(!canCollide || !c.canCollide) return false;
        Vector2 posC = new Vector2(c.getX() + c.getWidth() / 2, c.getY() + c.getHeight() / 2);
        Vector2 myPos = new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
        boolean areColliding = posC.dst(myPos) <= (getWidth() / 2f) + (c.getWidth() / 2f);
        /*Chapa b = isBalon ? this : c.isBalon ? c : null;
        if(b != null && b.canCollide && areColliding) b.canCollide = false;*/
        return areColliding;
    }

    boolean isColliding(Orbe o){
        //if(!canCollide || !c.canCollide) return false;
        Vector2 posC = new Vector2(o.x, o.y);
        Vector2 myPos = new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
        boolean areColliding = posC.dst(myPos) <= (getWidth() / 2f) + (o.radio);
        /*Chapa b = isBalon ? this : c.isBalon ? c : null;
        if(b != null && b.canCollide && areColliding) b.canCollide = false;*/
        return areColliding;
    }

    void reset(){
        sprite.setPosition(startX, startY);
        velocidad = 0;
        vida = vidaInit;
    }

    void setColor(Color c){ sprite.setColor(c); }

    Sprite getSprite() { return sprite; }

    void setPosition(Vector2 vec)
    {
        setPosition(vec.x, vec.y);
    }

    void setPosition(float x, float y)
    {
        sprite.setPosition(x * designScale - getWidth()/2, y * designScale - getHeight()/2);
        initX = (int) sprite.getX();
        initY = (int) sprite.getY();
        startX = sprite.getX();
        startY = sprite.getY();
    }

    Vector2 getPosition() { return new Vector2(sprite.getX(), sprite.getY()); }

    float getWidth() {return sprite.getWidth();}

    float getHeight() {return sprite.getHeight();}

    float getX() {return sprite.getX();}

    float getY() {return sprite.getY();}

    float getVx() {return direction.x * velocidad;}

    float getVy() {return direction.y * velocidad;}

    void setVelocity(int vel) {
        initVelocidad = vel;
    }

    float getVelocity() { return initVelocidad; }

    public float easeInCubic (float t, float b, float c) { // t: tiempo, b: inicio, c: inicio - final, d: duracion
        t = t > 1 ? 1 : t;
        return c * t * t * t + b;
    }

    public void dispose(){
        sprite.getTexture().dispose();
    }

}
