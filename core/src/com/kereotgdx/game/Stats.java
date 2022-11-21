package com.kereotgdx.game;

import lombok.Getter;

public class Stats {
    @Getter
    private float hitPoints;

    public Stats(float maxLife) {
        hitPoints = maxLife;
    }

    public void getHit(float damage) {
        hitPoints -= damage;
    }
}
