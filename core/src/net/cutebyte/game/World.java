package net.cutebyte.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import net.cutebyte.game.Tools.CollisionPlane;
import net.cutebyte.game.entities.Entity;
import net.cutebyte.game.entities.Movable;
import net.cutebyte.game.entities.Player;
import net.cutebyte.game.entities.Projectile;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class World {
    private Level level;
    private Player player;

    private ArrayList<Movable> forPhysics;
    private CollisionPlane collisionPlane;

    public World() {
        level = new Level();
        level.setCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                nextLevel();
                return null;
            }
        });
        level.loadLevel(1);
        collisionPlane = level.getCollisionEntities();

        forPhysics = new ArrayList<Movable>();
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.player.setPosition(level.getPlayerPosition());
    }

    public void buildPhysicsList() {
        forPhysics.clear();
        forPhysics.addAll(level.getEnemies());
        forPhysics.add(player);
    }

    public ArrayList<Entity> getRenderList(Vector3 pos) {
        ArrayList<Entity> forRender = new ArrayList<Entity>();

        forRender.addAll(level.getTilesForRenderCircleDec1(player, pos));
        forRender.addAll(level.getTilesForRenderObjects(player, pos));
        forRender.addAll(forPhysics);
        forRender.addAll(level.getTilesForRenderCircleDec2(player, pos));

        return forRender;
    }

    public void createProjectile(Movable owner) {
        forPhysics.add(new Projectile(owner, new Vector2(16,16), owner.getFacing(), Projectile.MID));
    }

    public ArrayList<Movable> getPhysicsList() {
        return forPhysics;
    }

    public void reload() {
        level.loadLevel(level.getLevelId());
        player.setPosition(level.getPlayerPosition());
        player.revive();
        buildPhysicsList();
    }

    public void nextLevel() {
        level.loadLevel(level.getLevelId()+1);
        collisionPlane.reset((int) level.getLevelSize().x, (int) level.getLevelSize().y);
        collisionPlane.addAll(level.getCollisionEntities());
        player.revive();
        player.setPosition(level.getPlayerPosition());
        buildPhysicsList();
    }

    public CollisionPlane getCollisionPlane() {
        return collisionPlane;
    }
}
