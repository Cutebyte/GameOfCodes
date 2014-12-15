package net.cutebyte.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import net.cutebyte.game.Textures;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class Player extends Movable {

    public Player(Vector2 position, Vector2 size) {
        this.position = position;
        this.size = size;
        tint = -1;
    }

    public void updatePosition() {
        position.x += velocity.x;
        position.y += velocity.y;
        velocity.x *= 0.8;
        velocity.y *= 0.8;

        if(hurtLock) {
            if(hurtLockCooldown>0) hurtLockCooldown--;
            else hurtLock = false;
        }

        facing = gib(velocity.x, velocity.y);
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
        return 0;
    }

    public void render(SpriteBatch spriteBatch) {
        if(facing == 2 || facing == 3)
            spriteBatch.draw(Textures.getTexture(textureId), getPosition().x, getPosition().y, getSize().x, (int)(getSize().y*1.5), this.getFacing()*32, ((int)(getPosition().y/50)%4)*48, 32, 48, false, false);
        else
            spriteBatch.draw(Textures.getTexture(textureId), getPosition().x, getPosition().y, getSize().x, (int)(getSize().y*1.5), this.getFacing()*32, ((int)(getPosition().x/50)%4)*48, 32, 48, false, false);
    }


    public void hurt(int hitPoints) {
        if(!hurtLock) {
            this.hitPoints -= hitPoints;
            hurtLock = true;
            hurtLockCooldown = 50;
        }
    }

    public void revive() {
        this.hitPoints = 10;
    }
}
