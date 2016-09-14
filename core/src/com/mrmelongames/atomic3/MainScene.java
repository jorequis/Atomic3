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

	CustomDraw customDraw;
	SpriteBatch spriteBatch;
	ShapeRenderer shapeRenderer;

	Chapa chapasAlly[];
	Chapa chapasEnemy[];

	Sprite nextChapaIndicator1;
	Sprite nextChapaIndicator2;
	Sprite nextChapaIndicator3;
	float deltaTimeIndicator1 = 0;
	float deltaTimeIndicator2 = 1/3f;
	float deltaTimeIndicator3 = 2/3f;

	Orbe orbeAlly;
	Orbe orbeEnemy;

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

	final int ACTION_DOWN = 0;
	final int ACTION_UP = 1;
	final int ACTION_MOVE = 2;

	Orbe orbeSelected;
	Chapa selectedChapa;
	Chapa nextChapa;
	boolean inAttack;
	int turnoChapaAlly;
	int turnoChapaEnemy;

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

		orbeAlly = new Orbe();
		orbeAlly.vidaMax = 50;
		orbeAlly.vida = orbeAlly.vidaMax;
		orbeAlly.x = screenWidth / 2;
		orbeAlly.y = (int) (30 * designScale);
		orbeAlly.radio = screenWidth / 10;
		orbeAlly.teamID = 0;

		orbeEnemy = new Orbe();
		orbeEnemy.vidaMax = 50;
		orbeEnemy.vida = orbeEnemy.vidaMax;
		orbeEnemy.x = screenWidth / 2;
		orbeEnemy.y = screenHeight - (int) (30 * designScale);
		orbeEnemy.radio = screenWidth / 10;
		orbeEnemy.teamID = 1;

		nextChapaIndicator1 = new Sprite(new Texture("nextc.png"));
		nextChapaIndicator1.setSize(nextChapaIndicator1.getWidth() * designScale, nextChapaIndicator1.getHeight() * designScale);
		nextChapaIndicator2 = new Sprite(new Texture("nextc.png"));
		nextChapaIndicator2.setSize(nextChapaIndicator2.getWidth() * designScale, nextChapaIndicator2.getHeight() * designScale);
		nextChapaIndicator3 = new Sprite(new Texture("nextc.png"));
		nextChapaIndicator3.setSize(nextChapaIndicator3.getWidth() * designScale, nextChapaIndicator3.getHeight() * designScale);

		int[] ataques = {3, 2, 1};
		int[] vidas = {1, 2, 5};
		int[] turnos = {1, 2, 3};

		teamTurn = 0;
		turnoChapaAlly = 0;
		chapasAlly = new Chapa[3];
		for(int i = 0; i < chapasAlly.length; i++) {
			chapasAlly[i] = new Chapa("greenc.png", screenWidth, screenHeight, designScale);
			chapasAlly[i].setPosition(415 + 317/2 + 707 * i, designHeight - 3658 - 347/2);
			chapasAlly[i].setVelocity((int)(75 * designScale));
			chapasAlly[i].mass = 1;
			chapasAlly[i].setColor(new Color(0, 0.85f, 1, 1));
			chapasAlly[i].teamID = 0;
			chapasAlly[i].setParams(vidas[i], ataques[i], turnos[i]);
		}
		nextChapa = chapasAlly[0];

		chapasEnemy = new Chapa[3];
		turnoChapaEnemy = 0;
		for(int i = 0; i < chapasEnemy.length; i++) {
			chapasEnemy[i] = new Chapa("redc.png", screenWidth, screenHeight, designScale);
			chapasEnemy[i].setPosition(415 + 317/2 + 707 * i, 3658 + 347/2);
			chapasEnemy[i].setVelocity((int)(75 * designScale));
			chapasEnemy[i].mass = 1;
			chapasEnemy[i].setColor(new Color(0.95f, 0.2f, 0.5f, 1));
			chapasEnemy[i].teamID = 1;
			chapasEnemy[i].setParams(vidas[i], ataques[i], turnos[i]);
		}
	}

	@Override
	public void render () {
		Gdx.gl20.glClearColor(0.3f, 0.35f, 0.3f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		deltaTime += Gdx.graphics.getDeltaTime();

		if(Gdx.graphics.getDeltaTime() > 0.5f){
			deltaTimeIndicator1 = 0f;
			deltaTimeIndicator2 = 1f/3f;
			deltaTimeIndicator3 = 2f/3f;
		} else {
			deltaTimeIndicator1 = deltaTimeIndicator1 >= 1 ? 0 : deltaTimeIndicator1;
			deltaTimeIndicator2 = deltaTimeIndicator2 >= 1 ? 0 : deltaTimeIndicator2;
			deltaTimeIndicator3 = deltaTimeIndicator3 >= 1 ? 0 : deltaTimeIndicator3;

			deltaTimeIndicator1 += Gdx.graphics.getDeltaTime() / 2f;
			deltaTimeIndicator2 += Gdx.graphics.getDeltaTime() / 2f;
			deltaTimeIndicator3 += Gdx.graphics.getDeltaTime() / 2f;
		}

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
			if(c.isColliding(orbeAlly)){
				handleCollision(c, orbeAlly, false);
			} else if(c.isColliding(orbeEnemy)){
				handleCollision(c, orbeEnemy, false);
				c.colidingWithOrbes.remove(orbeAlly);
			} else {
				c.colidingWithOrbes.remove(orbeEnemy);
			}

		for(Chapa c : chapasEnemy)
			if(c.isColliding(orbeAlly)){
				handleCollision(c, orbeAlly, false);
			} else if(c.isColliding(orbeEnemy)){
				handleCollision(c, orbeEnemy, false);
				c.colidingWithOrbes.remove(orbeAlly);
			} else {
				c.colidingWithOrbes.remove(orbeEnemy);
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
		shapeRenderer.rect(0, screenHeight / 2 - 60 * designScale, screenWidth * orbeAlly.vida / orbeAlly.vidaMax, 100 * designScale);
		shapeRenderer.setColor(1, 0, 0, 0.5f);
		shapeRenderer.rect(0, screenHeight / 2 + 60 * designScale, screenWidth * orbeEnemy.vida / orbeEnemy.vidaMax, 100 * designScale);

		shapeRenderer.setColor(0.4f, 0.79f, 1, 0.5f);
		shapeRenderer.circle(orbeAlly.x, orbeAlly.y, orbeAlly.radio);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(orbeEnemy.x, orbeEnemy.y, orbeEnemy.radio);

		if(inAttack && nextChapa != null) {
			shapeRenderer.setColor(0, 1, 0, 0.5f);
			shapeRenderer.circle(nextChapa.getX() + nextChapa.getWidth() / 2, nextChapa.getY() + nextChapa.getHeight() / 2, nextChapa.attackRadius);
		}
		customDraw.changeToSpriteBatch();

		nextChapaIndicator1.setSize(317 * deltaTimeIndicator1 * designScale + 317 * designScale, 347 * deltaTimeIndicator1 * designScale + 347 * designScale);
		nextChapaIndicator1.setPosition(nextChapa.getX() + nextChapa.getWidth() / 2 - nextChapaIndicator1.getWidth() / 2, nextChapa.getY() + nextChapa.getHeight() / 2 - nextChapaIndicator1.getHeight() / 2);
		nextChapaIndicator1.setColor(1, 1, 1, 0.85f - deltaTimeIndicator1 < 0 ? 0 : 0.85f - deltaTimeIndicator1);
		nextChapaIndicator1.draw(spriteBatch);

		nextChapaIndicator2.setSize(317 * deltaTimeIndicator2 * designScale + 317 * designScale, 347 * deltaTimeIndicator2 * designScale + 347 * designScale);
		nextChapaIndicator2.setPosition(nextChapa.getX() + nextChapa.getWidth() / 2 - nextChapaIndicator2.getWidth() / 2, nextChapa.getY() + nextChapa.getHeight() / 2 - nextChapaIndicator2.getHeight() / 2);
		nextChapaIndicator2.setColor(1, 1, 1, 0.85f - deltaTimeIndicator2 < 0 ? 0 : 0.85f - deltaTimeIndicator2);
		nextChapaIndicator2.draw(spriteBatch);

		nextChapaIndicator3.setSize(317 * deltaTimeIndicator3 * designScale + 317 * designScale, 347 * deltaTimeIndicator3 * designScale + 347 * designScale);
		nextChapaIndicator3.setPosition(nextChapa.getX() + nextChapa.getWidth() / 2 - nextChapaIndicator3.getWidth() / 2, nextChapa.getY() + nextChapa.getHeight() / 2 - nextChapaIndicator3.getHeight() / 2);
		nextChapaIndicator3.setColor(1, 1, 1, 0.85f - deltaTimeIndicator3 < 0 ? 0 : 0.85f - deltaTimeIndicator3);
		nextChapaIndicator3.draw(spriteBatch);

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
		turnoChapaAlly = 0;
		for(int i = 0; i < chapasAlly.length; i++)
			chapasAlly[i].reset();

		turnoChapaEnemy = 0;
		for(int i = 0; i < chapasEnemy.length; i++)
			chapasEnemy[i].reset();

		nextChapa = chapasAlly[0];
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

		if(isInside(orbeAlly, x, y)){
			orbeSelected = orbeAlly;
		} else if(isInside(orbeEnemy, x, y)){
			orbeSelected = orbeEnemy;
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

		if(x - initTouchX != 0 && y - initTouchY != 0) inAttack = false;
	}

	public void inputUp(float x, float y, float velocityX) {
		log((nextChapa != null) + " " + inAttack + " " + (selectedChapa != null));
		if (nextChapa != null) {
			if(inAttack) {
				log(teamTurn);
				if (selectedChapa == nextChapa) {
				} else if (selectedChapa != null && teamTurn != selectedChapa.teamID) {
					if (isInside(selectedChapa, x, y)) {
						float r = nextChapa.attackRadius;
						float a = nextChapa.getX() + nextChapa.getWidth() / 2;
						float b = nextChapa.getY() + nextChapa.getHeight() / 2;
						float one = ((float) Math.pow((x - a), 2));
						float two = ((float) Math.pow((y - b), 2));
						if (one + two < r * r) {
							nextChapa.turnos--;
							selectedChapa.vida -= nextChapa.ataque;
							if (selectedChapa.vida > 0)
								nextChapa.vida -= selectedChapa.ataque;
						}
					}
					inAttack = false;
				} else if (orbeSelected != null && teamTurn != orbeSelected.teamID) {
					if (isInside(orbeSelected, x, y)) {
						if (orbeSelected.teamID != teamTurn) {
							nextChapa.turnos--;
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
						nextChapa.turnos--;
					} else {
						float xC = chapasEnemy[turnoChapaEnemy].getSprite().getX() + chapasEnemy[turnoChapaEnemy].getWidth() / 2;
						float yC = chapasEnemy[turnoChapaEnemy].getSprite().getY() + chapasEnemy[turnoChapaEnemy].getHeight() / 2;

						chapasEnemy[turnoChapaEnemy].setPath(xC - (touchX - initTouchX), yC - (touchY - initTouchY), deltaTime, potencia);
						nextChapa.turnos--;
					}

					touchX = -1;
					touchY = -1;
				}
			}

			if (nextChapa.turnos == 0) {
				nextChapa.turnos = nextChapa.turnosInit;

				if(teamTurn == 0) {
					turnoChapaAlly = turnoChapaAlly == chapasAlly.length - 1 ? 0 : turnoChapaAlly + 1;
					nextChapa = chapasEnemy[turnoChapaEnemy];
				} else {
					turnoChapaEnemy = turnoChapaEnemy == chapasEnemy.length - 1 ? 0 : turnoChapaEnemy + 1;
					nextChapa = chapasAlly[turnoChapaAlly];
				}

				teamTurn = teamTurn == 0 ? 1 : 0;
				inAttack = false;
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

		nextChapaIndicator1.getTexture().dispose();
		nextChapaIndicator2.getTexture().dispose();
		nextChapaIndicator3.getTexture().dispose();
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
