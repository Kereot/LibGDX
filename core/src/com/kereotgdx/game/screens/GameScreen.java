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
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kereotgdx.game.*;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class GameScreen implements Screen {
    Game game;

    private SpriteBatch batch;
    private final MyAtlasAnim animEngMuscShoots;
    private final MyAtlasAnim animEngMuscStands;
    private final MyAtlasAnim animEngMuscWalks;
    private MyAtlasAnim tmpA;
    private Music music;
    private Sound heroShootSound;
    private Music enemySwingSound;
    private final MyInputProcessor myInputProcessor;
    private final int SIR = 1; // Standard image row
    private final int SFPS = 15;
    private boolean flip;
    private boolean fire;
    private final OrthographicCamera camera;
    private PsyX psyX;
    private BodyPartCreator bpc = new BodyPartCreator();
    private final Body bodyPlayer;
    private List<Enemy> enemiesList = new ArrayList<>();
    private TiledMap map;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final float MAX_VELOCITY = 0.025f;
    private final MyAnim flagEng;
    private int coinCount = 0;
    private int totalCoins = 0;
    private HashMap<Fixture, Float> damageMap = new HashMap<>();

    public static List<Body> bodyToDelete;
    public static List<Body> engagedEnemies = new ArrayList<>();
    public Bullet bullet;
    public Tower tower;
//    public Enemy enemy;
    public static List<Bullet> bullets;
    @Setter
    private static boolean fatality;
    private final Label font;

    private Stats heroStats;

    public GameScreen(Game game) {
        font = new Label(15);

        bodyToDelete = new ArrayList<>();
        bullets = new ArrayList<>();
        flagEng = new MyAnim("flag_eng.png", SIR, 25, SFPS, Animation.PlayMode.LOOP);

        heroStats = new Stats(100f);

        this.game = game;

        map = new TmxMapLoader().load("map/mapd.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        psyX = new PsyX();
        BodyDef def = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        PolygonShape shape;
        ChainShape chainShape;

        MapLayer env = map.getLayers().get("Препятствия");
        Array<RectangleMapObject> rect = env.getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < rect.size; i++) {
            float x = rect.get(i).getRectangle().width/2 + rect.get(i).getRectangle().x;
            float y = rect.get(i).getRectangle().height/2 + rect.get(i).getRectangle().y;
            float w = rect.get(i).getRectangle().width/2;
            float h = rect.get(i).getRectangle().height/2;
            shape = new PolygonShape();
            String name = "Obstacle";
            if (rect.get(i).getName() != null && rect.get(i).getName().equals("Lava")) {name = "Lava";}
            if (rect.get(i).getName() != null && rect.get(i).getName().equals("stop")) {
                bpc.createObstacle(psyX, def, fDef, shape, 1, x, y, w, h, name, Types.STOP, Types.MASK_STOP);
            } else if (rect.get(i).getName() != null && rect.get(i).getName().equals("Tower")) {
                tower = new Tower(psyX, x, y);
            } else {
                bpc.createObstacle(psyX, def, fDef, shape, 1, x, y, w, h, name, Types.SCENERY, Types.MASK_SCENERY);
            }
        }

        Array<PolylineMapObject> line = env.getObjects().getByType(PolylineMapObject.class);
        for (int i = 0; i < line.size; i++) {
            def = new BodyDef();
            float[] tf = line.get(i).getPolyline().getTransformedVertices();
            for (int j = 0; j < tf.length; j++) {
                tf[j] /= bpc.PPM;
            }
            chainShape = new ChainShape();
            chainShape.createChain(tf);
            String name = "Obstacle";
            if (line.get(i).getName() != null && line.get(i).getName().equals("Lava")) {name = "Lava";}
            bpc.createAngledObstacle(psyX, def, fDef, chainShape, 1, name);
        }

        env = map.getLayers().get("Монетки");
        Array<RectangleMapObject> coins = env.getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < coins.size; i++) {
            float x = coins.get(i).getRectangle().width/2 + coins.get(i).getRectangle().x;
            float y = coins.get(i).getRectangle().height/2 + coins.get(i).getRectangle().y;
            float w = coins.get(i).getRectangle().width/2;
            float h = coins.get(i).getRectangle().height/2;
            shape = new PolygonShape();
            bpc.createObstacle(psyX, def, fDef, shape, 1, x, y, w, h, "coins", Types.COIN, Types.MASK_COIN);
            totalCoins++;
        }

        env = map.getLayers().get("Урон");
        Array<RectangleMapObject> damage = env.getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < damage.size; i++) {
            float x = damage.get(i).getRectangle().width/2 + damage.get(i).getRectangle().x;
            float y = damage.get(i).getRectangle().height/2 + damage.get(i).getRectangle().y;
            float w = damage.get(i).getRectangle().width/2;
            float h = damage.get(i).getRectangle().height/2;
            shape = new PolygonShape();
            damageMap.put(
                    bpc.createDamageSensor(psyX, def, fDef, shape, x, y, w, h, "damage"),
                    damage.get(i).getProperties().get("damage", Float.class));
        }

        env = map.getLayers().get("Враги");
        Array<RectangleMapObject> enemies = env.getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < enemies.size; i++) {
            float x = enemies.get(i).getRectangle().width / 2 + enemies.get(i).getRectangle().x;
            float y = enemies.get(i).getRectangle().height / 2 + enemies.get(i).getRectangle().y;
            float w = enemies.get(i).getRectangle().width / 2;
            float h = enemies.get(i).getRectangle().height / 2;
            enemiesList.add(new Enemy(psyX, x, y, w, h));
        }

        env = map.getLayers().get("Герой");
        RectangleMapObject hero = (RectangleMapObject) env.getObjects().get("Hero");
        float x = hero.getRectangle().width/2 + hero.getRectangle().x;
        float y = hero.getRectangle().height/2 + hero.getRectangle().y;
        float w = hero.getRectangle().width/2;
        float h = hero.getRectangle().height/2;
        shape = new PolygonShape();
        bodyPlayer = bpc.createBody(psyX, def, 1, 2, x, y);
        bodyPlayer.setUserData("Hero");
        bpc.createFixture(fDef, shape, bodyPlayer, w, h, 0.015f, 0f, 0.05f, "Hero", Types.HERO, Types.MASK_HERO);
        bodyPlayer.setFixedRotation(true);


        myInputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(myInputProcessor);

        batch = new SpriteBatch();

        music = Gdx.audio.newMusic(Gdx.files.internal("cancionero175.mp3"));
        music.setVolume(0.025f);
        music.setLooping(true);
        music.play();

        heroShootSound = Gdx.audio.newSound(Gdx.files.internal("land_musket1.wav"));
        enemySwingSound = Gdx.audio.newMusic(Gdx.files.internal("land_sword2.wav"));

        animEngMuscShoots = new MyAtlasAnim("ENG_MUSC.atlas","ENG_MUSC_FIRE_RIGHT", SIR, 14, SFPS, Animation.PlayMode.NORMAL);
        animEngMuscStands = new MyAtlasAnim("ENG_MUSC.atlas","ENG_MUSC_STAND", SIR, 14, SFPS, Animation.PlayMode.LOOP);
        animEngMuscWalks = new MyAtlasAnim("ENG_MUSC.atlas", "ENG_MUSC_WALK_RIGHT", SIR, 14, SFPS, Animation.PlayMode.LOOP);

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

        if (MyContactListener.isDamaged()) {
            heroStats.getHit(damageMap.get(MyContactListener.getDamageObject()));
        }

        if (MyContactListener.isEngaged()) {
            heroStats.getHit(1.5f);
            for (Enemy enemy : enemiesList) {
                for (Body engagedEnemy : engagedEnemies) {
                    if (engagedEnemy.equals(enemy.getBody())) {
                        enemy.engage();
                        if (!enemySwingSound.isPlaying()) {
                            enemySwingSound.setVolume(0.025f);
                            enemySwingSound.setLooping(false);
                            enemySwingSound.play();
                        }
                    }
                }
            }
        }

        if (!MyContactListener.isEngaged()) {
            for (Enemy enemy : enemiesList) {
                for (Body engagedEnemy : engagedEnemies) {
                    if (engagedEnemy.equals(enemy.getBody())
                            && enemy.getTmpA().getAnim().isAnimationFinished(enemy.getTmpA().getTime())) {
                        enemy.disengage();
                    }
                }
            }
        }

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
//            if (myInputProcessor.getOutString().contains("S")) {
////				bodyPlayer.setLinearVelocity(new Vector2(0, 0));
//            }
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

        Rectangle heroRect = bpc.getRectAnim(tmpA, bodyPlayer);

        if (Gdx.input.isButtonJustPressed(0)
                && myInputProcessor.getOutString().isEmpty()
                && !fire
                && vel.y == 0) {
            heroShootSound.play(0.025f);
            tmpA = animEngMuscShoots;
            tmpA.resetTime();
            if (!flip) {
                bullet = new Bullet(psyX, bodyPlayer.getPosition().x + heroRect.width, bodyPlayer.getPosition().y, flip);
                bullets.add(bullet);
            } else {
                bullet = new Bullet(psyX, bodyPlayer.getPosition().x - heroRect.width, bodyPlayer.getPosition().y, flip);
                bullets.add(bullet);
            }

            fire = true;
        }

