package com.kereotgdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;

public class WinScreen implements Screen {
    Game game;
    Texture texture;
    SpriteBatch batch;
    int x;
    Rectangle rectangle;
    public WinScreen(Game game) {
        this.game = game;
        texture = new Texture("screens/win.jpg");
//        start = new Texture("screens/start.png");
//        x = Gdx.graphics.getWidth()/2 - start.getWidth()/2;
//        rectangle = new Rectangle(x, 0, start.getWidth(), start.getHeight());
        batch = new SpriteBatch();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(texture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
    }
}