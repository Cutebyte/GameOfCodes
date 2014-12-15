package net.cutebyte.game.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.cutebyte.game.Textures;
import java.util.concurrent.Callable;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class Analog extends TouchButton {

    private Callable<Void> callable;
    private TouchButton knob; // @_@

    public Analog(Vector2 position, int r, Callable<Void> callable) {
        this.position = position;
        this.size = new Vector2(r,r);
        textureId = Textures.KEY_TOUCH_BUTTON;
        allowSlippingOff = false;
        enabled = true;

        this.callable = callable;

        knob = new TouchButton();
        knob.setSize(new Vector2(r/2,r/2));
        knob.setPosition(new Vector2((this.position.x/this.size.x)-knob.getSize().x/2,
                (this.position.y/this.size.y)-knob.getSize().y/2));
        knob.setTextureId(Textures.KEY_TOUCH_BUTTON);
    }

    @Override
    public void render(SpriteBatch spriteBatch, Vector3 offset) {
        spriteBatch.draw(Textures.getTexture(textureId), getPosition().x + (offset.x - Gdx.graphics.getWidth() / 2),
                getPosition().y + (offset.y - Gdx.graphics.getHeight() / 2), getSize().x, getSize().y);
        spriteBatch.draw(Textures.getTexture(knob.getTextureId()), knob.getPosition().x + (offset.x - Gdx.graphics.getWidth() / 2),
                knob.getPosition().y + (offset.y - Gdx.graphics.getHeight() / 2), knob.getSize().x, knob.getSize().y);
    }

    public void action(Vector3 touchPos, Vector3 offset) {
        touched = true;

        int x = (int)((touchPos.x-knob.getSize().x/2-offset.x)+Gdx.graphics.getWidth()/2);
        int y = (int)((touchPos.y-knob.getSize().y/2-offset.y)+Gdx.graphics.getHeight()/2);

        if (Math.pow(touchPos.x - ((getPosition().x+(offset.x-Gdx.graphics.getWidth()/2))+getSize().x/2), 2) +
                Math.pow(touchPos.y - ((getPosition().y+(offset.y-Gdx.graphics.getHeight()/2))+getSize().x/2), 2)
                <= Math.pow(getSize().x/3,2)) {
            knob.setPosition(x, y);
        }
        else {
            x = (int) (x - getPosition().x - knob.getSize().x/2);
            y = (int) (y - getPosition().y - knob.getSize().y/2);

            double l = Math.sqrt(x * x + y * y);

            x = (int) (x / l * getSize().x/3);
            y = (int) (y / l * getSize().y/3);

            knob.setPosition((int)(x+getPosition().x+knob.getSize().x/2), (int)(y+getPosition().y+knob.getSize().y/2));
        }

        try {
            callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vector2 getValue() {
        Vector2 result;

        result = new Vector2(((knob.getPosition().x +knob.getSize().x/2)-(getPosition().x +getSize().x/2))/64,
                ((knob.getPosition().y +knob.getSize().y/2)-(getPosition().y +getSize().y/2))/64);

        return result;
    }

    public void reset() {
        touched = false;
        knob.setPosition(new Vector2(this.position.x + knob.getSize().x / 2, this.position.y + knob.getSize().y / 2));
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }
}
