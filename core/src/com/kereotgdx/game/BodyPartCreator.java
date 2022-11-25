package com.kereotgdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

public class BodyPartCreator {
    public final float PPM = 50;

    public Body createBody (
            PsyX psyX,
            BodyDef def,
            int gravityScale,
            int type,
            float positionX,
            float positionY
    ) {
        def.gravityScale = gravityScale;
        switch (type) {
            case 1:
                def.type = BodyDef.BodyType.StaticBody;
                break;
            case 2:
                def.type = BodyDef.BodyType.DynamicBody;
                break;
            case 3:
                def.type = BodyDef.BodyType.KinematicBody;
                break;
        }
        def.position.set(positionX/PPM, positionY/PPM);
        return psyX.world.createBody(def);
    }

    public void createFixture (
            FixtureDef fDef,
            PolygonShape shape,
            Body body,
            float shapeX,
            float shapeY,
            float density,
            float friction,
            float restitution,
            String name
    ) {
        shape.setAsBox(shapeX/PPM, shapeY/PPM);
        fDef.shape = shape;
        fDef.density = density;
        fDef.friction = friction;
        fDef.restitution = restitution;

        body.createFixture(fDef).setUserData(name);

        shape.dispose();
    }

    public void createObstacle(
            PsyX psyX,
            BodyDef def,
            FixtureDef fDef,
            PolygonShape shape,
            int type,
            float positionX,
            float positionY,
            float shapeX,
            float shapeY,
            String name
    ) {
        switch (type) {
            case 1:
                def.type = BodyDef.BodyType.StaticBody;
                break;
            case 2:
                def.type = BodyDef.BodyType.DynamicBody;
                break;
            case 3:
                def.type = BodyDef.BodyType.KinematicBody;
                break;
        }
        def.position.set(positionX/PPM, positionY/PPM);
        shape.setAsBox(shapeX/PPM, shapeY/PPM);
        fDef.shape = shape;
        Body body = psyX.world.createBody(def);
        body.setUserData(name);
        body.createFixture(fDef).setUserData(name);
        shape.dispose();
    }

    public Fixture createDamageSensor(
            PsyX psyX,
            BodyDef def,
            FixtureDef fDef,
            PolygonShape shape,
            float positionX,
            float positionY,
            float shapeX,
            float shapeY,
            String name
    ) {
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(positionX/PPM, positionY/PPM);
        shape.setAsBox(shapeX/PPM, shapeY/PPM);
        fDef.shape = shape;
        Body body = psyX.world.createBody(def);
        body.setUserData(name);
        body.createFixture(fDef).setUserData(name);
        body.getFixtureList().get(0).setSensor(true);

        shape.dispose();
        return body.getFixtureList().get(0);
    }

    public Body addBullet(PsyX psyx, float x, float y, Bullet bullet) {
        BodyDef def = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x, y);
        polygonShape.setAsBox(bullet.getTEXTURE().getWidth()/PPM, bullet.getTEXTURE().getHeight()/PPM);
        fDef.shape = polygonShape;
        String name = "bullet";
        Body body = psyx.world.createBody(def);
        body.setGravityScale(0);
        body.setBullet(true);
        body.setUserData(name);
        body.createFixture(fDef).setUserData(name);
        body.getFixtureList().get(0).setSensor(true);
        polygonShape.dispose();
        return body;
    }

    public Body addEnemy(PsyX psyx, float x, float y, float w, float h) {
        BodyDef def = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x/PPM, y/PPM);
        polygonShape.setAsBox(w/PPM, h/PPM);
        fDef.shape = polygonShape;
        fDef.density = 0.010f;
        fDef.friction = 0f;
        fDef.restitution = 0f;
        String name = "enemy";
        Body body = psyx.world.createBody(def);
        body.setGravityScale(1);
        body.setUserData(name);
        body.createFixture(fDef).setUserData(name);
        polygonShape.dispose();
        return body;
    }
    
    public Rectangle getRectAnim(MyAnim anim, Body body) {
        TextureRegion tr = anim.anim.getKeyFrame(anim.getTime());
        float cx = body.getPosition().x * PPM - tr.getRegionWidth() / 2;
        float cy = body.getPosition().y * PPM - tr.getRegionHeight() / 2;
        float cw = tr.getRegionWidth() / PPM;
        float ch = tr.getRegionHeight() / PPM;
        return new Rectangle(cx, cy, cw, ch);
    }

    public Rectangle getRectAnim(MyAtlasAnim anim, Body body) {
        TextureRegion tr = anim.anim.getKeyFrame(anim.getTime());
        float cx = body.getPosition().x * PPM - tr.getRegionWidth() / 2;
        float cy = body.getPosition().y * PPM - tr.getRegionHeight() / 2;
        float cw = tr.getRegionWidth() / PPM;
        float ch = tr.getRegionHeight() / PPM;
        return new Rectangle(cx, cy, cw, ch);
    }
}
