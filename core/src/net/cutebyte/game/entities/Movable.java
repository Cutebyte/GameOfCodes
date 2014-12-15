package net.cutebyte.game.entities;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class Movable extends Entity {

    protected Vector2 velocity;
    protected int hitPoints;
    protected int maxHitPoints;
    protected int attackDmg;
    protected int objectType;
    protected boolean friendly;
    protected boolean hurtLock;
    protected int hurtLockCooldown;

    protected int facing;
    protected boolean using;

    protected Entity follow;

    protected Callable<Void> attackCallable;
    protected Callable<Void> secondaryAttackCallable;

    protected Movable owner;

    //Object types
    public static int BOX = 2;
    public static int PROJECTILE = 3;
    public static int DEFAULT = 0;
    public static int CREATURE = 1;
    public static int ENEMY_CREATURE = 4;

    public Movable() {
        position = new Vector2(200,200);
        velocity = new Vector2(0,0);
        size = new Vector2(64,64);
        friendly = true;
        tint = -1;
    }

    public void applyForce(Vector2 velocity) {
        this.velocity.x += velocity.x;
        this.velocity.y += velocity.y;
    }

    public void setOwner(Movable owner) {
        this.owner = owner;
    }

    public Movable getOwner() {
        return owner;
    }

    public void setForce(Vector2 velocity) {
        this.velocity.x = velocity.x;
        this.velocity.y = velocity.y;
    }

    public void setForce(float x, float y) {
        velocity.x = x;
        velocity.y = y;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public int getFacing() {
        return facing;
    }

    public void updatePosition() {
        position.x += velocity.x;
        position.y += velocity.y;
        velocity.x *= 0.8;
        velocity.y *= 0.8;

        facing = gib(velocity.x, velocity.y);

        if(isFollowing()) {
            follow();
        }

        if(hurtLock) {
            if(hurtLockCooldown>0) hurtLockCooldown--;
            else hurtLock = false;
        }
    }

    private int gib(float x, float y) {
        if( (x-y)<0 && (-x-y)>0 )
            return 0;
        if( (x-y)>=0 && (-x-y)<=0 )
            return 1;
        if( (x-y)<=0 && (-x-y)<=0 )
            return 2;
        if( (x-y)>0 && (-x-y)>0 )
            return 3;
        return -1;
    }

    public void moveUp() {
        applyForce(new Vector2(0,1.2f));
    }

    public void moveDown() {
        applyForce(new Vector2(0,-1.2f));
    }

    public void moveLeft() {
        applyForce(new Vector2(-1.2f,0));
    }

    public void moveRight() {
        applyForce(new Vector2(1.2f,0));
    }

    public void bindAttack(Callable<Void> callable) {
        attackCallable = callable;
    }

    public void bindSecondaryAttack(Callable<Void> callable) {
        secondaryAttackCallable = callable;
    }

    public void attack() {
        try {
            if (attackCallable != null)
                attackCallable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void secondaryAttack() {
        try {
            if (secondaryAttackCallable != null)
                secondaryAttackCallable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void followThis(Entity entity) {
        follow = entity;
    }

    public void dontFollow() {
        follow = null;
    }

    public void follow() {
        double x = (follow.getPosition().x+follow.getSize().x/2)-(position.x+size.x/2);
        double y = (follow.getPosition().y+follow.getSize().y/2)-(position.y+size.y/2);

        double l = Math.sqrt(x * x + y * y);

        x = (x / l * 1.4)/1.6;
        y = (y / l * 1.4)/1.6;

        applyForce(new Vector2((float)x,(float)y));
    }

    public void whoToFollow(ArrayList<Movable> objects) {
        Movable best = null;
        Movable temp;
        for (Iterator<Movable> itr = objects.iterator(); itr.hasNext(); ) {
            temp = itr.next();
            if ((temp.isFriendly() != friendly) && (temp.objectType != Movable.PROJECTILE)) {
                if (getDistance(temp) < Math.pow(20,2)) {
                    if (best == null) best = temp;
                    if(getDistance(temp) < getDistance(best)) {
                        best = temp;
                    }
                }
            }
        }
        if(best != null) followThis(best);
        else dontFollow();

    }

    public boolean isFollowing() {
        return follow != null;
    }

    public boolean isFriendly() {
        return friendly;
    }

    public void setFriendly(boolean value) {
        this.friendly = value;
    }

    public void setHP(int hp) {
        this.hitPoints = hp;
        this.maxHitPoints = hp;
    }

    public void setAttackDmg(int dmg) {
        this.attackDmg = dmg;
    }

    public void hurt(int hitPoints) {
        this.hitPoints -= hitPoints;
    }

    public int getAttackDmg() {
        return attackDmg;
    }

    public int getObjectType() {
        return objectType;
    }

    public boolean isLiving() {
        return hitPoints > 0;
    }

    public boolean isUsing() {
        boolean isUsing = using;
        using = false;
        return isUsing;
    }

    public void use() {
        this.using = true;
    }

    private int getDistance(Entity target) {
        return (int)Math.sqrt(Math.pow((this.getPosition().x+this.getSize().x/2)-(target.getPosition().x+target.getSize().x/2), 2)
                + Math.pow((this.getPosition().y+this.getSize().y/2)-(target.getPosition().y+target.getSize().y/2), 2));
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    public boolean getHurtLock() {
        return hurtLock;
    }

    public void hurtLock() {
        hurtLock = true;
    }
}
