package com.kereotgdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kereotgdx.game.BodyPartCreator;
import com.kereotgdx.game.MyAtlasAnim;
import com.kereotgdx.game.MyInputProcessor;
import com.kereotgdx.game.PsyX;

public class GameScreen implements Screen {
    Game game;

    private SpriteBatch batch;
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
    private final float MAX_VELOCITY = 0.025f;

    public GameScreen(Game game) {
        this.game = game;

        map = new TmxMapLoader().load("map/mapd.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        psyX = new PsyX();
        BodyDef def = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        PolygonShape shape;

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
        bodyPlayer = bpc.createBody(psyX, def, 1, 2, x, y);
        bpc.createFixture(fDef, shape, bodyPlayer, w, h, 0.015f, 0f, 0.05f, "Hero");
        bodyPlayer.setFixedRotation(true);

        myInputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(myInputProcessor);

        batch = new SpriteBatch();

        music = Gdx.audio.newMusic(Gdx.files.internal("cancionero175.mp3"));
        music.setVolume(0.025f);
        music.setLooping(true);
        music.play();

        sound = Gdx.audio.newSound(Gdx.files.internal("land_musket1.wav"));

        animEngMuscShoots = new MyAtlasAnim("ENG_MUSC.atlas","ENG_MUSC_FIRE_RIGHT", SIR, SIC, SFPS, Animation.PlayMode.NORMAL);
        animEngMuscStands = new MyAtlasAnim("ENG_MUSC.atlas","ENG_MUSC_STAND", SIR, SIC, SFPS, Animation.PlayMode.LOOP);
        animEngMuscWalks = new MyAtlasAnim("ENG_MUSC.atlas", "ENG_MUSC_WALK_RIGHT", SIR, SIC, SFPS, Animation.PlayMode.LOOP);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);

        camera.position.x = bodyPlayer.getPosition().x * bpc.PPM;
        camera.position.y = bodyPlayer.getPosition().y * bpc.PPM;
        camera.zoom = 0.5f;
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        Vector2 vel = bodyPlayer.getLinearVelocity();
        Vector2 pos = bodyPlayer.getPosition();

        if (fire) {
            tmpA = animEngMuscShoots;
            if (tmpA.getAnim().isAnimationFinished(tmpA.getTime())) {
                fire = false;
            }
        } else if (vel.x == 0) {
            tmpA = animEngMuscStands;
        }

        if (!fire) {
            if (myInputProcessor.getOutString().contains("A") && vel.x > -MAX_VELOCITY) {
                bodyPlayer.applyLinearImpulse(-MAX_VELOCITY, 0, pos.x, pos.y, true);
                tmpA = animEngMuscWalks;
                flip = true;
            }
            if (myInputProcessor.getOutString().contains("D") && vel.x < MAX_VELOCITY) {
                bodyPlayer.applyLinearImpulse(MAX_VELOCITY, 0, pos.x, pos.y, true);
                tmpA = animEngMuscWalks;
                flip = false;
            }
            if (myInputProcessor.getOutString().contains("W") && vel.y == 0) {
                bodyPlayer.applyLinearImpulse(0, 0.05f, pos.x, pos.y, true);
            }
            if (myInputProcessor.getOutString().contains("S")) {
//				bodyPlayer.setLinearVelocity(new Vector2(0, 0));
            }
            if (myInputProcessor.getOutString().contains("Space") && vel.y == 0) {
                bodyPlayer.setLinearVelocity(new Vector2(0, 5f));
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

        float x = (bodyPlayer.getPosition().x * bpc.PPM - 16);
        float y = (bodyPlayer.getPosition().y * bpc.PPM - 24);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(tmpA.draw(), x, y);
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
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
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
