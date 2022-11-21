package com.kereotgdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import lombok.Getter;

public class Label {
    @Getter
    private BitmapFont font;

    public Label(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = Color.CYAN;
        parameter.genMipMaps = true;
        parameter.magFilter = Texture.TextureFilter.MipMapNearestNearest;
        parameter.characters = "-0123456789ЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮёйцукенгшщзхъфывапролджэячсмитьбюQWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm!.,:;";
        font = generator.generateFont(parameter);
    }

    public void draw(SpriteBatch batch, String text, float x, float y) {
        font.draw(batch, text, x, y + font.getLineHeight());
    }

    public void dispose() {
        font.dispose();
    }
}
