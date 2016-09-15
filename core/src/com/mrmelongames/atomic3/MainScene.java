package com.mrmelongames.atomic3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

public class MainScene extends ApplicationAdapter implements InputProcessor{

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		inputManager(ACTION_DOWN, screenX, screenY, 0);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		inputManager(ACTION_UP, screenX, screenY, 0);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		inputManager(ACTION_MOVE, screenX, screenY, 0);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	final int ACTION_DOWN = 0;
	final int ACTION_UP = 1;
	final int ACTION_MOVE = 2;

	CustomDraw customDraw;
	SpriteBatch spriteBatch;
	ShapeRenderer shapeRenderer;

	Chapa chapasAlly[];
	Chapa chapasEnemy[];

	Orbe orbes[];

	float deltaTime;

	int designWidth = 2560;
	int designHeight = 4550;
	float designScale;

	int screenWidth;
	int screenHeight;

	int initTouchX;
	int initTouchY;

	int touchX;
	int touchY;

	Orbe orbeSelected;
	Chapa selectedChapa;
	Chapa chapaInAttack;
	Chapa chapaInMovement;
	int teamTurn;

	Boton testButton;
	BitmapFont turnosLeft;

	@Override
	public void create () {

		Gdx.input.setInputProcessor(this);

		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		testButton = new Boton();
		testButton.x = 0;
		testButton.y = screenHeight / 2;
		testButton.ancho = screenWidth / 10;

		touchX = -1;
		touchY = -1;

		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		customDraw = new CustomDraw(spriteBatch, shapeRenderer);

		designScale = Gdx.graphics.getWidth() / (float) designWidth;

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/RobotoCondensed-Bold.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

		int pixelsFont = Gdx.graphics.getWidth() / 15;
		param.size = (int) ((pixelsFont * 101d) / 72d);
		param.color = Color.WHITE;
		param.borderColor = Color.BLACK;
		param.borderWidth = 10 * designScale;

		turnosLeft = generator.generateFont(param);
		turnosLeft.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		orbes = new Orbe[2];

		orbes[0] = new Orbe();
		orbes[0].vidaMax = 50;
		orbes[0].vida = orbes[0].vidaMax;
		orbes[0].x = screenWidth / 2;
		orbes[0].y = (int) (30 * designScale);
		orbes[0].radio = screenWidth / 10;
		orbes[0].teamID = 0;
		orbes[0].turnos = 3;
		orbes[0].turnosInit = 3;

		orbes[1] = new Orbe();
		orbes[1].vidaMax = 50;
		orbes[1].vida = orbes[1].vidaMax;
		orbes[1].x = screenWidth / 2;
		orbes[1].y = screenHeight - (int) (30 * designScale);
		orbes[1].radio = screenWidth / 10;
		orbes[1].teamID = 1;
		orbes[1].turnos = 3;
		orbes[1].turnosInit = 3;

		int[] ataques = {2, 1, 2};
		int[] vidas = {2, 2, 5};
		int[] attackRadius = {(int) (950 * designScale), (int) (1500 * designScale), (int) (400 * designScale)};
		int[] velocidades /*Rango de movimiento*/ = {(int) (350 * designScale), (int) (200 * designScale), (int) (100 * designScale)};

		chapaInAttack = null;
		chapaInMovement = null;

		teamTurn = 0;
		chapasAlly = new Chapa[3];
		for(int i = 0; i < chapasAlly.length; i++) {
			chapasAlly[i] = new Chapa("greenc.png", screenWidth, screenHeight, designScale);
			chapasAlly[i].setPosition(415 + 317/2 + 707 * i, designHeight - 3658 - 347/2);
			chapasAlly[i].setColor(new Color(0, 0.85f, 1, 1));
			chapasAlly[i].teamID = 0;
			chapasAlly[i].setParams(vidas[i], ataques[i], velocidades[i], attackRadius[i]);
			log(chapasAlly[i].velocidad);
		}

		chapasEnemy = new Chapa[3];
		for(int i = 0; i < chapasEnemy.length; i++) {
			chapasEnemy[i] = new Chapa("redc.png", screenWidth, screenHeight, designScale);
			chapasEnemy[i].setPosition(415 + 317/2 + 707 * i, 3658 + 347/2);
			chapasEnemy[i].setVelocity((int)(150 * designScale));
			chapasEnemy[i].setColor(new Color(0.95f, 0.2f, 0.5f, 1));
			chapasEnemy[i].teamID = 1;
			chapasEnemy[i].setParams(vidas[i], ataques[i], velocidades[i], attackRadius[i]);
		}
	}

