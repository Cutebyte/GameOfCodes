package net.cutebyte.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.cutebyte.game.Textures;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class Entity {
    protected Vector2 position;
    protected Vector2 size;
    protected int textureId;

    protected boolean interaction;
    protected Action action;
    protected boolean solid;
    protected boolean visibility;

    protected int tint;

    public Entity() {
        position = new Vector2(200,200);
        size = new Vector2(64,64);
        tint = -1;
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    public void setPosition(int x, int y) {
        position = new Vector2(x,y);
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }

    public int getTextureId() {
        return textureId;
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(Textures.getTexture(textureId), getPosition().x, getPosition().y, getSize().x, getSize().y);
    }

    public void render(SpriteBatch spriteBatch, Vector3 offset) {
        spriteBatch.draw(Textures.getTexture(textureId), getPosition().x+(offset.x-Gdx.graphics.getWidth()/2),
                getPosition().y+(offset.y-Gdx.graphics.getHeight()/2), getSize().x, getSize().y);
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean canInteract() {
        return interaction;
    }

    public void callAction() {
        action.call();
    }

    public boolean isVisible() {
        return visibility;
    }

    public void setTint(int tint) {
        this.tint = tint;
    }
}
