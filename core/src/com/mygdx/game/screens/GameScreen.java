package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.AnimationHero;
import com.mygdx.game.MainApp;
import com.mygdx.game.PhysitionBlock;

import java.util.ArrayList;

public class GameScreen implements Screen {
    private MainApp game;
    SpriteBatch batch;
    int clk;
    int coinsQuantity;
    private final AnimationHero heroWalk;
    private final AnimationHero heroRun;
    private final AnimationHero heroJump;
    private final AnimationHero heroIdle;
    private final AnimationHero heroHit;
    private  AnimationHero hero;
    boolean heroDirection = true;
    boolean lookRight = true;
    public static boolean canJump = false;

    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;


    private ShapeRenderer shapeRenderer;
    private PhysitionBlock physitionBlock;

    private final int[] background;
    private final int[] layer1;
    private Body body;
    private final Rectangle heroRect;
    private final Music music;
    private final Sound sound;
    public static ArrayList<Body> bodies;

    public GameScreen(MainApp game) {
        bodies = new ArrayList<>();
        this.game = game;
        batch = new SpriteBatch();

        heroWalk = new AnimationHero("unnamed", "walk", Animation.PlayMode.LOOP);
        heroRun = new AnimationHero("unnamed","run", Animation.PlayMode.LOOP);
        heroJump = new AnimationHero("unnamed","jump", Animation.PlayMode.LOOP_REVERSED);
        heroIdle = new AnimationHero("unnamed","idle", Animation.PlayMode.NORMAL);
        heroHit = new AnimationHero("unnamed","hit", Animation.PlayMode.LOOP_REVERSED);
        hero = heroIdle;

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.25f;

        music = Gdx.audio.newMusic(Gdx.files.internal("muzyka-iz-kino-rozovaja-pantera.mp3"));
        music.setLooping(true);
        music.play();

        sound = Gdx.audio.newSound(Gdx.files.internal("Bulk.mp3"));

        map = new TmxMapLoader().load("map/безымянный.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        background = new int[1];
        background[0] = map.getLayers().getIndex("фон");
        layer1 = new int[2];
        layer1[0] = map.getLayers().getIndex("Слой 2");
        layer1[1] = map.getLayers().getIndex("Слой 2");

        physitionBlock = new PhysitionBlock();

        map.getLayers().get("Объекты1").getObjects().getByType(RectangleMapObject.class);    //выбор всех объектов со слоя Объекты по типу
        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("сеттинг").getObjects().get("Hero");//выбор всех объектов со слоя Объекты по имени
        heroRect = tmp.getRectangle();
        body = physitionBlock.addObject(tmp);


        Array<RectangleMapObject> objects = map.getLayers().get("Объекты1").getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < objects.size; i++) {
            physitionBlock.addObject(objects.get(i));
        }

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        boolean isKeyPressed = false;

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) ){
            isKeyPressed = true;
            hero = heroWalk;
            body.applyForceToCenter(new Vector2(-1, 0), true);
            if(body.getLinearVelocity().x < -5f) hero = heroRun;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) ) {
            isKeyPressed = true;
            hero = heroWalk;
            body.applyForceToCenter(new Vector2(1, 0), true);
            if(body.getLinearVelocity().x > 5f) hero = heroRun;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            isKeyPressed = true;
            if(canJump) {
                hero = heroJump;
                body.applyForceToCenter(new Vector2(0, 10), true);
                canJump = false;
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && canJump) {
            isKeyPressed = true;
            hero = heroHit;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.NUMPAD_ADD)) camera.zoom += 0.01f;
        if(Gdx.input.isKeyPressed(Input.Keys.NUMPAD_SUBTRACT) && camera.zoom > 0) camera.zoom -= 0.01f;

        camera.position.x = body.getPosition().x * PhysitionBlock.PPM;
        camera.position.y = body.getPosition().y * PhysitionBlock.PPM;
        camera.update();
        ScreenUtils.clear(Color.BLACK);

        hero.setTime(Gdx.graphics.getDeltaTime());

        Gdx.graphics.setTitle(coinsQuantity + " coins keeps!");

        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) lookRight = false;
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) lookRight = true;

        if(!hero.getFrame().isFlipX() && !lookRight) hero.getFrame().flip(true, false);
        if(hero.getFrame().isFlipX() && lookRight) hero.getFrame().flip(true, false);

        float x = Gdx.graphics.getWidth()/2f - heroRect.getWidth()/2/camera.zoom;
        float y = Gdx.graphics.getHeight()/2f - heroRect.getHeight()/2/camera.zoom;
        batch.begin();
        heroRect.x = body.getPosition().x - heroRect.width/2;
        heroRect.y = body.getPosition().y - heroRect.height/2;
        batch.draw(hero.getFrame(), x, y);
        batch.end();

        mapRenderer.setView(camera);
        mapRenderer.render(background);
        mapRenderer.render(layer1);

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            dispose();
            game.setScreen(new MenuScreen(game));
        }

//        shapeRenderer.setProjectionMatrix(camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(Color.GOLD);
//        for (int i = 0; i < objects.size; i++) {
//            Rectangle mapSize = objects.get(i).getRectangle();
//            shapeRenderer.rect(mapSize.x, mapSize.y, mapSize.width, mapSize.height);
//        }
//        shapeRenderer.end();


        physitionBlock.step();
        physitionBlock.debugDraw(camera);

        for (int i = 0; i < bodies.size(); i++) {
            physitionBlock.destroyBody(bodies.get(i));
            sound.play();
            coinsQuantity++;
        }
        bodies.clear();
        if(!isKeyPressed) {
            hero = heroIdle;
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.music.dispose();
        this.mapRenderer.dispose();
        this.hero.dispose();
        this.sound.dispose();
    }
}
