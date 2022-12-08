package com.kereotgdx.game;

public class Types {
    public static final short HERO = 0x1;
    public static final short COIN = 0x2;
    public static final short DAMAGE = 0x4;
    public static final short BULLET = 0x8;
    public static final short TOWER = 0x16;
    public static final short ENEMY = 0x32;
    public static final short SCENERY = 0x64;
    public static final short STOP = 0x128;


    public static final short MASK_HERO = COIN | ENEMY | TOWER | DAMAGE | SCENERY;
    public static final short MASK_ENEMY = HERO | BULLET | STOP;
    public static final short MASK_BULLET = ENEMY | TOWER | DAMAGE | SCENERY;
    public static final short MASK_COIN = HERO | SCENERY;
    public static final short MASK_DAMAGE = HERO | BULLET;
    public static final short MASK_TOWER = HERO | BULLET;
    public static final short MASK_STOP = ENEMY;
    public static final short MASK_SCENERY = -1;
}
