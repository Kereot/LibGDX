package com.kereotgdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.Getter;

public class Tower {
    @Getter
    private final Body BODY;
    private final BodyPartCreator bpc = new BodyPartCreator();
    @Getter
    private final Texture TEXTURE = new Texture("tower.png");

    public Tower (PsyX psyx, float x, float y) {
        BODY = bpc.createTower(psyx, x, y, this);
    }

    public Rectangle getRectTower(Body body) {
        float cx = body.getPosition().x * bpc.PPM - TEXTURE.getWidth()/4f;
        float cy = body.getPosition().y * bpc.PPM - TEXTURE.getWidth()/4f;
        float cw = TEXTURE.getWidth() / bpc.PPM / 2;
        float ch = TEXTURE.getHeight() / bpc.PPM / 2;
        return new Rectangle(cx, cy, cw, ch);
    }

    public void dispose() {
        TEXTURE.dispose();
    }
}
