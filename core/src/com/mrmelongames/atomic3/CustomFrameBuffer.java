package com.mrmelongames.atomic3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CustomFrameBuffer {
    FrameBuffer fbo;
    TextureRegion fboRegion;
    SpriteBatch fboBatch;
    ShapeRenderer fboShape;

    boolean changed;

    public CustomFrameBuffer(){
        this.init();
    }

    public void init(){
        fboBatch = new SpriteBatch();
        fbo = new FrameBuffer(Pixmap.Format.RGBA4444, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
        fboRegion = new TextureRegion(fbo.getColorBufferTexture(), 0, 0, fbo.getWidth(), fbo.getHeight());
        //fboRegion.flip(false, true);
        fboShape = new ShapeRenderer();
        changed = false;
    }

    public SpriteBatch getSpriteBatch(){
        return fboBatch;
    }

    public ShapeRenderer getShapeRenderer(){
        return fboShape;
    }

    protected void begin() {

        fbo.begin();

        //Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        fboBatch.begin();

        fboBatch.setBlendFunction(-1, -1);

        Gdx.gl20.glBlendFuncSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE);

    }

    protected void begin(ShapeRenderer.ShapeType shapeType) {

        fbo.begin();

        //Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl20.glBlendFuncSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE);

        changed = true;

        fboShape.begin(shapeType);

    }

    protected void end() {
        if(changed){
            fboShape.end();
        } else {
            fboBatch.end();
        }

        // unbind the FBO
        fbo.end();

        // now let's reset blending to the default...
        fboBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        //Gdx.app.log("", "FPS: " + Gdx.graphics.getFramesPerSecond());

        fboBatch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);

    }

    public void change(ShapeRenderer.ShapeType shapeType){
        changed = true;

        fboBatch.end();
        fboShape.begin(shapeType);
    }
/*
    public void change(){
        changed = false;

        fboShape.end();
        fboBatch.begin();
    }
*/
    public void draw(SpriteBatch batch){

        draw(batch, 0, 0);
    }
    public void draw(SpriteBatch batch, float offsetX, float offsetY){

        batch.begin();

        batch.draw(fboRegion, offsetX, offsetY);

        batch.end();

    }

    public TextureRegion getFboRegion(){
        return fboRegion;
    }

    /*
    protected void renderSlider() {
        // make our offscreen FBO the current buffer
        fbo.begin();

        // we need to first clear our FBO with transparent black
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // start our batch
        fboBatch.begin();

        // render some sprites to our offscreen FBO
        float x = 0;
        float y = 0;
        int val = 40; // example value amount

        // use -1 to ignore.. somebody should fix this in LibGDX :\
        fboBatch.setBlendFunction(-1, -1);

        // setup our alpha blending to avoid blending twice
        Gdx.gl20.glBlendFuncSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE);

        // draw sprites
        fboBatch.setColor(1f, 1f, 1f, 1f);
        fboBatch.draw(fboSprite, 0, 0);

        fontDesc.setColor(0, 0, 0, 1);
        fontDesc.draw(fboBatch, "Holis", 0, 100, Gdx.graphics.getWidth(), Align.center, true);

        // end (flush) our batch
        fboBatch.end();

        // unbind the FBO
        fbo.end();

        // now let's reset blending to the default...
        fboBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Gdx.app.log("", "FPS: " + Gdx.graphics.getFramesPerSecond());

        // nice smooth background color
        //float L = 233 / 255f;
        //Gdx.gl.glClearColor(L, L, L, 0f);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // whenever the slider moves we will need to call renderSlider() to
        // update the offscreen texture

        // render the offscreen texture with "premultiplied alpha" blending
        fboBatch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);

        batch.begin();

        // due to our different blend funcs we need to use RGBA to specify opacity
        // tinting becomes unavailable with this solution
        //float a = 1;
        //batch.setColor(a, a, a, a);

        batch.draw(fboRegion, 0, 0);

        fontDesc.setColor(0, 0, 0, 1);
        fontDesc.draw(batch, "Holis", 0, 100 + fontDesc.getCapHeight(), Gdx.graphics.getWidth(), Align.center, true);

        batch.end();
    }
*/
}
