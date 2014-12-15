package net.cutebyte.game.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class Tile extends Entity {

    public Tile(Vector2 position, int size, int textureId) {
        this.position = position;
        this.size = new Vector2(size,size);
        this.textureId = textureId;
        this.solid = true;
        tint = -1;
    }

    public Tile(Vector2 position, int size, int textureId, boolean solid, boolean visibility, Action action) {
        this.position = position;
        this.size = new Vector2(size,size);
        this.textureId = textureId;
        this.solid = solid;
        this.visibility = visibility;
        this.interaction = true;
        this.action = action;
        tint = -1;
    }
}
