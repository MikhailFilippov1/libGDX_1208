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
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.AnimationHero;
import com.mygdx.game.MainApp;

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
    private Rectangle mapSize;
    private ShapeRenderer shapeRenderer;

    public GameScreen(MainApp game) {
        this.game = game;
        batch = new SpriteBatch();

        animationHero = new AnimationHero("elemental.png", 8, 1);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        map = new TmxMapLoader().load("map/безымянный.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);


//        map.getLayers().get("Объекты").getObjects().getByType(RectangleMapObject.class);    //выбор всех объектов со слоя Объекты по типу
        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("Объекты1").getObjects().get("Camera#1");//выбор всех объектов со слоя Объекты по имени

        camera.position.x = tmp.getRectangle().x;
        camera.position.y = tmp.getRectangle().y;

        tmp = (RectangleMapObject) map.getLayers().get("Объекты1").getObjects().get("Border");
        mapSize = tmp.getRectangle();

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && mapSize.x < (camera.position.x - STEP)) camera.position.x -= STEP;
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && (mapSize.x + mapSize.width) > (camera.position.x + STEP)) camera.position.x += STEP;
        if(Gdx.input.isKeyPressed(Input.Keys.UP) && mapSize.y < (camera.position.y - STEP)) camera.position.y -= STEP;
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && (mapSize.x + mapSize.height) > (camera.position.x + STEP)) camera.position.y += STEP;

        if(Gdx.input.isKeyPressed(Input.Keys.P)) camera.zoom += 0.01f;
        if(Gdx.input.isKeyPressed(Input.Keys.O) && camera.zoom > 0) camera.zoom -= 0.01f;

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
        batch.draw(animationHero.getFrame(), heroPositionX, 0);
        batch.end();

        mapRenderer.setView(camera);
        mapRenderer.render();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            dispose();
            game.setScreen(new MenuScreen(game));
        }

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.rect(mapSize.x, mapSize.y, mapSize.width, mapSize.height);
        shapeRenderer.end();
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
