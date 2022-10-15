package com.kereotgdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
// technical line for git
public class KereotGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	MyAnim anim;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		anim = new MyAnim("blast.png",6, 8, 15, Animation.PlayMode.LOOP);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		anim.setTime(Gdx.graphics.getDeltaTime());

		float x = Gdx.input.getX() - anim.draw().getRegionWidth()/2;
		float y = Gdx.graphics.getHeight() - Gdx.input.getY() - anim.draw().getRegionHeight()/2;
		batch.begin();
//		batch.draw(img, x, y);

		batch.draw(anim.draw(), x, y);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		anim.dispose();
	}
}
