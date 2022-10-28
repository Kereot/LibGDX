package com.kereotgdx.game;

import com.badlogic.gdx.physics.box2d.*;

public class BodyPartCreator {


    public void create(
            PsyX psyX,
            BodyDef def,
            FixtureDef fDef,
            PolygonShape shape,
            int gravityScale,
            int type,
            float positionX,
            float positionY,
            float shapeX,
            float shapeY,
            float density,
            float friction,
            float restitution,
            String name
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
        def.position.set(positionX, positionY);
        shape.setAsBox(shapeX, shapeY);
        fDef.shape = shape;
        fDef.density = density;
        fDef.friction = friction;
        fDef.restitution = restitution;

        psyX.world.createBody(def).createFixture(fDef).setUserData(name);

        shape.dispose();
    }

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
        def.position.set(positionX, positionY);
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
        shape.setAsBox(shapeX, shapeY);
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
        def.position.set(positionX, positionY);
        shape.setAsBox(shapeX, shapeY);
        fDef.shape = shape;
        psyX.world.createBody(def).createFixture(fDef).setUserData(name);
        shape.dispose();
    }
}
