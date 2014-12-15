package net.cutebyte.game.GUI;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.cutebyte.game.Textures;
import net.cutebyte.game.entities.Entity;
import java.util.concurrent.Callable;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class TouchButton extends Entity {

    private Callable<Void> callable;
    protected boolean allowSlippingOff;
    protected boolean enabled;
    protected boolean lock;
    protected boolean touched;

    public TouchButton() {
    }

    public TouchButton(Vector2 position, int r, Callable<Void> callable) {
        this.position = position;
        size = new Vector2(r*2,r*2);
        textureId = Textures.KEY_TOUCH_BUTTON;
        allowSlippingOff = true;
        enabled = true;
        lock = false;

        this.callable = callable;
    }

    public void setAction(Callable<Void> action) {
        callable = action;
    }

    public void action(Vector3 touchPos, Vector3 offset) {
        touched = true;
        try {
            callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        touched = false;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void lock() {
        lock = true;
    }

    public void unlock() {
        lock = false;
    }

    public boolean isLocked() {
        return lock;
    }

    public boolean isTouched() {
        return touched;
    }
}
