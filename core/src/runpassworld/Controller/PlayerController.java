package runpassworld.Controller;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by Nicoy on 29/09/2017.
 */

public class PlayerController implements IScript {

    private Entity player;
    private TransformComponent transformComponent;
    private DimensionsComponent dimensionsComponent;

    private World world;

    private Vector2 speed;
    private float gravity = -500f;

    private final float jumpSpeed = 200f;

    public PlayerController(World world){
        this.world = world;
    }

    @Override
    public void init(Entity entity) {
        player = entity;

        //untuk mendapatkan transformasi dari player (x,y)
        transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        //untuk mendapatkan dimensi dari player (width, height)
        dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);

        speed = new Vector2(100, 0);
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            transformComponent.x -= speed.x * delta;
            transformComponent.scaleX = -1.0f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            transformComponent.x += speed.x * delta;
            transformComponent.scaleX = 1.0f;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            speed.y = jumpSpeed;
        }

        speed.y += gravity*delta;
        transformComponent.y += speed.y*delta;

        if (transformComponent.y < 7f){
            speed.y = 0;
            transformComponent.y = 7f;
        }

        rayCast();
    }

    //membuat player tidak jatuh ke bawah ground
    private void rayCast() {
        float rayGap = dimensionsComponent.height/2;

        float raySize = -speed.y*Gdx.graphics.getDeltaTime();

        if (speed.y>0) return;

        Vector2 rayFrom = new Vector2((transformComponent.x+dimensionsComponent.width/2)* PhysicsBodyLoader.getScale(), (transformComponent.y+rayGap)* PhysicsBodyLoader.getScale());
        Vector2 rayTo = new Vector2((transformComponent.x+dimensionsComponent.width/2)* PhysicsBodyLoader.getScale(), (transformComponent.y+raySize)* PhysicsBodyLoader.getScale());

        world.rayCast(new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                //re-posisi setelah player menyentuh ground
                speed.y = 0;

                transformComponent.y = point.y / PhysicsBodyLoader.getScale() - 2.0f;

                return 0;
            }
        }, rayFrom, rayTo);

    }

    public float getX(){
        return transformComponent.x;
    }

    public float getY(){
        return transformComponent.y;
    }

    @Override
    public void dispose() {

    }

    public float getWidth(){
        return dimensionsComponent.width;
    }
}
