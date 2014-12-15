package net.cutebyte.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.cutebyte.game.GUI.Control;
import net.cutebyte.game.entities.Entity;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class Renderer {
    private ArrayList<Entity> renderList;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private Entity cameraFocus;
    private Control control;

    public Renderer() {
        renderList = new ArrayList<Entity>();
        spriteBatch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    }

    public void connectControl(Control control) {
        this.control = control;
    }

    public void setList(ArrayList<Entity> list) {
        renderList.clear();
        renderList.addAll(list);
    }

    public void setCameraFocus(Entity entity) {
        cameraFocus = entity;
    }

    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(cameraFocus != null) {
            follow();
        }

        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        float tint;
        Entity temp;
        spriteBatch.begin();
        for (Iterator<Entity> itr = renderList.iterator(); itr.hasNext(); ) {
            temp = itr.next();
            tint = (float)(256-getDistance(cameraFocus, temp))/256;
            if(tint>=0) {
                spriteBatch.setColor(tint, tint, tint, 1f);
                temp.render(spriteBatch);
                spriteBatch.setColor(Color.WHITE);
            }
        }
        spriteBatch.end();

        spriteBatch.begin();
        control.draw(spriteBatch,camera.position);
        spriteBatch.end();
    }

    private int getDistance(Entity source, Entity target) {
        return (int)Math.sqrt(Math.pow((source.getPosition().x+source.getSize().x/2)-(target.getPosition().x+target.getSize().x/2), 2)
                + Math.pow((source.getPosition().y+source.getSize().y/2)-(target.getPosition().y+target.getSize().y/2), 2));
    }

    public void follow()
    {
        camera.position.x += ((cameraFocus.getPosition().x+cameraFocus.getSize().x/2) - camera.position.x)/20;
        camera.position.y += ((cameraFocus.getPosition().y+cameraFocus.getSize().y/2) - camera.position.y)/20;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
