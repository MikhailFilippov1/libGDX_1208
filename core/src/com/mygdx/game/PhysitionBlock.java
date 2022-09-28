package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PhysitionBlock {
    private final World world;
    private final Box2DDebugRenderer debugRenderer;
    public static final float PPM = 100; // Point per meter: приводим физику к нормальным размерам

    public PhysitionBlock(){
        world = new World(new Vector2(0,-9.81f), true);
        world.setContactListener(new MyContactListener());
        debugRenderer = new Box2DDebugRenderer();
    }

    public void destroyBody(Body body){
        world.destroyBody(body);
    }

    public Body addObject(RectangleMapObject object){
        Rectangle rect = object.getRectangle();
        String type = (String) object.getProperties().get("BodyType");
        BodyDef def = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        if(type.equals("StaticBody"))def.type = BodyDef.BodyType.StaticBody;
        if(type.equals("DynamicBody"))def.type = BodyDef.BodyType.DynamicBody;

        def.position.set((rect.x + rect.width/2) / PPM, (rect.y + rect.height/2) / PPM);
        def.gravityScale = (float) object.getProperties().get("GravityScale");

        polygonShape.setAsBox(rect.width/2/PPM, rect.height/2/PPM);

        fdef.shape = polygonShape;
        fdef.friction = 0.5f;
        fdef.density = 1;
        fdef.restitution = (float) object.getProperties().get("Restitution");

        Body body;
        body = world.createBody(def);
        String name = object.getName();
        body.createFixture(fdef).setUserData(name);
        if(name != null && name.equals("Hero")){
            polygonShape.setAsBox(rect.width/6/PPM, rect.height/12/PPM, new Vector2(0, -rect.width/2/PPM), 0);
            body.createFixture(fdef).setUserData("ноги");
            body.setFixedRotation(true);
            body.getFixtureList().get(body.getFixtureList().size-1).setSensor(true);
        }


        polygonShape.dispose();
        return body;
    }

    public void setGravity(Vector2 gravity){
        world.setGravity(gravity);
    }

    public void step(){
        world.step(1/60.0f, 3, 3);
    }

    public void debugDraw(OrthographicCamera cam){
        debugRenderer.render(world, cam.combined);
    }

    public void dispose(){
        world.dispose();
        debugRenderer.dispose();
    }
}
