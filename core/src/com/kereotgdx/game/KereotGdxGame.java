package com.kereotgdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import lombok.Getter;

public class KereotGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
//	private Texture img;
//	private MyAnim anim;
	private MyAtlasAnim animEngMuscShoots;
	private MyAtlasAnim animEngMuscStands;
	private MyAtlasAnim animEngMuscWalks;
	private MyAtlasAnim tmpA;
	private Music music;
	private Sound sound;
	private MyInputProcessor myInputProcessor;
	private float x, y;
	private final int SIR = 1; // Standard image row
	private final int SIC = 14; // Standard image column
	private final int SFPS = 15;
	private boolean flip;
	private boolean fire;
	private OrthographicCamera camera;
	private PsyX psyX;
	private BodyPartCreator bpc = new BodyPartCreator();
	private Body bodyPlayer;
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	private final float MAX_VELOCITY = 250f;

	@Override
	public void create () {
		map = new TmxMapLoader().load("map/mapd.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map);

		psyX = new PsyX();
		BodyDef def = new BodyDef();
		FixtureDef fDef = new FixtureDef();
		PolygonShape shape;

//		shape = new PolygonShape();
//		bpc.create(psyX, def, fDef, shape, 1, 1, 0, 0,
//				250, 10, 1, 0 , 1, "Kubik");
//		for (int i = 0; i < 100; i++) {
//			shape = new PolygonShape();
//			bpc.create(psyX, def, fDef, shape, 4, 2,
//					MathUtils.random(-120, 120), MathUtils.random(60, 150),
//					10, 10, 1, 0 , 1, "Rubik");
//		}
//		shape = new PolygonShape();
//		Body bodyTest = bpc.createBody(psyX, def, 2, 2, MathUtils.random(-120, 120), MathUtils.random(60, 150));
//		bpc.createFixture(fDef, shape, bodyTest, 30, 30, 1, 0, 1, "Body");

//		shape = new PolygonShape();
//		bodyPlayer = bpc.createBody(psyX, def, 2, 2, 0, 128);
//		bpc.createFixture(fDef, shape, bodyPlayer, 0, 0, 0, 0, 0, "Body");

		MapLayer env = map.getLayers().get("Препятствия");
		Array<RectangleMapObject> rect = env.getObjects().getByType(RectangleMapObject.class);
		for (int i = 0; i < rect.size; i++) {
			float x = rect.get(i).getRectangle().width/2 + rect.get(i).getRectangle().x;
			float y = rect.get(i).getRectangle().height/2 + rect.get(i).getRectangle().y;
			float w = rect.get(i).getRectangle().width/2;
			float h = rect.get(i).getRectangle().height/2;
			shape = new PolygonShape();
			bpc.createObstacle(psyX, def, fDef, shape, 1, x, y, w, h, "Obstacle");
		}

		env = map.getLayers().get("Герой");
		RectangleMapObject hero = (RectangleMapObject) env.getObjects().get("Hero");
			float x = hero.getRectangle().width/2 + hero.getRectangle().x;
			float y = hero.getRectangle().height/2 + hero.getRectangle().y;
			float w = hero.getRectangle().width/2;
			float h = hero.getRectangle().height/2;
			shape = new PolygonShape();
			bodyPlayer = bpc.createBody(psyX, def, 4, 2, x, y);
			bpc.createFixture(fDef, shape, bodyPlayer, w, h, 0.05f, 0f, 0.05f, "Hero");
			bodyPlayer.setFixedRotation(true);

		myInputProcessor = new MyInputProcessor();
		Gdx.input.setInputProcessor(myInputProcessor);

		batch = new SpriteBatch();

		music = Gdx.audio.newMusic(Gdx.files.internal("cancionero175.mp3"));
		music.setVolume(0.025f);
		music.setLooping(true);
		music.play();

		sound = Gdx.audio.newSound(Gdx.files.internal("land_musket1.wav"));

//		img = new Texture("badlogic.jpg");
//		anim = new MyAnim("blast.png",6, 8, 15, Animation.PlayMode.LOOP);
		animEngMuscShoots = new MyAtlasAnim("ENG_MUSC.atlas","ENG_MUSC_FIRE_RIGHT", SIR, SIC, SFPS, Animation.PlayMode.NORMAL);
		animEngMuscStands = new MyAtlasAnim("ENG_MUSC.atlas","ENG_MUSC_STAND", SIR, SIC, SFPS, Animation.PlayMode.LOOP);
		animEngMuscWalks = new MyAtlasAnim("ENG_MUSC.atlas", "ENG_MUSC_WALK_RIGHT", SIR, SIC, SFPS, Animation.PlayMode.LOOP);

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		camera.position.x = Gdx.graphics.getWidth()/2;
//		camera.position.y = Gdx.graphics.getHeight()/2;

	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);

		camera.position.x = bodyPlayer.getPosition().x;
		camera.position.y = bodyPlayer.getPosition().y;
		camera.zoom = 0.5f;
		camera.update();

		mapRenderer.setView(camera);
		mapRenderer.render();

		if (fire) {
			tmpA = animEngMuscShoots;
			if (tmpA.anim.isAnimationFinished(tmpA.getTime())) {
				fire = false;
			}
		} else {
			tmpA = animEngMuscStands;
		}

		Vector2 vel = bodyPlayer.getLinearVelocity();
		Vector2 pos = bodyPlayer.getPosition();

//		x = Gdx.input.getX() - anim.draw().getRegionWidth()/2;
//		y = Gdx.graphics.getHeight() - Gdx.input.getY() - anim.draw().getRegionHeight()/2;
		if (!fire) {
			if (myInputProcessor.getOutString().contains("A") && vel.x > -MAX_VELOCITY) {
//				bodyPlayer.applyForceToCenter(new Vector2(-10000f, 0), true);
//				bodyPlayer.setLinearVelocity(new Vector2(-50f, 0));
				bodyPlayer.applyLinearImpulse(-250f, 0, pos.x, pos.y, true);
				tmpA = animEngMuscWalks;
				flip = true;
			}
			if (myInputProcessor.getOutString().contains("D") && vel.x < MAX_VELOCITY) {
//				bodyPlayer.applyForceToCenter(new Vector2(10000f, 0), true);
//				bodyPlayer.setLinearVelocity(new Vector2(50f, 0));
				bodyPlayer.applyLinearImpulse(250f, 0, pos.x, pos.y, true);
				tmpA = animEngMuscWalks;
				flip = false;
			}
			if (myInputProcessor.getOutString().contains("W") && vel.y == 0) {
				bodyPlayer.applyLinearImpulse(0, 6000f, pos.x, pos.y, true);
			}
			if (myInputProcessor.getOutString().contains("S")) {
//				bodyPlayer.setLinearVelocity(new Vector2(0, 0));
			}
			if (myInputProcessor.getOutString().contains("Space") && vel.y == 0) {
				bodyPlayer.setLinearVelocity(new Vector2(0, 50f));
			}
			if (myInputProcessor.isMovementKeyJustPressed()) {
				if (myInputProcessor.isAJustPressed() || myInputProcessor.isDJustPressed()) {
					bodyPlayer.setLinearVelocity(new Vector2(0, bodyPlayer.getLinearVelocity().y));
				}
				if (myInputProcessor.isWJustPressed() && vel.y >= 0) {
					bodyPlayer.setLinearVelocity(new Vector2(bodyPlayer.getLinearVelocity().x, 0));
				}
				myInputProcessor.setMovementKeyJustPressed(false);
			}
		}
//		System.out.println(myInputProcessor.getOutString());
		tmpA.setTime(Gdx.graphics.getDeltaTime());
		if (tmpA != animEngMuscStands) {
			if (!tmpA.draw().isFlipX() && flip) {
				tmpA.draw().flip(true, false);
			} else if (tmpA.draw().isFlipX() && !flip) {
				tmpA.draw().flip(true, false);
			}
		} else {
			if (!tmpA.draw().isFlipX() && !flip) {
				tmpA.draw().flip(true, false);
			} else if (tmpA.draw().isFlipX() && flip) {
				tmpA.draw().flip(true, false);
			}
		}


		if (Gdx.input.isButtonJustPressed(0)
				&& myInputProcessor.getOutString().isEmpty()
				&& !fire
				&& vel.y == 0) {
			sound.play(0.025f);
			tmpA = animEngMuscShoots;
			tmpA.resetTime();
			fire = true;
		}

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

//		if ((!flip && tmpA != animEngMuscStands) || (tmpA == animEngMuscStands && flip)) { // doh, STAND image is facing the wrong way...
//			batch.draw(tmpA.draw(), x, y);
//		} else {
//			batch.draw(tmpA.draw(), x + tmpA.draw().getRegionWidth(), y,
//					-tmpA.draw().getRegionWidth(), tmpA.draw().getRegionHeight());
//		}
		batch.draw(tmpA.draw(), bodyPlayer.getPosition().x - 16, bodyPlayer.getPosition().y - 24);
		batch.end();

		psyX.step();
		psyX.debugDraw(camera);
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
//		img.dispose();
//		anim.dispose();
		animEngMuscShoots.dispose();
		animEngMuscWalks.dispose();
		animEngMuscStands.dispose();
		music.dispose();
		sound.dispose();
		tmpA.dispose();
		map.dispose();
		mapRenderer.dispose();
	}
}
