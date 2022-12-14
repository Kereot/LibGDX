package com.kereotgdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
// technical line for git
public class MyAnim {
    Texture img;
    Animation<TextureRegion> anim;
    private float time;

    public MyAnim(String name, int row, int col, float fps, Animation.PlayMode playMode) {
        time = 0;
        img = new Texture(name);
        TextureRegion reg1 = new TextureRegion(img);
        TextureRegion[][] regions = reg1.split(img.getWidth()/col, img.getHeight()/row);
        TextureRegion[] tmp = new TextureRegion[regions.length*regions[0].length];
        int cnt = 0;
        for (int i = 0; i < regions.length; i++) {
            for (int j = 0; j < regions[0].length; j++) {
                tmp[cnt++] = regions[i][j];
            }
        }
        anim = new Animation<>(1/fps, tmp);
        anim.setPlayMode(playMode);
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

    public void dispose() {
        this.img.dispose();
    }
}
