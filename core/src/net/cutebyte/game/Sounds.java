package net.cutebyte.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class Sounds {

    private static Sound fire;
    private static Sound hurt;
    private static Music music;

    public static void init() {
        fire = Gdx.audio.newSound(Gdx.files.internal("data/fire.wav"));
        hurt = Gdx.audio.newSound(Gdx.files.internal("data/hurt.wav"));
        music = Gdx.audio.newMusic(Gdx.files.internal("data/theme.ogg"));
    }

    public static void dispose() {
        fire.dispose();
        hurt.dispose();
        music.dispose();
    }

    public static void playFire() {
        long id = fire.play(0.7f);
        fire.setPitch(id, (float)(0.5f+Math.random()));
    }

    public static void playHurt() {
        long id = hurt.play();
        hurt.setPitch(id, (float)(0.5f+Math.random()));
    }

    public static void playMusic() {
        music.setLooping(true);
        music.play();
    }
}
