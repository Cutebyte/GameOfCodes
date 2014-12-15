package net.cutebyte.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import net.cutebyte.game.GUI.Control;
import net.cutebyte.game.entities.Movable;
import net.cutebyte.game.entities.Player;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class SuperTestGDX extends ApplicationAdapter {

    private Player player;
    private Physics physics;
    private Renderer renderer;
    private Control control;
    private World world;

    @Override
	public void create() {

        renderer = new Renderer(); // here goes render thing!

        control = new Control(renderer.getCamera());
        renderer.connectControl(control);
        Gdx.input.setInputProcessor(control);

        world = new World();

        physics = new Physics();

        ArrayList<Movable> objectList = new ArrayList<Movable>();
        physics.bindList(objectList);

        control.bindButton(5,new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if (!control.getButton(5).isLocked()) {
                    control.getControlled().attack();
                    control.getButton(5).lock();
                }
                return null;
            }
        });

        control.bindButton(6,new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if (!control.getButton(6).isLocked()) {
                    control.getControlled().secondaryAttack();
                    control.getButton(6).lock();
                }
                return null;
            }
        });

        player = new Player(new Vector2(100,500), new Vector2(64,64));
        player.setHP(10);
        player.setAttackDmg(1);
        player.setFriendly(true);
        player.setTextureId(Textures.KEY_PLAYER);
        objectList.add(player);
        control.setControlled(player);
        player.bindAttack(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                world.createProjectile(player);
                return null;
            }
        });
        player.bindSecondaryAttack(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                player.use();
                return null;
            }
        });

        world.setPlayer(player);
        world.buildPhysicsList();

        physics.bindCollisionPlane(world.getCollisionPlane());
        physics.bindList(world.getPhysicsList());

        renderer.setCameraFocus(player);
    }

	@Override
	public void render() {
        control.update(renderer.getCamera().position);
        physics.logics();
        renderer.setList(world.getRenderList(renderer.getCamera().position));
        renderer.render();
        rules();
	}

    public void rules() {
        if(!player.isLiving()) {
            world.reload();
        }
    }
}
