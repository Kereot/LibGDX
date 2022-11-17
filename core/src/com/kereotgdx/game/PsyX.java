package com.kereotgdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class PsyX {
    public final MyContactListener contactListener;
    final World world;
    private final Box2DDebugRenderer debugRenderer;

    public PsyX() {
        world = new World(new Vector2(0, -9.81f), true);
        debugRenderer = new Box2DDebugRenderer();

        contactListener = new MyContactListener();
        world.setContactListener(contactListener);
    }

    public void removeBody(Body body) {
        world.destroyBody(body);
    }

    public Array<Body> getBodies(String name) {
        Array<Body> tmp = new Array<>();
        world.getBodies(tmp);

//        Iterator<Body> it = tmp.iterator();
//        while (it.hasNext()) {
//            Body body = it.next();
//            if (!body.getUserData().equals(name)) {
//                it.remove();
//            }
//        }
        for (int i = tmp.size - 1; i >= 0; i--) {
            if (!tmp.get(i).getUserData().equals(name)) {
                tmp.removeIndex(i);
            }
        }
        return tmp;
    }

    public void debugDraw(OrthographicCamera camera) {
        debugRenderer.render(world, camera.combined);
    }

    public void step() {
        world.step(1/60f, 3, 3);
    }

    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
    }
}
