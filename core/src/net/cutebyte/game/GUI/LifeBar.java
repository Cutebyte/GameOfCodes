package net.cutebyte.game.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import net.cutebyte.game.entities.Movable;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class LifeBar {
    private Movable target;
    private ShapeRenderer shapeRenderer;

    public LifeBar(Movable target) {
        this.target = target;
        shapeRenderer = new ShapeRenderer();
    }

    public void render() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1f, 1f, 1f, 1f);
        shapeRenderer.rect(1, Gdx.graphics.getHeight()-11, Gdx.graphics.getWidth()/2, 10);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if(target.getHurtLock())
            shapeRenderer.setColor(1f, 0f, 1f, 1f);
        else
            shapeRenderer.setColor(1f, 0f, 0f, 1f);
        shapeRenderer.rect(2, Gdx.graphics.getHeight()-9, ((float)target.getHitPoints()/(float)target.getMaxHitPoints())*((Gdx.graphics.getWidth()/2)-2), 8);
        shapeRenderer.end();
    }
}
