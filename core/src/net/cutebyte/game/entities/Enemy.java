package net.cutebyte.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.cutebyte.game.Textures;

/**
 * Created by CuteByte (Tomasz Chmielewski a.k.a Hopskocz)
 * First android game
 */
public class Enemy extends Movable {

    public void render(SpriteBatch spriteBatch) {
        if(facing == 2 || facing == 3)
            spriteBatch.draw(Textures.getTexture(textureId), getPosition().x, getPosition().y, getSize().x, (int)(getSize().y*1.5), this.getFacing()*32, ((int)(getPosition().y/50)%4)*48, 32, 48, false, false);
        else
            spriteBatch.draw(Textures.getTexture(textureId), getPosition().x, getPosition().y, getSize().x, (int)(getSize().y*1.5), this.getFacing()*32, ((int)(getPosition().x/50)%4)*48, 32, 48, false, false);
    }
}
