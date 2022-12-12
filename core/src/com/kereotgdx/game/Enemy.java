package com.kereotgdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;;
import lombok.Getter;
import lombok.Setter;

public class Enemy {
    @Getter
    private Body body;
    private BodyPartCreator bpc = new BodyPartCreator();
    private final MyAtlasAnim animFraKnightWalks;
    private final MyAtlasAnim animFraKnightShoots;
    private final float MAX_VELOCITY = 0.5f;
    @Getter
    private MyAtlasAnim tmpA;
    private final int SIR = 1;
    private final int SFPS = 12;
    @Setter
    @Getter
    private boolean flip;

    public Enemy(PsyX psyX, float x, float y, float w, float h) {
        body = bpc.addEnemy(psyX, x, y, w, h);
        body.setLinearVelocity(new Vector2(-MAX_VELOCITY, 0));

        animFraKnightShoots = new MyAtlasAnim("FRA_KNIGHT.atlas", "FRA_KNIGHT_FIRE_RIGHT",SIR, 20, SFPS, Animation.PlayMode.NORMAL);
        animFraKnightWalks = new MyAtlasAnim("FRA_KNIGHT.atlas", "FRA_KNIGHT_WALK_RIGHT", SIR, 18, SFPS, Animation.PlayMode.LOOP);
        tmpA = animFraKnightWalks;
    }

    public void engage() {
        tmpA = animFraKnightShoots;
    }

    public void disengage() {
        tmpA = animFraKnightWalks;
    }

    public void flipMe() {
        if (!tmpA.draw().isFlipX() && !flip) {
            tmpA.draw().flip(true, false);
        } else if (tmpA.draw().isFlipX() && flip) {
            tmpA.draw().flip(true, false);
        }
        if (body.getLinearVelocity().x > -MAX_VELOCITY && !flip) {
            body.setLinearVelocity(new Vector2(MAX_VELOCITY, 0));
            flip = true;
        } else if (body.getLinearVelocity().x < MAX_VELOCITY && flip) {
            body.setLinearVelocity(new Vector2(-MAX_VELOCITY, 0));
            flip = false;
        }
    }

    public Rectangle getRectEnemy() {
        TextureRegion tr = tmpA.anim.getKeyFrame(tmpA.getTime());
        float cx = body.getPosition().x * bpc.PPM - tr.getRegionWidth() / 2;
        float cy = body.getPosition().y * bpc.PPM - tr.getRegionHeight() / 2;
        float cw = tr.getRegionWidth() / bpc.PPM;
        float ch = tr.getRegionHeight() / bpc.PPM;
        return new Rectangle(cx, cy, cw, ch);
    }

    public void dispose() {
        animFraKnightShoots.dispose();
        animFraKnightWalks.dispose();
        tmpA.dispose();
    }


}
