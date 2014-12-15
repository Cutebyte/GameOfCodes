package net.cutebyte.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.cutebyte.game.Tools.CollisionPlane;
import net.cutebyte.game.entities.Action;
import net.cutebyte.game.entities.Enemy;
import net.cutebyte.game.entities.Entity;
import net.cutebyte.game.entities.Movable;
import net.cutebyte.game.entities.Tile;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class Level {

    private Tile[][][] levelTiles;  // 3 layers
                                    // 0 - collision mask
                                    // 1 - 1st decoration layer
                                    // 2 - 2nd decoration layer
    private ArrayList<Movable> enemies;
    private int tileSize;
    private Vector2 levelSize;
    private int levelId;
    private Vector2 playerPosition;

    private Callable<Void> callable;

    public Level() {

    }

    public void loadLevel(int level) {
        System.out.println("Choosen level: " + level);
        tileSize = 64;
        levelId = level;
        loadTiles();
    }

    private void loadTiles() {
        Pixmap collisionLayer = new Pixmap(Gdx.files.internal("level"+levelId+"/level-collision.png"));
        Pixmap decorationLayer1 = new Pixmap(Gdx.files.internal("level"+levelId+"/level-decor1.png"));
        Pixmap decorationLayer2 = new Pixmap(Gdx.files.internal("level"+levelId+"/level-decor2.png"));
        enemies = new ArrayList<Movable>();
        levelSize = new Vector2(collisionLayer.getWidth(),collisionLayer.getHeight());
        levelTiles = new Tile[(int) levelSize.x][(int) levelSize.y][3];
        for(int i = 0; i<levelSize.x; i++) {
            for(int j = 0; j<levelSize.y; j++) {
                levelTiles[i][j][0] = loadCollisionTile(collisionLayer.getPixel(i, (int) (levelSize.y-1-j)),
                        new Vector2(i * tileSize, j * tileSize), tileSize);

                levelTiles[i][j][1] = loadDecor1Tile(decorationLayer1.getPixel(i, (int) (levelSize.y-1-j)),
                        new Vector2(i * tileSize, j * tileSize), tileSize);

                levelTiles[i][j][2] = loadDecor2Tile(decorationLayer2.getPixel(i, (int) (levelSize.y - 1 - j)),
                        new Vector2(i * tileSize, (j * tileSize) + tileSize/2), tileSize);
            }
        }
    }

    private Tile loadCollisionTile(int pixel, Vector2 pos, int tileSize) {
        if(pixel == Color.rgba8888(Color.BLACK)) return new Tile(pos, tileSize, Textures.KEY_COLLISION);
        if(pixel == Textures.C_PORTAL) return new Tile(pos, tileSize, Textures.KEY_PORTAL, false, true, new Action(callable));
        if(pixel == Textures.C_ENEMY) enemies.add(loadEnemy(pos));
        if(pixel == Color.rgba8888(Color.WHITE)) playerPosition = pos;
        return null;
    }

    private Tile loadDecor1Tile(int pixel, Vector2 pos, int tileSize) {
        if (pixel == Textures.C_WALL) return new Tile(pos, tileSize, Textures.KEY_WALL);
        if (pixel == Textures.C_FLOOR) return new Tile(pos, tileSize, Textures.KEY_FLOOR);
        if (pixel == Textures.C_WALLBOAZI) return new Tile(pos, tileSize, Textures.KEY_WALLBOAZI);
        if (pixel == Textures.C_FLOORLAMIN) return new Tile(pos, tileSize, Textures.KEY_FLOORLAMIN);
        if (pixel == Textures.C_FLOOR_BROKEN) return new Tile(pos, tileSize, Textures.KEY_FLOOR_BROKEN);
        if (pixel == Textures.C_WALL_TAP) return new Tile(pos, tileSize, Textures.KEY_WALL_TAP);
        if (pixel == Textures.C_WALL_TAP_BROKEN) return new Tile(pos, tileSize, Textures.KEY_WALL_TAP_BROKEN);
        return null;
    }

    private Tile loadDecor2Tile(int pixel, Vector2 pos, int tileSize) {
        if(pixel == Textures.C_WALLUP) return new Tile(pos, tileSize, Textures.KEY_WALLUP);
        return null;
    }

    public Enemy loadEnemy(Vector2 pos) {
        Enemy entity;

        entity = new Enemy();
        entity.setPosition(pos);
        entity.setHP(2*levelId);
        entity.setAttackDmg((int)(1.2*levelId));
        entity.setFriendly(false);
        entity.setTextureId(Textures.KEY_ENEMY);

        return entity;
    }

    public ArrayList<Entity> getTilesForRenderDec1() {
        ArrayList<Entity> result = new ArrayList<Entity>();
        for (int i = (int)levelSize.x-1; i>=0; i--) {
            for (int j = (int)levelSize.y-1; j>=0; j--) {
                if (exist(i,j,1))
                    result.add(levelTiles[i][j][1]);
            }
        }
        return result;
    }

    public ArrayList<Entity> getTilesForRenderDec2() {
        ArrayList<Entity> result = new ArrayList<Entity>();
        for (int i = (int)levelSize.x-1; i>=0; i--) {
            for (int j = (int)levelSize.y-1; j>=0; j--) {
                if (exist(i,j,2))
                    result.add(levelTiles[i][j][2]);
            }
        }
        return result;
    }

    public ArrayList<Entity> getTilesForRenderCircleDec1(Movable source, Vector3 pos) {
        ArrayList<Entity> result = new ArrayList<Entity>();
        for (int i = (int)((pos.x-Gdx.graphics.getWidth()/2)/tileSize)-1; i<(int)((pos.x+Gdx.graphics.getWidth()/2)/tileSize)+2; i++) {
            for (int j = (int)((pos.y-Gdx.graphics.getHeight()/2)/tileSize)-1; j<(int)((pos.y+Gdx.graphics.getHeight()/2)/tileSize)+2; j++) {
                if (exist(i,j,1))
                    if(getDistance(source, levelTiles[i][j][1]) < 280) {
                        levelTiles[i][j][1].setTint(256-getDistance(source, levelTiles[i][j][1]));
                        result.add(levelTiles[i][j][1]);
                    }
            }
        }
        return result;
    }

    public ArrayList<Entity> getTilesForRenderCircleDec2(Movable source, Vector3 pos) {
        ArrayList<Entity> result = new ArrayList<Entity>();
        for (int i = (int)((pos.x-Gdx.graphics.getWidth()/2)/tileSize)-1; i<(int)((pos.x+Gdx.graphics.getWidth()/2)/tileSize)+2; i++) {
            for (int j = (int)((pos.y-Gdx.graphics.getHeight()/2)/tileSize)-1; j<(int)((pos.y+Gdx.graphics.getHeight()/2)/tileSize)+2; j++) {
                if (exist(i,j,2))
                    if(getDistance(source, levelTiles[i][j][2]) < 280) {
                        levelTiles[i][j][2].setTint(256-getDistance(source, levelTiles[i][j][2]));
                        result.add(levelTiles[i][j][2]);
                    }
            }
        }
        return result;
    }

    public ArrayList<Entity> getTilesForRenderObjects(Movable source, Vector3 pos) {
        ArrayList<Entity> result = new ArrayList<Entity>();
        for (int i = (int)((pos.x-Gdx.graphics.getWidth()/2)/tileSize)-1; i<(int)((pos.x+Gdx.graphics.getWidth()/2)/tileSize)+2; i++) {
            for (int j = (int)((pos.y-Gdx.graphics.getHeight()/2)/tileSize)-1; j<(int)((pos.y+Gdx.graphics.getHeight()/2)/tileSize)+2; j++) {
                if (exist(i,j,0))
                    if((getDistance(source, levelTiles[i][j][0]) < 280) && levelTiles[i][j][0].isVisible()) {
                        levelTiles[i][j][0].setTint(256-getDistance(source, levelTiles[i][j][0]));
                        result.add(levelTiles[i][j][0]);
                    }
            }
        }
        return result;
    }

    private int getDistance(Entity source, Entity target) {
        return (int)Math.sqrt(Math.pow((source.getPosition().x+source.getSize().x/2)-(target.getPosition().x+target.getSize().x/2), 2)
                + Math.pow((source.getPosition().y+source.getSize().y/2)-(target.getPosition().y+target.getSize().y/2), 2));
    }

    private boolean exist(int x, int y, int z) {
        if (x>=0 && x<levelSize.x && y>=0 && y<levelSize.y)
            if (levelTiles[x][y][z] != null)
                return true;
        return false;
    }

    public ArrayList<Movable> getEnemies() {
        return enemies;
    }

    public CollisionPlane getCollisionEntities() {
        CollisionPlane result = new CollisionPlane((int)levelSize.x, (int)levelSize.y, tileSize);

        for (int i = 0; i<levelSize.x; i++) {
            for (int j = 0; j<levelSize.y; j++) {
                if (exist(i,j,0))
                    result.add(i,j,levelTiles[i][j][0]);
            }
        }

        return result;
    }

    public void setCallable(Callable<Void> callable) {
        this.callable = callable;
    }

    public int getLevelId() {
        return levelId;
    }

    public Vector2 getLevelSize() {
        return levelSize;
    }

    public Vector2 getPlayerPosition() {
        return playerPosition;
    }
}
