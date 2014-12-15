package net.cutebyte.game.entities;

import java.util.concurrent.Callable;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class Action extends Entity {

    private Callable<Void> callable;

    public Action(Callable<Void> callable) {
        this.callable = callable;
    }

    public void call() {
        try {
            callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