	public void update() {
		deltaTime += Gdx.graphics.getDeltaTime();

		for(Chapa c : chapasAlly)
			c.update(deltaTime);

		for(Chapa c : chapasEnemy)
			c.update(deltaTime);

		Chapa c1 = chapasAlly[0];
		Chapa c2 = chapasAlly[1];
		if(c1.isColliding(c2)){
			handleCollision(c1, c2);
		} else {
			c1.colidingWithChapas.remove(c2);
			c2.colidingWithChapas.remove(c1);
		}

		c1 = chapasAlly[0];
		c2 = chapasAlly[2];
		if(c1.isColliding(c2)) {
			handleCollision(c1, c2);
		} else {
			c1.colidingWithChapas.remove(c2);
			c2.colidingWithChapas.remove(c1);
		}

		c1 = chapasAlly[1];
		c2 = chapasAlly[2];
		if(c1.isColliding(c2)){
			handleCollision(c1, c2);
		} else {
			c1.colidingWithChapas.remove(c2);
			c2.colidingWithChapas.remove(c1);
		}

		for(Chapa a : chapasAlly)
			for(Chapa e : chapasEnemy)
				if(a.isColliding(e)){
					handleCollision(a, e);
				} else {
					a.colidingWithChapas.remove(e);
					e.colidingWithChapas.remove(a);
				}

		c1 = chapasEnemy[0];
		c2 = chapasEnemy[1];
		if(c1.isColliding(c2)){
			handleCollision(c1, c2);
		} else {
			c1.colidingWithChapas.remove(c2);
			c2.colidingWithChapas.remove(c1);
		}

		c1 = chapasEnemy[0];
		c2 = chapasEnemy[2];
		if(c1.isColliding(c2)) {
			handleCollision(c1, c2);
		} else {
			c1.colidingWithChapas.remove(c2);
			c2.colidingWithChapas.remove(c1);
		}

		c1 = chapasEnemy[1];
		c2 = chapasEnemy[2];
		if(c1.isColliding(c2)){
			handleCollision(c1, c2);
		} else {
			c1.colidingWithChapas.remove(c2);
			c2.colidingWithChapas.remove(c1);
		}

		for(Chapa c : chapasAlly)
			for(Orbe o : orbes)
				if(c.isColliding(o)){
					handleCollision(c, o);
				} else {
					c.colidingWithOrbes.remove(o);
				}

		if(chapaInMovement != null && chapaInMovement.velocidad == 0) {chapaInAttack = chapaInMovement; chapaInMovement = null;}
	}