//        ArrayList<Bullet> bTmp = new ArrayList<>();
//        for (Bullet b: bullets) {
//            Body tB = b.update(delta);
//            if (tB != null) {
//                bodyToDelete.add(tB);
//                bTmp.add(b);
//            }
//        }
//        bullets.removeAll(bTmp);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (tmpA == animEngMuscWalks) {
            ((PolygonShape) bodyPlayer.getFixtureList().get(0).getShape()).setAsBox(heroRect.width/2, heroRect.height/2, new Vector2(0, 0.05f), 0);
        } else if (tmpA == animEngMuscShoots) {
            ((PolygonShape) bodyPlayer.getFixtureList().get(0).getShape()).setAsBox(heroRect.width/2, heroRect.height/2, new Vector2(0, 0.01f), 0);
        } else {
            ((PolygonShape) bodyPlayer.getFixtureList().get(0).getShape()).setAsBox(heroRect.width/2, heroRect.height/2);
        }
        batch.draw(tmpA.draw(), heroRect.x, heroRect.y, heroRect.width * bpc.PPM, heroRect.height * bpc.PPM);

        Array<Body> bodies = psyX.getBodies("coins");
        flagEng.setTime(delta);
        for (Array.ArrayIterator<Body> iterator = new Array.ArrayIterator<>(bodies); iterator.hasNext(); ) {
            Body body = iterator.next();
            Rectangle coin = bpc.getRectAnim(flagEng, body);
            ((PolygonShape) body.getFixtureList().get(0).getShape()).setAsBox(coin.width / 2, coin.height / 2);
            batch.draw(flagEng.draw(), coin.x, coin.y, coin.width * bpc.PPM, coin.height * bpc.PPM);
        }
        if (heroStats.getHitPoints() > 0.1f) {
            font.draw(batch, "HP: " + String.format("%.1f", heroStats.getHitPoints()), heroRect.x, heroRect.y + heroRect.height * bpc.PPM);
        }

        Array<Body> bulletArray = psyX.getBodies("bullet");
        for (Array.ArrayIterator<Body> iterator = new Array.ArrayIterator<>(bulletArray); iterator.hasNext(); ) {
            Body body = iterator.next();
            Rectangle bulletRect = bullet.getRectBullet(body);
            ((PolygonShape) body.getFixtureList().get(0).getShape()).setAsBox(bulletRect.width / 2, bulletRect.height / 2);
            if (body.getLinearVelocity().x > 0) {
                batch.draw(bullet.getTEXTURE(), bulletRect.x, bulletRect.y, bulletRect.width * bpc.PPM, bulletRect.height * bpc.PPM);
            } else {
                batch.draw(bullet.getTEXTURE(), bulletRect.x, bulletRect.y, bulletRect.width * bpc.PPM, bulletRect.height * bpc.PPM,
                        0, 0, bullet.getTEXTURE().getWidth(), bullet.getTEXTURE().getHeight(), true, false);
            }
        }

        Array<Body> towerArray = psyX.getBodies("Tower");
        for (Array.ArrayIterator<Body> iterator = new Array.ArrayIterator<>(towerArray); iterator.hasNext(); ) {
            Body body = iterator.next();
            Rectangle towerRect = tower.getRectTower(body);
            ((PolygonShape) body.getFixtureList().get(0).getShape()).setAsBox(towerRect.width / 2, towerRect.height / 2);
            batch.draw(tower.getTEXTURE(), towerRect.x, towerRect.y, towerRect.width * bpc.PPM, towerRect.height * bpc.PPM);
        }

        for (Enemy enemy : enemiesList) {
            Rectangle enemyRect = enemy.getRectEnemy();
            ((PolygonShape) enemy.getBody().getFixtureList().get(0).getShape()).setAsBox(enemyRect.width / 2, enemyRect.height / 2);
            enemy.getTmpA().setTime(Gdx.graphics.getDeltaTime());
            enemy.flipMe();
            batch.draw(enemy.getTmpA().draw(), enemyRect.x, enemyRect.y, enemyRect.width * bpc.PPM, enemyRect.height * bpc.PPM);
        }

        batch.end();

        psyX.step();

        for (Body body : bodyToDelete) {
            if (body.getUserData() != null && body.getUserData().equals("coins"))
                Gdx.graphics.setTitle(String.valueOf(++coinCount));
            if (body.getUserData() != null && body.getUserData().equals("enemy")) {
                for (Iterator<Enemy> iterator = enemiesList.iterator(); iterator.hasNext();) {
                    Enemy enemy = iterator.next();
                    if (enemy.getBody() == body) iterator.remove();
                }
            }
            psyX.removeBody(body);
        }
        bodyToDelete.clear();

        psyX.debugDraw(camera);

        if (coinCount == totalCoins) {
            dispose();
            Gdx.graphics.setTitle("WIN");
            coinCount = 0;
            game.setScreen(new WinScreen(game));
        }

        if (heroStats.getHitPoints() <= 0) {fatality = true;}

        if (fatality) {
            fatality = false;
            Gdx.graphics.setTitle("LOSE");
            coinCount = 0;
            MyContactListener.setDamaged(false);
            MyContactListener.setEngaged(false);
            dispose();
            game.setScreen(new LoseScreen(game));
        }
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
        heroShootSound.dispose();
        enemySwingSound.dispose();
        tmpA.dispose();
        flagEng.dispose();
        tower.dispose();
        map.dispose();
        mapRenderer.dispose();
        psyX.dispose();
        font.dispose();
        if (bullet != null) bullet.dispose();
//        enemy.dispose();
    }
}
