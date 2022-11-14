package com.kereotgdx.game;
// meow
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.Arrays;

public class MyInputProcessor implements InputProcessor {
    private String outString = "";

    public String getOutString() {
        return outString;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!Arrays.asList(29, 51, 47, 32, 19, 22, 21, 22, 62)
                .contains(keycode)) return false; // A, W, S, D, Up, Down, Left, Right, Space
        if (keycode == 47 && outString.contains("Space")) {
            outString += Input.Keys.toString(keycode);
            return true;
        }
        if (!outString.contains(Input.Keys.toString(keycode))) {
            outString += Input.Keys.toString(keycode);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!Arrays.asList(29, 51, 47, 32, 19, 22, 21, 22, 62)
                .contains(keycode)) return false; // A, W, S, D, Up, Down, Left, Right, Space
//        if (keycode == 62) {
//            outString = "";
//            return true;
//        }
        if (keycode == 47 && outString.contains("Space")) {
            outString = outString.replace(Input.Keys.toString(keycode), "");
            if (outString.contains("pace") && !outString.contains("Space")) {
                outString = outString.replace(Input.Keys.toString(keycode), "");
                outString = outString.replace("pace", "Space");
            }
            return true;
        }
        if (outString.contains(Input.Keys.toString(keycode))) {
            outString = outString.replace(Input.Keys.toString(keycode), "");
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
