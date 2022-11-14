package com.kereotgdx.game;
// meow
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class KereotGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
//	private Texture img;
//	private MyAnim anim;
	private MyAtlasAnim animEngMuscShoots;
	private MyAtlasAnim animEngMuscStands;
	private MyAtlasAnim animEngMuscWalks;
	private MyAtlasAnim tmpA;
	private Music music;
	private Sound sound;
	private MyInputProcessor myInputProcessor;
	private float x, y;
	private final int SIR = 1; // Standard image row
	private final int SIC = 14; // Standard image column
	private final int SFPS = 15;
	private boolean flip;
	private boolean fire;
	
	@Override
	public void create () {
		myInputProcessor = new MyInputProcessor();
		Gdx.input.setInputProcessor(myInputProcessor);

		batch = new SpriteBatch();

		music = Gdx.audio.newMusic(Gdx.files.internal("cancionero175.mp3"));
		music.setVolume(0.025f);
		music.setLooping(true);
		music.play();

		sound = Gdx.audio.newSound(Gdx.files.internal("land_musket1.wav"));

//		img = new Texture("badlogic.jpg");
//		anim = new MyAnim("blast.png",6, 8, 15, Animation.PlayMode.LOOP);
		animEngMuscShoots = new MyAtlasAnim("ENG_MUSC.atlas","ENG_MUSC_FIRE_RIGHT", SIR, SIC, SFPS, Animation.PlayMode.NORMAL);
		animEngMuscStands = new MyAtlasAnim("ENG_MUSC.atlas","ENG_MUSC_STAND", SIR, SIC, SFPS, Animation.PlayMode.LOOP);
		animEngMuscWalks = new MyAtlasAnim("ENG_MUSC.atlas", "ENG_MUSC_WALK_RIGHT", SIR, SIC, SFPS, Animation.PlayMode.LOOP);
		}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);

		if (fire) {
			tmpA = animEngMuscShoots;
			if (tmpA.anim.isAnimationFinished(tmpA.getTime())) {
				fire = false;
			}
		} else {
			tmpA = animEngMuscStands;
		}

//		x = Gdx.input.getX() - anim.draw().getRegionWidth()/2;
//		y = Gdx.graphics.getHeight() - Gdx.input.getY() - anim.draw().getRegionHeight()/2;
		if (!fire) {
			if (myInputProcessor.getOutString().contains("A") || myInputProcessor.getOutString().contains("Left")) {
				x--;
				tmpA = animEngMuscWalks;
				flip = true;
			}
			if (myInputProcessor.getOutString().contains("D") || myInputProcessor.getOutString().contains("Right")) {
				x++;
				tmpA = animEngMuscWalks;
				flip = false;
			}
			if (myInputProcessor.getOutString().contains("W") || myInputProcessor.getOutString().contains("Up")) {
				y++;
			}
			if (myInputProcessor.getOutString().contains("S") || myInputProcessor.getOutString().contains("Down")) {
				y--;
			}
			if (myInputProcessor.getOutString().contains("Space")) {
				x = Gdx.graphics.getWidth() / 2;
				y = Gdx.graphics.getHeight() / 2;
			}
		}
//		System.out.println(myInputProcessor.getOutString());

//		if (!tmpA.draw().isFlipX() && flip) {
//			tmpA.draw().flip(true, false);
//		} else if (tmpA.draw().isFlipX() && !flip) {
//			tmpA.draw().flip(true, false);
//		} else {
			tmpA.setTime(Gdx.graphics.getDeltaTime());
//		}

		if (Gdx.input.isButtonJustPressed(0)
				&& myInputProcessor.getOutString().isEmpty()
				&& !fire) {
			sound.play(0.025f);
			tmpA = animEngMuscShoots;
			tmpA.resetTime();
			fire = true;
		}

		batch.begin();

		if ((!flip && tmpA != animEngMuscStands) || (tmpA == animEngMuscStands && flip)) { // doh, STAND image is facing the wrong way...
			batch.draw(tmpA.draw(), x, y);
		} else {
			batch.draw(tmpA.draw(), x + tmpA.draw().getRegionWidth(), y,
					-tmpA.draw().getRegionWidth(), tmpA.draw().getRegionHeight());
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
//		img.dispose();
//		anim.dispose();
		animEngMuscShoots.dispose();
		animEngMuscWalks.dispose();
		animEngMuscStands.dispose();
		music.dispose();
		sound.dispose();
		tmpA.dispose();
	}
}
