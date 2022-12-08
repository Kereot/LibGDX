package com.kereotgdx.game;

import com.badlogic.gdx.physics.box2d.*;
import com.kereotgdx.game.screens.GameScreen;
import lombok.Getter;
import lombok.Setter;

public class MyContactListener implements ContactListener {
    @Getter
    @Setter
    private static boolean isDamaged;
    @Getter
    @Setter
    private static boolean isEngaged;
    @Getter
    private static Fixture damageObject;
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData().equals("Hero") && b.getUserData().equals("Lava")) {
            GameScreen.setFatality(true);
        }
        if (b.getUserData().equals("Hero") && a.getUserData().equals("Lava")) {
            GameScreen.setFatality(true);
        }

        if (a.getUserData().equals("Hero") && b.getUserData().equals("damage")) {
            isDamaged = true;
            damageObject = b;
        }
        if (b.getUserData().equals("Hero") && a.getUserData().equals("damage")) {
            isDamaged = true;
            damageObject = a;
        }

        if (a.getUserData().equals("bullet")
                && b.getUserData().equals("enemy")) {
            GameScreen.bodyToDelete.add(a.getBody());
            GameScreen.bodyToDelete.add(b.getBody());
        }
        if (b.getUserData().equals("bullet")
                && a.getUserData().equals("enemy")) {
            GameScreen.bodyToDelete.add(a.getBody());
            GameScreen.bodyToDelete.add(b.getBody());
        }

        if (a.getUserData().equals("bullet")
                && !b.getUserData().equals("Hero")
                && !b.getUserData().equals("coins")
                && !b.getUserData().equals("bullet")) {
            GameScreen.bodyToDelete.add(a.getBody());
        }
        if (b.getUserData().equals("bullet")
                && !a.getUserData().equals("Hero")
                && !a.getUserData().equals("coins")
                && !a.getUserData().equals("bullet")) {
            GameScreen.bodyToDelete.add(b.getBody());
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData().equals("Hero") && b.getUserData().equals("damage")) {
            isDamaged = false;
        }
        if (b.getUserData().equals("Hero") && a.getUserData().equals("damage")) {
            isDamaged = false;
        }

        if (a.getUserData().equals("Hero") && b.getUserData().equals("enemy")) {
            isEngaged = false;
            GameScreen.engagedEnemies.remove(b.getBody());
        }
        if (b.getUserData().equals("Hero") && a.getUserData().equals("enemy")) {
            isEngaged = false;
            GameScreen.engagedEnemies.remove(a.getBody());
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData().equals("Hero") && b.getUserData().equals("coins")) {
            GameScreen.bodyToDelete.add(b.getBody());
        }
        if (b.getUserData().equals("Hero") && a.getUserData().equals("coins")) {
            GameScreen.bodyToDelete.add(a.getBody());
        }

        if (a.getUserData().equals("Hero") && b.getUserData().equals("enemy")) {
            isEngaged = true;
            contact.setEnabled(false);
            GameScreen.engagedEnemies.add(b.getBody());
        }
        if (b.getUserData().equals("Hero") && a.getUserData().equals("enemy")) {
            isEngaged = true;
            contact.setEnabled(false);
            GameScreen.engagedEnemies.add(a.getBody());
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
