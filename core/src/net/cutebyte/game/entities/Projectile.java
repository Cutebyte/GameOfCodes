package net.cutebyte.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import net.cutebyte.game.Textures;

/**
 * Created by hopskocz on 22.11.14.
 */
public class Projectile extends Movable {

    public static int LONG = 50;
    public static int MID = 25;
    public static int SHORT = 10;

    private int ttl;

    public Projectile(Movable entity, Vector2 size, int facing, int ttl) {
        this.size = size;
        this.position = new Vector2(entity.getPosition().x+entity.getSize().x/2-size.x/2,
                entity.getPosition().y+entity.getSize().y/2-size.y/2);
        this.objectType = Movable.PROJECTILE;
        this.ttl = ttl;
        this.hitPoints = 1;
        textureId = Textures.KEY_BULLET;
        owner = entity;
        friendly = owner.isFriendly();
        attackDmg = owner.attackDmg;
        switch(facing) {
            case 0: velocity = new Vector2(owner.getVelocity().x-10,owner.getVelocity().y); break;
            case 1: velocity = new Vector2(owner.getVelocity().x+10,owner.getVelocity().y); break;
            case 2: velocity = new Vector2(owner.getVelocity().x,owner.getVelocity().y+10); break;
            case 3: velocity = new Vector2(owner.getVelocity().x,owner.getVelocity().y-10); break;
        }
    }

    @Override
    public void updatePosition() {
        position.x += velocity.x;
        position.y += velocity.y;
        ttl--;
    }

    @Override
    public boolean isLiving() {
        return ttl > 0 && hitPoints > 0;
    }
}
