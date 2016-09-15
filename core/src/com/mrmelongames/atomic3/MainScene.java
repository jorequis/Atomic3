package com.mrmelongames.atomic3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

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
	int teamTurn;

	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);

		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		touchX = -1;
		touchY = -1;

		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		customDraw = new CustomDraw(spriteBatch, shapeRenderer);

		designScale = Gdx.graphics.getWidth() / (float) designWidth;

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

		int[] ataques = {3, 2, 1};
		int[] vidas = {1, 2, 5};

		teamTurn = 0;
		chapasAlly = new Chapa[3];
		for(int i = 0; i < chapasAlly.length; i++) {
			chapasAlly[i] = new Chapa("greenc.png", screenWidth, screenHeight, designScale);
			chapasAlly[i].setPosition(415 + 317/2 + 707 * i, designHeight - 3658 - 347/2);
			chapasAlly[i].setVelocity((int)(75 * designScale));
			chapasAlly[i].mass = 1;
			chapasAlly[i].setColor(new Color(0, 0.85f, 1, 1));
			chapasAlly[i].teamID = 0;
			chapasAlly[i].setParams(vidas[i], ataques[i]);
		}

		chapasEnemy = new Chapa[3];
		for(int i = 0; i < chapasEnemy.length; i++) {
			chapasEnemy[i] = new Chapa("redc.png", screenWidth, screenHeight, designScale);
			chapasEnemy[i].setPosition(415 + 317/2 + 707 * i, 3658 + 347/2);
			chapasEnemy[i].setVelocity((int)(75 * designScale));
			chapasEnemy[i].mass = 1;
			chapasEnemy[i].setColor(new Color(0.95f, 0.2f, 0.5f, 1));
			chapasEnemy[i].teamID = 1;
			chapasEnemy[i].setParams(vidas[i], ataques[i]);
		}
	}

	@Override
	public void render () {
		Gdx.gl20.glClearColor(0.3f, 0.35f, 0.3f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		deltaTime += Gdx.graphics.getDeltaTime();

		for(Chapa c : chapasAlly)
		c.update(deltaTime);

		for(Chapa c : chapasEnemy)
		c.update(deltaTime);

		Chapa c1 = chapasAlly[0];
		Chapa c2 = chapasAlly[1];
		if(c1.isColliding(c2)){
			handleCollision(c1, c2, true);
		} else {
			c1.colidingWithChapas.remove(c2);
			c2.colidingWithChapas.remove(c1);
		}

		c1 = chapasAlly[0];
		c2 = chapasAlly[2];
		if(c1.isColliding(c2)) {
			handleCollision(c1, c2, true);
		} else {
			c1.colidingWithChapas.remove(c2);
			c2.colidingWithChapas.remove(c1);
		}

		c1 = chapasAlly[1];
		c2 = chapasAlly[2];
		if(c1.isColliding(c2)){
			handleCollision(c1, c2, true);
		} else {
			c1.colidingWithChapas.remove(c2);
			c2.colidingWithChapas.remove(c1);
		}

		for(Chapa a : chapasAlly)
			for(Chapa e : chapasEnemy)
				if(a.isColliding(e)){
					handleCollision(a, e, false);
				} else {
					a.colidingWithChapas.remove(e);
					e.colidingWithChapas.remove(a);
				}

		c1 = chapasEnemy[0];
		c2 = chapasEnemy[1];
		if(c1.isColliding(c2)){
			handleCollision(c1, c2, true);
		} else {
			c1.colidingWithChapas.remove(c2);
			c2.colidingWithChapas.remove(c1);
		}

		c1 = chapasEnemy[0];
		c2 = chapasEnemy[2];
		if(c1.isColliding(c2)) {
			handleCollision(c1, c2, true);
		} else {
			c1.colidingWithChapas.remove(c2);
			c2.colidingWithChapas.remove(c1);
		}

		c1 = chapasEnemy[1];
		c2 = chapasEnemy[2];
		if(c1.isColliding(c2)){
			handleCollision(c1, c2, true);
		} else {
			c1.colidingWithChapas.remove(c2);
			c2.colidingWithChapas.remove(c1);
		}

		for(Chapa c : chapasAlly)
			for(Orbe o : orbes)
				if(c.isColliding(o)){
					handleCollision(c, o, false);
				} else {
					c.colidingWithOrbes.remove(o);
				}

		customDraw.beginSpriteBatch();
		customDraw.changeToShapeRenderer(ShapeRenderer.ShapeType.Filled);
		if(touchX != -1 && nextChapa != null) {
			float x = nextChapa.getX() + nextChapa.getWidth() / 2;
			float y = nextChapa.getY() + nextChapa.getHeight() / 2;
			shapeRenderer.setColor(1, 1, 1, 1);
			shapeRenderer.rectLine(x, y, x - (touchX - initTouchX), y - (touchY - initTouchY), 10 * designScale);
		}

		shapeRenderer.setColor(0.4f, 0.79f, 1, 0.5f);
		shapeRenderer.rect(0, screenHeight / 2 - 60 * designScale, screenWidth * orbes[0].vida / orbes[0].vidaMax, 100 * designScale);
		shapeRenderer.setColor(1, 0, 0, 0.5f);
		shapeRenderer.rect(0, screenHeight / 2 + 60 * designScale, screenWidth * orbes[1].vida / orbes[1].vidaMax, 100 * designScale);

		shapeRenderer.setColor(0.4f, 0.79f, 1, 0.5f);
		shapeRenderer.circle(orbes[0].x, orbes[0].y, orbes[0].radio);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(orbes[1].x, orbes[1].y, orbes[1].radio);

		if(inAttack && nextChapa != null) {
			shapeRenderer.setColor(0, 1, 0, 0.5f);
			shapeRenderer.circle(nextChapa.getX() + nextChapa.getWidth() / 2, nextChapa.getY() + nextChapa.getHeight() / 2, nextChapa.attackRadius);
		}
		customDraw.changeToSpriteBatch();

		for(int i = 0; i < chapasAlly.length; i++)
			chapasAlly[i].draw(spriteBatch);
		for(int i = 0; i < chapasEnemy.length; i++)
			chapasEnemy[i].draw(spriteBatch);
		customDraw.endSpriteBatch();
	}

	void handleCollision(Chapa a, Orbe b, boolean allys) {
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
			double v2yr = v2i * Math.sin(ang2 - phi);

			double v1fxr = -v1xr+2*v2xr;
			double v2fxr = v2xr;
			double v1fyr = v1yr;
			double v2fyr = v2yr;

			double v1fx = Math.cos(phi) * v1fxr + Math.cos(phi + Math.PI / 2f) * v1fyr;
			double v1fy = Math.sin(phi) * v1fxr + Math.sin(phi + Math.PI / 2f) * v1fyr;
			double v2fx = Math.cos(phi) * v2fxr + Math.cos(phi + Math.PI / 2f) * v2fyr;
			double v2fy = Math.sin(phi) * v2fxr + Math.sin(phi + Math.PI / 2f) * v2fyr;

			a.direction = new Vector2((float) v1fx, (float) v1fy).nor();

			/*if(a.teamID != b.teamID){
				b.vida -= a.ataque;
			}*/
		}
	}

	void handleCollision(Chapa a, Chapa b, boolean allys){
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

			/*float m1 = a.mass;
			float m2 = b.mass;

			double v1fxr = ((m1-m2)*v1xr+(m2+m2)*v2xr)/(m1+m2);
			double v2fxr = ((m1+m1)*v1xr+(m2-m1)*v2xr)/(m1+m2);
			double v1fyr = v1yr;
			double v2fyr = v2yr;*/

			double v1fxr = v2xr;
			double v2fxr = v1xr;
			double v1fyr = v1yr;
			double v2fyr = v2yr;

			double v1fx = Math.cos(phi) * v1fxr + Math.cos(phi + Math.PI / 2f) * v1fyr;
			double v1fy = Math.sin(phi) * v1fxr + Math.sin(phi + Math.PI / 2f) * v1fyr;
			double v2fx = Math.cos(phi) * v2fxr + Math.cos(phi + Math.PI / 2f) * v2fyr;
			double v2fy = Math.sin(phi) * v2fxr + Math.sin(phi + Math.PI / 2f) * v2fyr;

			a.direction = new Vector2((float) v1fx, (float) v1fy).nor();
			//a.velocidad = new Vector2((float) v1fx, (float) v1fy).len();

			b.direction = new Vector2((float) v2fx, (float) v2fy).nor();
			//b.velocidad = new Vector2((float) v2fx, (float) v2fy).len();

			/*if(a.teamID != b.teamID){
				if(teamTurn == a.teamID){
					b.vida -= a.ataque;
					if(b.vida > 0)
						a.vida -= b.ataque;
				}
				if(teamTurn == b.teamID){
					a.vida -= b.ataque;
					if(a.vida > 0)
						b.vida -= a.ataque;
				}
			}*/
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

	void reset(){
		teamTurn = 0;

		orbes[0].turnos = orbes[0].turnosInit;
		orbes[1].turnos = orbes[1].turnosInit;

		for(int i = 0; i < chapasAlly.length; i++)
			chapasAlly[i].reset();

		for(int i = 0; i < chapasEnemy.length; i++)
			chapasEnemy[i].reset();
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
		log((one+two) + " " + r);
		return  (one + two < r);
	}

	public void inputDown (float x, float y){ // X, Y son las coordenadas del dedo lul
		//y = screenHeight - y;
		for (int i = 0; i < chapasAlly.length; i++) {
			if (isInside(chapasAlly[i], x, y)) {
				selectedChapa = chapasAlly[i];
			}
		}

		if(selectedChapa == null)
		for (int i = 0; i < chapasEnemy.length; i++) {
			if (isInside(chapasEnemy[i], x, y)) {
				selectedChapa = chapasEnemy[i];
			}
		}

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
	}

	public void inputMove(float x, float y, float velocityX){

		touchX = (int) x;
		touchY = (int) y;

		if(x - initTouchX != 0 && y - initTouchY != 0) chapaInAttack = null;
	}

	public void inputUp(float x, float y, float velocityX) {
		if (selectedChapa != null) {
			if(inAttack) {
				if (teamTurn != selectedChapa.teamID) {
					if (isInside(selectedChapa, x, y)) {
						float r = nextChapa.attackRadius;
						float a = nextChapa.getX() + nextChapa.getWidth() / 2;
						float b = nextChapa.getY() + nextChapa.getHeight() / 2;
						float one = ((float) Math.pow((x - a), 2));
						float two = ((float) Math.pow((y - b), 2));
						if (one + two < r * r) {
							orbes[nextChapa.teamID].turnos--;
							selectedChapa.vida -= nextChapa.ataque;
							nextChapa.vida -= selectedChapa.ataque;

							if(selectedChapa.vida < 0) orbes[selectedChapa.teamID].vida += selectedChapa.vida;
							if(nextChapa.vida < 0) orbes[nextChapa.teamID].vida += nextChapa.vida;
						}
					}
					inAttack = false;
				} else if (orbeSelected != null && teamTurn != orbeSelected.teamID) {
					if (isInside(orbeSelected, x, y)) {
						if (orbeSelected.teamID != teamTurn) {
							orbes[nextChapa.teamID].turnos--;
							orbeSelected.vida -= nextChapa.ataque;
						}
					}
				}
			} else {
				if (x - initTouchX == 0 && y - initTouchY == 0) {
					inAttack = true;
				} else {
					inAttack = false;
					float potencia = new Vector2(x - initTouchX, y - initTouchY).len() / designScale / 600;
					potencia = potencia > 1 ? 1 : potencia;

					if(teamTurn == 0) {
						float xC = chapasAlly[turnoChapaAlly].getSprite().getX() + chapasAlly[turnoChapaAlly].getWidth() / 2;
						float yC = chapasAlly[turnoChapaAlly].getSprite().getY() + chapasAlly[turnoChapaAlly].getHeight() / 2;

						chapasAlly[turnoChapaAlly].setPath(xC - (touchX - initTouchX), yC - (touchY - initTouchY), deltaTime, potencia);
						orbes[nextChapa.teamID].turnos--;
					} else {
						float xC = chapasEnemy[turnoChapaEnemy].getSprite().getX() + chapasEnemy[turnoChapaEnemy].getWidth() / 2;
						float yC = chapasEnemy[turnoChapaEnemy].getSprite().getY() + chapasEnemy[turnoChapaEnemy].getHeight() / 2;

						chapasEnemy[turnoChapaEnemy].setPath(xC - (touchX - initTouchX), yC - (touchY - initTouchY), deltaTime, potencia);
						orbes[nextChapa.teamID].turnos--;
					}

					touchX = -1;
					touchY = -1;
				}
			}

			if (orbes[teamTurn].turnos == 0) {
				orbes[teamTurn].turnos = orbes[teamTurn].turnosInit;

				teamTurn = teamTurn == 0 ? 1 : 0;
				chapaInAttack = null;
			}
		}
		selectedChapa = null;
		orbeSelected = null;
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

	public void log(String s){
		Gdx.app.log("Atomic", s);
	}
	public void log(int i){
		Gdx.app.log("Atomic", i + "");
	}
	public void log(float f){
		Gdx.app.log("Atomic", f + "");
	}
	public void log(boolean b){
		Gdx.app.log("Atomic", b + "");
	}

	public float easeInCubic (float t, float b, float c, float d) { // t: tiempo, b: inicio, c: inicio - final, d: duracion
		t /= d;
		return c*t*t*t + b;
	}
}
