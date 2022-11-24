package com.kereotgdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.Getter;

public class Bullet {
    @Getter
    private final Body BODY;
    private final static float SPD = 10;
    private float time = 3f;
    private BodyPartCreator bpc = new BodyPartCreator();
    @Getter
    private final Texture TEXTURE = new Texture("arrow.png");

    public Bullet(PsyX psyX, float x, float y, boolean flip) {
        BODY = bpc.addBullet(psyX, x, y, this);
        if (!flip) {
            BODY.setLinearVelocity(new Vector2(SPD, 0));
        } else {
            BODY.setLinearVelocity(new Vector2(-SPD, 0));
        }
    }
    public Body update(float dTime) {
        time -= dTime;
        if (time <= 0) return BODY;
        return null;
    }

    public Rectangle getRectBullet(Body body) {
        float cx = body.getPosition().x * bpc.PPM - TEXTURE.getWidth() / 2;
        float cy = body.getPosition().y * bpc.PPM;
        float cw = TEXTURE.getWidth() / bpc.PPM / 2;
        float ch = TEXTURE.getHeight() / bpc.PPM / 2;
        return new Rectangle(cx, cy, cw, ch);
    }

    public void dispose() {
        TEXTURE.dispose();
    }
}
