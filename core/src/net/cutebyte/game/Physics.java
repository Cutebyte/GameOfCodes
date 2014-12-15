package net.cutebyte.game;

import com.badlogic.gdx.math.Vector2;
import net.cutebyte.game.Tools.CollisionPlane;
import net.cutebyte.game.entities.Entity;
import net.cutebyte.game.entities.Movable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class Physics {
    private ArrayList<Movable> physicsList;
    private CollisionPlane collisionPlane;

    public Physics() {

    }

    public void bindCollisionPlane(CollisionPlane collisionPlane) {
        this.collisionPlane = collisionPlane;
    }

    public void bindList(ArrayList<Movable> list) {
        physicsList = list;
    }

    public void logics() {
        Movable temp;
        for (Iterator<Movable> itr = physicsList.iterator(); itr.hasNext(); ) {
            temp = itr.next();
            if (!temp.isLiving()) {
                itr.remove();
                continue;
            }
            else {
                temp.updatePosition();
                temp.whoToFollow(physicsList);
            }
            if(collisionCheck(temp) && (temp.getObjectType() == Movable.PROJECTILE))
                itr.remove();
        }
    }

    public boolean collisionCheck(Movable movable) {
        boolean result = false;
        HashSet<Entity> colliders = collisionPlane.optiGet(movable);
        for (Iterator<Entity> itr = colliders.iterator(); itr.hasNext(); )
            if (dealWithWorldCollisions(movable, itr.next()))
                result = true;
        for (Iterator<Movable> itr = physicsList.iterator(); itr.hasNext(); ) {
            if (dealWithOtherCollisions(movable, itr.next()))
                result = true;
        }
        return result;
    }

    public boolean dealWithWorldCollisions(Movable entity, Entity block)
    {
        int pos;

        if(    entity.getPosition().x + entity.getVelocity().x + entity.getSize().x > block.getPosition().x  //l
            && entity.getPosition().x + entity.getVelocity().x < block.getPosition().x + block.getSize().x  //r
            && entity.getPosition().y + entity.getVelocity().y < block.getPosition().y + block.getSize().y //d
            && entity.getPosition().y + entity.getVelocity().y + entity.getSize().y > block.getPosition().y //u
        ) {
            if(block.isSolid()) {
                pos = detPos(entity, block);
                if (pos == 0) {
                    entity.setPosition(new Vector2(block.getPosition().x - entity.getSize().x, entity.getPosition().y));
                    entity.setForce(new Vector2(0, entity.getVelocity().y));
                    return true;
                }
                if (pos == 1) {
                    entity.setPosition(new Vector2(block.getPosition().x + block.getSize().x, entity.getPosition().y));
                    entity.setForce(new Vector2(0, entity.getVelocity().y));
                    return true;
                }
                if (pos == 2) {
                    entity.setPosition(new Vector2(entity.getPosition().x, block.getPosition().y + block.getSize().y));
                    entity.setForce(new Vector2(entity.getVelocity().x, 0));
                    return true;
                }
                if (pos == 3)
                {
                    entity.setPosition(new Vector2(entity.getPosition().x, block.getPosition().y - entity.getSize().y));
                    entity.setForce(new Vector2(entity.getVelocity().x, 0));
                    return true;
                }
            }
            if (block.canInteract()) {
                if (entity.isUsing()) {
                    block.callAction();
                }
            }
        }
        return false;
    }

    /*
      2
    0 + 1   <-- helping a lot :D
      3
     */

    public boolean dealWithOtherCollisions(Movable first, Movable second)
    {
        boolean result = false;
        if (!first.equals(second) && !(first.equals(second.getOwner()) || second.equals(first.getOwner()))) {
            int pos;
            if (first.getPosition().x + first.getSize().x + first.getVelocity().x > second.getPosition().x + second.getVelocity().x  //lewo
                    && first.getPosition().x + first.getVelocity().x < second.getPosition().x + second.getSize().x + second.getVelocity().x  //prawo
                    && first.getPosition().y + first.getVelocity().y < second.getPosition().y + second.getSize().y + second.getVelocity().y //down
                    && first.getPosition().y + first.getSize().y + first.getVelocity().y > second.getPosition().y + second.getVelocity().y  //up
                    ) {
                pos = detPos(first, second);
                if (pos == 0) {
                    first.setPosition(new Vector2(second.getPosition().x - first.getSize().x, first.getPosition().y));
                    float temp = first.getVelocity().x;
                    first.setForce(second.getVelocity().x, first.getVelocity().y);
                    second.setForce(temp, second.getVelocity().y);
                    result = true;
                }
                if (pos == 1) {
                    first.setPosition(new Vector2(second.getPosition().x + second.getSize().x, first.getPosition().y));
                    float temp = first.getVelocity().x;
                    first.setForce(second.getVelocity().x, first.getVelocity().y);
                    second.setForce(temp, second.getVelocity().y);
                    result = true;
                }
                if (pos == 2) {
                    first.setPosition(new Vector2(first.getPosition().x, second.getPosition().y + second.getSize().y));
                    float temp = first.getVelocity().y;
                    first.setForce(first.getVelocity().x, second.getVelocity().y);
                    second.setForce(second.getVelocity().x, temp);
                    result = true;
                }
                if (pos == 3)
                {
                    first.setPosition(new Vector2(first.getPosition().x, second.getPosition().y - first.getSize().y));
                    float temp = first.getVelocity().y;
                    first.setForce(first.getVelocity().x, second.getVelocity().y);
                    second.setForce(second.getVelocity().x, temp);
                    result = true;
                }
            }
            if(result && (first.isLiving() && second.isLiving()) && !(first.isFriendly() == second.isFriendly())) {
                if(!first.getHurtLock() && !second.getHurtLock()) {
                    first.hurt(second.getAttackDmg());
                    second.hurt(first.getAttackDmg());
                    Sounds.playHurt();
                }
            }
        }
        return result;
    }

    public int detPos(Movable entity, Entity dstEntity)
    {
        return gib(((entity.getPosition().x+entity.getSize().x/2)-(dstEntity.getPosition().x+dstEntity.getSize().x/2)),
                ((entity.getPosition().y+entity.getSize().y/2-(dstEntity.getPosition().y+dstEntity.getSize().y/2))));

    }

    private int gib(float x, float y) {
        if( (x-y)<0 && (-x-y)>0 )
            return 0;
        if( (x-y)>0 && (-x-y)<0 )
            return 1;
        if( (x-y)<0 && (-x-y)<0 )
            return 2;
        if( (x-y)>0 && (-x-y)>0 )
            return 3;
        return -1;
    }
}
