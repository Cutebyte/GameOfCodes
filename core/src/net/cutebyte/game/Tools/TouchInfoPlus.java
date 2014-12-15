package net.cutebyte.game.Tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.cutebyte.game.GUI.TouchButton;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class TouchInfoPlus {
    private Vector2 touchPosition;
    private boolean touched = false;
    private TouchButton button;

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public void setTouchPosition(Vector2 position) {
        touchPosition = position;
    }

    public Vector2 getTouchPosition() {
        return touchPosition;
    }

    public void setButton(TouchButton button) {
        this.button = button;
    }

    public void updateButton(Vector3 touchPos, Vector3 offset) {
        button.action(touchPos, offset);
    }

    public boolean isBound() {
        return button != null;
    }

    public void resetButton() {
        button = null;
    }
}