	public void draw() {
		Gdx.gl20.glClearColor(0.3f, 0.35f, 0.3f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		customDraw.beginSpriteBatch();
		customDraw.changeToShapeRenderer(ShapeRenderer.ShapeType.Filled);
		if(touchX != -1 && selectedChapa != null && selectedChapa.teamID == teamTurn) {
			float x = selectedChapa.getX() + selectedChapa.getWidth() / 2;
			float y = selectedChapa.getY() + selectedChapa.getHeight() / 2;
			shapeRenderer.setColor(1, 1, 1, 1);
			shapeRenderer.rectLine(x, y, x - (touchX - initTouchX), y - (touchY - initTouchY), 10 * designScale);
		}

		shapeRenderer.setColor(0.4f, 0.79f, 1, 0.5f);
		shapeRenderer.rect(0, screenHeight / 2 - 50 * designScale + 100 * designScale, screenWidth * orbes[0].vida / orbes[0].vidaMax, 100 * designScale);
		shapeRenderer.setColor(1, 0, 0, 0.5f);
		shapeRenderer.rect(0, screenHeight / 2 - 50 * designScale - 100 * designScale, screenWidth * orbes[1].vida / orbes[1].vidaMax, 100 * designScale);

		shapeRenderer.setColor(0.4f, 0.79f, 1, 0.5f);
		shapeRenderer.circle(orbes[0].x, orbes[0].y, orbes[0].radio);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(orbes[1].x, orbes[1].y, orbes[1].radio);

		testButton.render(spriteBatch, shapeRenderer, teamTurn);

		if(chapaInAttack != null) {
			shapeRenderer.setColor(0, 1, 0, 0.5f);
			shapeRenderer.circle(chapaInAttack.getX() + chapaInAttack.getWidth() / 2, chapaInAttack.getY() + chapaInAttack.getHeight() / 2, chapaInAttack.attackRadius);
		}
		customDraw.changeToSpriteBatch();
		turnosLeft.draw(spriteBatch, orbes[teamTurn].turnos + "", 15 * designScale, screenHeight / 2 + turnosLeft.getCapHeight() / 2, testButton.ancho / 2, Align.center, false);

		for(Chapa c : chapasAlly)
			c.draw(spriteBatch);
		for(Chapa c : chapasEnemy)
			c.draw(spriteBatch);
		customDraw.endSpriteBatch();
	}

	void handleCollision(Chapa a, Orbe b) {
		boolean areColliding = false;
		for (Orbe o : a.colidingWithOrbes) {
			if(o == b){
				areColliding = true;
				break;
			}
		}

		if(!areColliding) {
			a.addColligingWith(b);

			float x1 = a.getX();
			float y1 = a.getY();
			float x2 = b.x;
			float y2 = b.y;

			float vx1 = a.getVx();
			float vy1 = a.getVy();
			float vx2 = 0;
			float vy2 = 0;

			float dx = x2 - x1;
			float dy = y2 - y1;

			double phi = dx == 0 ? Math.PI / 2f : Math.atan(dy / dx);

			double v1i = Math.sqrt((vx1 * vx1) + (vy1 * vy1));
			double v2i = Math.sqrt((vx2 * vx2) + (vy2 * vy2));

			double ang1 = findAngle(vx1, vy1);
			double ang2 = findAngle(vx2, vy2);

			double v1xr = v1i * Math.cos(ang1 - phi);
			double v1yr = v1i * Math.sin(ang1 - phi);
			double v2xr = v2i * Math.cos(ang2 - phi);

			double v1fxr = -v1xr+2*v2xr;

			double v1fx = Math.cos(phi) * v1fxr + Math.cos(phi + Math.PI / 2f) * v1yr;
			double v1fy = Math.sin(phi) * v1fxr + Math.sin(phi + Math.PI / 2f) * v1yr;

			a.direction = new Vector2((float) v1fx, (float) v1fy).nor();

		}
	}

	void handleCollision(Chapa a, Chapa b){
		boolean areColliding = false;
		for (Chapa c : a.colidingWithChapas) {
			if(c == b){
				areColliding = true;
				break;
			}
		}

		if(!areColliding) {
			a.addColligingWith(b);
			b.addColligingWith(a);

			float x1 = a.getX();
			float y1 = a.getY();
			float x2 = b.getX();
			float y2 = b.getY();

			float vx1 = a.getVx();
			float vy1 = a.getVy();
			float vx2 = b.getVx();
			float vy2 = b.getVy();

			float dx = x2 - x1;
			float dy = y2 - y1;

			double phi = dx == 0 ? Math.PI / 2f : Math.atan(dy / dx);

			double v1i = Math.sqrt((vx1 * vx1) + (vy1 * vy1));
			double v2i = Math.sqrt((vx2 * vx2) + (vy2 * vy2));

			double ang1 = findAngle(vx1, vy1);
			double ang2 = findAngle(vx2, vy2);

			double v1xr = v1i * Math.cos(ang1 - phi);
			double v1yr = v1i * Math.sin(ang1 - phi);
			double v2xr = v2i * Math.cos(ang2 - phi);
			double v2yr = v2i * Math.sin(ang2 - phi);

			double v1fx = Math.cos(phi) * v2xr + Math.cos(phi + Math.PI / 2f) * v1yr;
			double v1fy = Math.sin(phi) * v2xr + Math.sin(phi + Math.PI / 2f) * v1yr;
			double v2fx = Math.cos(phi) * v1xr + Math.cos(phi + Math.PI / 2f) * v2yr;
			double v2fy = Math.sin(phi) * v1xr + Math.sin(phi + Math.PI / 2f) * v2yr;

			a.direction = new Vector2((float) v1fx, (float) v1fy).nor();
			b.direction = new Vector2((float) v2fx, (float) v2fy).nor();

			if(a.teamID != b.teamID) a.velocidad = b.velocidad = 0;
		}
	}

	double findAngle(float xthing, float ything){
		double conv = Math.PI / 180f;
		double t;

		if (xthing < 0) {
			t = 180 + Math.atan(ything / xthing) / conv;
		} else if (xthing > 0 && ything >= 0) {
			t = Math.atan(ything / xthing) / conv;
		} else if (xthing > 0 && ything < 0 ) {
			t = 360. + Math.atan(ything / xthing) / conv;
		} else if (xthing == 0 && ything == 0) {
			t = 0;
		} else if( xthing == 0 && ything >= 0) {
			t = 90;
		} else {
			t = 270;
		}

		return t * conv;
	}

	void nextTurn(){
		orbes[teamTurn].turnos = orbes[teamTurn].turnosInit;
		chapaInMovement = null;
		chapaInAttack = null;

		teamTurn = teamTurn == 0 ? 1 : 0;
	}

	@SuppressWarnings("unused")
	void reset(){
		teamTurn = 0;

		orbes[0].turnos = orbes[0].turnosInit;
		orbes[1].turnos = orbes[1].turnosInit;

		for(Chapa c : chapasAlly)
			c.reset();
		for(Chapa c : chapasEnemy)
			c.reset();
	}

	public void inputManager(int action, float x, float y, float velocityX){
		y = Gdx.graphics.getHeight() - y;
		switch (action){
			case ACTION_DOWN:
				inputDown(x, y);
				break;
			case ACTION_MOVE:
				inputMove(x, y, velocityX);
				break;
			case ACTION_UP:
				inputUp(x, y, velocityX);
				break;
		}
	}

	boolean isInside(Chapa c, float x, float y){
		float r = ((float) Math.pow(c.getWidth() / 2f, 2));
		float a = c.getX() + c.getWidth() / 2;
		float b = c.getY() + c.getHeight() / 2;
		float one = ((float) Math.pow((x - a), 2));
		float two = ((float) Math.pow((y - b), 2));
		return  (one + two < r);
	}

	boolean isInside(Orbe o, float x, float y){
		float r = ((float) Math.pow(o.radio, 2));
		float a = o.x;
		float b = o.y;
		float one = ((float) Math.pow((x - a), 2));
		float two = ((float) Math.pow((y - b), 2));

		return  (one + two < r);
	}

	public void inputDown (float x, float y){ // X, Y son las coordenadas del dedo lul
		//y = screenHeight - y;
		for (Chapa c : chapasAlly) {
			if (isInside(c, x, y)) {
				selectedChapa = c;
			}
		}

		if(selectedChapa == null)
		for (Chapa c : chapasEnemy) {
			if (isInside(c, x, y)) {
				selectedChapa = c;
			}
		}

		if(chapaInAttack != null && selectedChapa != null && selectedChapa.teamID == chapaInAttack.teamID) chapaInAttack = null;

		if(isInside(orbes[0], x, y)){
			orbeSelected = orbes[0];
		} else if(isInside(orbes[1], x, y)){
			orbeSelected = orbes[1];
		} else {
			orbeSelected = null;
		}

		initTouchX = (int) x;
		initTouchY = (int) y;

		touchX = (int) x;
		touchY = (int) y;

		testButton.touchDown = testButton.containsPoint(x, y);

	}

	public void inputMove(float x, float y, @SuppressWarnings("unused") float velocityX){
		touchX = (int) x;
		touchY = (int) y;

		if(x - initTouchX != 0 && y - initTouchY != 0) chapaInAttack = null;
	}

	public void inputUp(float x, float y, @SuppressWarnings("unused") float velocityX) {
		if (selectedChapa != null) {
			if(chapaInAttack != null) {
				if (teamTurn != selectedChapa.teamID) {
					if (isInside(selectedChapa, x, y)) {
						float r = chapaInAttack.attackRadius;
						float a = chapaInAttack.getX() + chapaInAttack.getWidth() / 2;
						float b = chapaInAttack.getY() + chapaInAttack.getHeight() / 2;
						float one = ((float) Math.pow((x - a), 2));
						float two = ((float) Math.pow((y - b), 2));
						if (one + two < r * r) {
							orbes[chapaInAttack.teamID].turnos--;
							selectedChapa.vida -= chapaInAttack.ataque;
							chapaInAttack.vida -= selectedChapa.ataque;

							if(selectedChapa.vida < 0) orbes[selectedChapa.teamID].vida += selectedChapa.vida;
							if(chapaInAttack.vida < 0) orbes[chapaInAttack.teamID].vida += chapaInAttack.vida;
						}
					}
					chapaInAttack = null;
				} else if (orbeSelected != null && teamTurn != orbeSelected.teamID) {
					if (isInside(orbeSelected, x, y)) {
						if (orbeSelected.teamID != teamTurn) {
							orbes[chapaInAttack.teamID].turnos--;
							orbeSelected.vida -= chapaInAttack.ataque;
						}
					}
				}
			} else if(selectedChapa.teamID == teamTurn) {
				if (x - initTouchX == 0 && y - initTouchY == 0) {
					chapaInAttack = selectedChapa;
				} else if(!testButton.containsPoint(x, y)) {
					chapaInAttack = null;
					float potencia = new Vector2(x - initTouchX, y - initTouchY).len() / designScale / 600;
					potencia = potencia > 1 ? 1 : potencia;

					float xC = selectedChapa.getSprite().getX() + selectedChapa.getWidth() / 2;
					float yC = selectedChapa.getSprite().getY() + selectedChapa.getHeight() / 2;
					selectedChapa.setPath(xC - (touchX - initTouchX), yC - (touchY - initTouchY), deltaTime, potencia);
					log(selectedChapa.velocidad);
					chapaInMovement = selectedChapa;

					orbes[teamTurn].turnos--;

					touchX = -1;
					touchY = -1;
				}
			}

			if (orbes[teamTurn].turnos == 0) {
				orbes[teamTurn].turnos = orbes[teamTurn].turnosInit;
				nextTurn();
			}
		} else {
			if(testButton.touchDown && testButton.containsPoint(x, y)) {
				testButton.estado++;
			}
			log(testButton.estado);
			if(testButton.estado >= 2){
				nextTurn();
				testButton.estado = 0;
			}
		}
		selectedChapa = null;
		orbeSelected = null;
	}

	@Override
	public void render () {
		update();
		draw();
	}

	@Override
	public void dispose () {
		spriteBatch.dispose();
		shapeRenderer.dispose();

		for(Chapa c : chapasAlly)
		c.dispose();
		for(Chapa c : chapasEnemy)
		c.dispose();
	}

	@SuppressWarnings("unused")
	public void log(String s){
		Gdx.app.log("Atomic", s);
	}
	@SuppressWarnings("unused")
	public void log(int i){
		Gdx.app.log("Atomic", i + "");
	}
	@SuppressWarnings("unused")
	public void log(float f){
		Gdx.app.log("Atomic", f + "");
	}
	@SuppressWarnings("unused")
	public void log(boolean b){
		Gdx.app.log("Atomic", b + "");
	}
	@SuppressWarnings("unused")
	public float easeInCubic (float t, float b, float c, float d) { // t: tiempo, b: inicio, c: inicio - final, d: duracion
		t /= d;
		return c*t*t*t + b;
	}
}
