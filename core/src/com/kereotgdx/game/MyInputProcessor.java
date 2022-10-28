package com.kereotgdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

public class MyInputProcessor implements InputProcessor {

    private String outString = "";
    @Getter
    private boolean aJustPressed;
    @Getter
    private boolean dJustPressed;
    @Getter
    private boolean wJustPressed;
    @Getter
    private boolean sJustPressed;
    @Getter
    private boolean spaceJustPressed;
    @Getter
    @Setter
    private boolean movementKeyJustPressed;

    public String getOutString() {
        return outString;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!Arrays.asList(29, 51, 47, 32, 62)
                .contains(keycode)) return false; // A, W, S, D, Space
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
        if (!Arrays.asList(29, 51, 47, 32, 62)
                .contains(keycode)) return false; // A, W, S, D, Space
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
            aJustPressed = false;
            dJustPressed = false;
            wJustPressed = false;
            sJustPressed = true;
            spaceJustPressed = false;
            movementKeyJustPressed = true;
            return true;
        }
        if (outString.contains(Input.Keys.toString(keycode))) {
            outString = outString.replace(Input.Keys.toString(keycode), "");
            if (keycode == 29) {
                aJustPressed = true;
                dJustPressed = false;
                wJustPressed = false;
                sJustPressed = false;
                spaceJustPressed = false;
                movementKeyJustPressed = true;
            }
            if (keycode == 51) {
                aJustPressed = false;
                dJustPressed = false;
                wJustPressed = true;
                sJustPressed = false;
                spaceJustPressed = false;
                movementKeyJustPressed = true;
            }
            if (keycode == 47) {
                aJustPressed = false;
                dJustPressed = false;
                wJustPressed = false;
                sJustPressed = true;
                spaceJustPressed = false;
                movementKeyJustPressed = true;
            }
            if (keycode == 32) {
                aJustPressed = false;
                dJustPressed = true;
                wJustPressed = false;
                sJustPressed = false;
                spaceJustPressed = false;
                movementKeyJustPressed = true;
            }
            if (keycode == 62) {
                aJustPressed = false;
                dJustPressed = false;
                wJustPressed = false;
                sJustPressed = false;
                spaceJustPressed = true;
                movementKeyJustPressed = true;
            }
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
