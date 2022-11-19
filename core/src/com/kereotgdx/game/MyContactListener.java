package com.kereotgdx.game;

import com.badlogic.gdx.physics.box2d.*;
import com.kereotgdx.game.screens.GameScreen;

public class MyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData().equals("Hero") && b.getUserData().equals("coins")) {
            GameScreen.bodyToDelete.add(b.getBody());
        }
        if (b.getUserData().equals("Hero") && a.getUserData().equals("coins")) {
            GameScreen.bodyToDelete.add(a.getBody());
        }

        if (a.getUserData().equals("Hero") && b.getUserData().equals("Lava")) {
            GameScreen.setFatality(true);
        }
        if (b.getUserData().equals("Hero") && a.getUserData().equals("Lava")) {
            GameScreen.setFatality(true);
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
