package net.cutebyte.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class Textures {
    public static Texture TEST = new Texture("badlogic.jpg");
    public static int KEY_TEST = 1;
    public static Texture TOUCH_BUTTON = new Texture("circleTouch.png");
    public static int KEY_TOUCH_BUTTON = 2;
    public static Texture COLLISION = new Texture("collision.png");
    public static int KEY_COLLISION = 3;
    public static Texture PLAYER = new Texture("player.png");
    public static int KEY_PLAYER = 4;

    public static Texture ENEMY = new Texture("enemy.png");
    public static int C_ENEMY = convert(0,255,0,255);
    public static int KEY_ENEMY = 5;

    public static Texture PORTAL = new Texture("portal.png");
    public static int C_PORTAL = convert(255,0,0,255);
    public static int KEY_PORTAL = 6;

    public static Texture BULLET = new Texture("bullet.png");
    public static int KEY_BULLET = 7;

    //level Tiles
    public static Texture WALL = new Texture("tiles/wall.png");
    public static int C_WALL = convert(255,0,0,255);
    public static int KEY_WALL = 100;
    public static Texture WALLBOAZI = new Texture("tiles/wall-boazi.png");
    public static int C_WALLBOAZI = convert(100,0,0,255);
    public static int KEY_WALLBOAZI = 101;
    public static Texture WALLUP = new Texture("tiles/wallUp.png");
    public static int C_WALLUP = convert(255,0,0,255);
    public static int KEY_WALLUP = 102;
    public static Texture FLOOR = new Texture("tiles/floor.png");
    public static int C_FLOOR = convert(0,0,255,255);
    public static int KEY_FLOOR = 103;
    public static Texture FLOORLAMIN = new Texture("tiles/floor-lamin.png");
    public static int C_FLOORLAMIN = convert(0,0,100,255);
    public static int KEY_FLOORLAMIN = 104;
    public static Texture FLOOR_BROKEN = new Texture("tiles/floor-brk.png");
    public static int C_FLOOR_BROKEN = convert(0,0,50,255);
    public static int KEY_FLOOR_BROKEN = 105;
    public static Texture WALL_TAP = new Texture("tiles/wall-taptap.png");
    public static int C_WALL_TAP = convert(150,0,0,255);
    public static int KEY_WALL_TAP = 106;
    public static Texture WALL_TAP_BROKEN = new Texture("tiles/wall-taptapbrk.png");
    public static int C_WALL_TAP_BROKEN = convert(50,0,0,255);
    public static int KEY_WALL_TAP_BROKEN = 107;

    public static Texture getTexture(int key) {
        if(key == KEY_WALL) return WALL;
        if(key == KEY_WALLBOAZI) return WALLBOAZI;
        if(key == KEY_WALLUP) return WALLUP;
        if(key == KEY_FLOOR) return FLOOR;
        if(key == KEY_FLOORLAMIN) return FLOORLAMIN;
        if(key == KEY_TEST) return TEST;
        if(key == KEY_TOUCH_BUTTON) return TOUCH_BUTTON;
        if(key == KEY_COLLISION) return COLLISION;
        if(key == KEY_PLAYER) return PLAYER;
        if(key == KEY_PORTAL) return PORTAL;
        if(key == KEY_ENEMY) return ENEMY;
        if(key == KEY_BULLET) return BULLET;
        if(key == KEY_FLOOR_BROKEN) return FLOOR_BROKEN;
        if(key == KEY_WALL_TAP) return WALL_TAP;
        if(key == KEY_WALL_TAP_BROKEN) return WALL_TAP_BROKEN;
        return null;
    }

    public static int convert(int r, int g, int b, int a) {
        return Color.rgba8888((float)r/255,(float)g/255,(float)b/255,(float)a/255);
    }
}
