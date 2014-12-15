package net.cutebyte.game.Tools;

import net.cutebyte.game.entities.Entity;
import net.cutebyte.game.entities.Movable;
import java.util.HashSet;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class CollisionPlane {
    private Entity tiles[][];
    private int width;
    private int height;
    private int tileSize;

    public CollisionPlane(int width, int height, int tileSize) {
        tiles = new Entity[width][height];
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
    }

    public void add(int x, int y, Entity entity) {
        tiles[x][y] = entity;
    }

    public HashSet<Entity> optiGet(Movable entity) {
        HashSet<Entity> collisions = new HashSet<Entity>();

        float entPosX = (entity.getPosition().x+entity.getSize().x/2)/tileSize;
        float entPosY = ((entity.getPosition().y+entity.getSize().y/2)/tileSize);

        addTile(collisions, (int)entPosX, (int)entPosY);

        if (entPosX%1 > 0.6) {
            addTile(collisions,(int)entPosX,(int)entPosY-1);
            addTile(collisions,(int)entPosX,(int)entPosY+1);
            addTile(collisions,(int)entPosX+1,(int)entPosY-1);
            addTile(collisions,(int)entPosX+1,(int)entPosY+1);
        } else if (entPosX%1 < 0.4) {
            addTile(collisions,(int)entPosX,(int)entPosY-1);
            addTile(collisions,(int)entPosX,(int)entPosY+1);
            addTile(collisions,(int)entPosX-1,(int)entPosY-1);
            addTile(collisions,(int)entPosX-1,(int)entPosY+1);
        } else {
            addTile(collisions,(int)entPosX,(int)entPosY-1);
            addTile(collisions,(int)entPosX,(int)entPosY+1);
        }

        if (entPosY%1 > 0.6) {
            addTile(collisions,(int)entPosX-1,(int)entPosY);
            addTile(collisions,(int)entPosX+1,(int)entPosY);
            addTile(collisions,(int)entPosX-1,(int)entPosY+1);
            addTile(collisions,(int)entPosX+1,(int)entPosY+1);
        } else if (entPosY%1 < 0.4) {
            addTile(collisions,(int)entPosX-1,(int)entPosY);
            addTile(collisions,(int)entPosX+1,(int)entPosY);
            addTile(collisions,(int)entPosX-1,(int)entPosY-1);
            addTile(collisions,(int)entPosX+1,(int)entPosY-1);
        } else {
            addTile(collisions,(int)entPosX-1,(int)entPosY);
            addTile(collisions,(int)entPosX+1,(int)entPosY);
        }

        return collisions;
    }

    private void addTile(HashSet<Entity> set, int x, int y) {
        if (exist(x,y))
            set.add(tiles[x][y]);
    }

    public void reset(int width, int height) {
        tiles = new Entity[width][height];
        this.width = width;
        this.height = height;
    }

    public void addAll(CollisionPlane collisionPlane) {
        for (int i = 0; i<width; i++) {
            for (int j = 0; j<height; j++) {
                tiles[i][j] = collisionPlane.getTile(i,j);
            }
        }
    }

    public Entity getTile(int x, int y) {
        return tiles[x][y];
    }

    private boolean exist(int x, int y) {
        if (x>=0 && x<width && y>=0 && y<height)
            if (tiles[x][y] != null)
                return true;
        return false;
    }
}
