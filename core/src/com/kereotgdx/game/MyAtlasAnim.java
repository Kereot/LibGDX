package com.kereotgdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;

public class MyAtlasAnim {
    TextureAtlas atlas;
    @Getter
    Animation<TextureRegion> anim;
    private float time;

    public MyAtlasAnim(String atlas, String name, int row, int col, float fps, Animation.PlayMode playMode) {
        time = 0;
        this.atlas = new TextureAtlas(atlas);
        TextureRegion reg1 = new TextureRegion(this.atlas.findRegion(name));
        TextureRegion[][] regions = reg1.split(reg1.getRegionWidth()/col, reg1.getRegionHeight()/row);
        TextureRegion[] tmp = new TextureRegion[regions.length*regions[0].length];
        int cnt = 0;
        for (int i = 0; i < regions.length; i++) {
            for (int j = 0; j < regions[0].length; j++) {
                tmp[cnt++] = regions[i][j];
            }
        }
        this.anim = new Animation<>(1/fps, tmp);
        this.anim.setPlayMode(playMode);
    }

    public TextureRegion draw() {
        return anim.getKeyFrame(time);
    }

    public void setTime(float time) {
        this.time += time;
    }

    public float getTime() {
        return time;
    }

    public void resetTime () {
        this.time = 0f;
    }

    public void dispose() {
        this.atlas.dispose();
    }
}
