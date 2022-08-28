package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

public class GameScreen implements Screen {
    private MainApp game;
    SpriteBatch batch;
    int clk;
    AnimationHero animationHero;
    boolean heroDirection = true;
    boolean lookRight = true;

    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    int distance = 0;
    int heroPositionX = 0;
    private float STEP = 3;
    private ShapeRenderer shapeRenderer;
    private PhysitionBlock physitionBlock;

    private final int[] background;
    private final int[] layer1;
    private Body body;
    private final Rectangle heroRect;

    public GameScreen(MainApp game) {
        this.game = game;
        batch = new SpriteBatch();

        animationHero = new AnimationHero("elemental.png", 8, 1);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1.25f;

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

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) ) body.applyForceToCenter(new Vector2(-10000, 0), true);
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) ) body.applyForceToCenter(new Vector2(10000, 0), true);
        if(Gdx.input.isKeyPressed(Input.Keys.UP) ) camera.position.y -= STEP;
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) ) camera.position.y += STEP;

        if(Gdx.input.isKeyPressed(Input.Keys.P)) camera.zoom += 0.01f;
        if(Gdx.input.isKeyPressed(Input.Keys.O) && camera.zoom > 0) camera.zoom -= 0.01f;

        camera.position.x = body.getPosition().x;
        camera.position.y = body.getPosition().y;
        camera.update();
        ScreenUtils.clear(Color.BLACK);

        animationHero.setTime(Gdx.graphics.getDeltaTime());
        float x = Gdx.input.getX() - animationHero.getFrame().getRegionWidth()/2;
        float y = Gdx.graphics.getHeight() - Gdx.input.getY() - animationHero.getFrame().getRegionHeight()/2;

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) clk++;
        Gdx.graphics.setTitle("Clicks " + clk + " times!");

        if(Gdx.input.isKeyJustPressed(Input.Keys.L)) heroDirection = true;
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)) heroDirection = false;

        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) lookRight = false;
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) lookRight = true;
        if(heroPositionX + 64 >= Gdx.graphics.getWidth()) lookRight = false;
        if(heroPositionX <= 0)lookRight = true;

        if(!animationHero.getFrame().isFlipX() && !lookRight) animationHero.getFrame().flip(true, false);
        if(animationHero.getFrame().isFlipX() && lookRight) animationHero.getFrame().flip(true, false);

        if(lookRight) {
            heroPositionX += 1;
        }
        else  {
            heroPositionX -= 1;
        }

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        heroRect.x = body.getPosition().x - heroRect.width/2;
        heroRect.y = body.getPosition().y - heroRect.height/2;
        batch.draw(animationHero.getFrame(), heroRect.x, heroRect.y, heroRect.width, heroRect.height);
        batch.end();

        mapRenderer.setView(camera);
        mapRenderer.render(background);

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

        mapRenderer.render(layer1);

        physitionBlock.step();
        physitionBlock.debugDraw(camera);
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

    }
}
