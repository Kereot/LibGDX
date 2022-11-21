package com.kereotgdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.kereotgdx.game.Label;


public class LoseScreen implements Screen {
    Game game;
    Texture texture;
    SpriteBatch batch;
    Label label;

    String gameOverText;
    GlyphLayout glyphLayout;
    public LoseScreen(Game game) {
        this.game = game;
        texture = new Texture("screens/lose.jpg");
//        start = new Texture("screens/start.png");
//        x = Gdx.graphics.getWidth()/2 - start.getWidth()/2;
//        rectangle = new Rectangle(x, 0, start.getWidth(), start.getHeight());
        batch = new SpriteBatch();
        label = new Label(45);
        gameOverText = "Game over";
        glyphLayout = new GlyphLayout(label.getFont(), gameOverText);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(texture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        label.draw(batch, gameOverText, Gdx.graphics.getWidth()/2 - glyphLayout.width/2, Gdx.graphics.getHeight()/2 - glyphLayout.height/2);
//        batch.draw(start, x, 0);
        batch.end();

        if (Gdx.input.isTouched()) {
//            if (rectangle.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
//                dispose();
            Gdx.graphics.setTitle("");
            game.setScreen(new GameScreen(game));
//            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
//        this.start.dispose();
        this.texture.dispose();
        this.batch.dispose();
        this.label.dispose();
    }
}