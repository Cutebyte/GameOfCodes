package net.cutebyte.game.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.cutebyte.game.Tools.TouchInfoPlus;
import net.cutebyte.game.entities.Movable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class Control implements InputProcessor {

    private Movable controlled;
    private Map<Integer,TouchButton> buttonList;
    private Map<Integer,TouchInfoPlus> touchPoints;
    private OrthographicCamera camera;

    private LifeBar lifeBar;

    public Control(OrthographicCamera camera) {
        this.camera = camera;
        touchPoints = new HashMap<Integer, TouchInfoPlus>();

        for(int i = 0; i<5; i++) {
            touchPoints.put(i, new TouchInfoPlus());
        }

        buttonList = new HashMap<Integer,TouchButton>();

        buttonList.put(0, new Analog(new Vector2(0,0), 200, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                controlled.applyForce(((Analog)buttonList.get(0)).getValue());
                return null;
            }
        }));
        buttonList.get(0).disable();

        buttonList.put(1,new TouchButton(new Vector2(60,120),40,new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                controlled.moveUp();
                return null;
            }
        })); //UP
        buttonList.put(2,new TouchButton(new Vector2(60,0),40,new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                controlled.moveDown();
                return null;
            }
        })); //DOWN
        buttonList.put(3,new TouchButton(new Vector2(0,60),40,new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                controlled.moveLeft();
                return null;
            }
        })); //LEFT
        buttonList.put(4,new TouchButton(new Vector2(120,60),40,new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                controlled.moveRight();
                return null;
            }
        })); //RIGHT
        buttonList.put(5,new TouchButton(new Vector2(Gdx.graphics.getWidth()-140,0),40,new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                return null;
            }
        })); //ACTION1 - test
        buttonList.put(6,new TouchButton(new Vector2(Gdx.graphics.getWidth()-80,60),40,new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                //controlled.moveUp();
                return null;
            }
        })); //ACTION2 - test
        buttonList.put(666, new TouchButton(new Vector2(Gdx.graphics.getWidth()-80,Gdx.graphics.getHeight()-80), 40, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if (!buttonList.get(666).isLocked()) {
                    switchToAnalog(!buttonList.get(0).isEnabled());
                    buttonList.get(666).lock();
                }
                return null;
            }
        }));
    }

    public void setControlled(Movable movable) {
        this.controlled = movable;
        lifeBar = new LifeBar(this.controlled);
    }

    public Movable getControlled() {
        return controlled;
    }


    public void draw(SpriteBatch spriteBatch, Vector3 offset) {
        for(Iterator<Map.Entry<Integer, TouchButton>> itr = buttonList.entrySet().iterator(); itr.hasNext(); ) {
            TouchButton temp = itr.next().getValue();
            if(temp.isEnabled())
                temp.render(spriteBatch, offset);
        }
        spriteBatch.end();
        if(lifeBar != null) lifeBar.render();
        spriteBatch.begin();
    }

    public void update(Vector3 offset) {
        for(Iterator<Map.Entry<Integer, TouchButton>> itr = buttonList.entrySet().iterator(); itr.hasNext(); ) {
            TouchButton temp = itr.next().getValue();
            temp.reset();
        }
        buttonList.get(0).reset();
        for(int i = 0; i < 5; i++) {
            if(touchPoints.get(i).isTouched()) {
                Vector3 touchPos = new Vector3();
                touchPos.set(touchPoints.get(i).getTouchPosition().x, touchPoints.get(i).getTouchPosition().y, 0);
                camera.unproject(touchPos);

                if (!touchPoints.get(i).isBound())
                    for (Iterator<Map.Entry<Integer, TouchButton>> itr = buttonList.entrySet().iterator(); itr.hasNext(); ) {
                        TouchButton temp = itr.next().getValue();
                        if (temp.isEnabled())
                            if (Math.pow(touchPos.x - ((temp.getPosition().x + (offset.x - Gdx.graphics.getWidth() / 2)) + temp.getSize().x / 2), 2) +
                                    Math.pow(touchPos.y - ((temp.getPosition().y + (offset.y - Gdx.graphics.getHeight() / 2)) + temp.getSize().x / 2), 2)
                                    <= Math.pow(temp.getSize().x / 2, 2))
                            {
                                temp.action(touchPos, offset);
                                if (!temp.allowSlippingOff)
                                    touchPoints.get(i).setButton(temp);
                            }
                    }
                else
                    touchPoints.get(i).updateButton(touchPos, offset);
            }
            else
                touchPoints.get(i).resetButton();
        }
        for (Iterator<Map.Entry<Integer, TouchButton>> itr = buttonList.entrySet().iterator(); itr.hasNext(); ) {
            TouchButton temp = itr.next().getValue();
            if(!temp.isTouched())
                temp.unlock();
        }
    }

    public void switchToAnalog(boolean value) {
        if(value) {
            buttonList.get(0).enable();
            buttonList.get(1).disable();
            buttonList.get(2).disable();
            buttonList.get(3).disable();
            buttonList.get(4).disable();
        }
        else
        {
            buttonList.get(0).disable();
            buttonList.get(1).enable();
            buttonList.get(2).enable();
            buttonList.get(3).enable();
            buttonList.get(4).enable();
        }
    }

    public void bindButton(int button, Callable<Void> action) {
        buttonList.get(button).setAction(action);
    }

    public TouchButton getButton(int button) {
        return buttonList.get(button);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5) {
            touchPoints.get(pointer).setTouchPosition(new Vector2(screenX,screenY));
            touchPoints.get(pointer).setTouched(true);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5) {
            touchPoints.get(pointer).setTouchPosition(new Vector2(screenX,screenY));
            touchPoints.get(pointer).setTouched(false);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(pointer < 5) {
            touchPoints.get(pointer).setTouchPosition(new Vector2(screenX,screenY));
            touchPoints.get(pointer).setTouched(true);
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
