package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MainApp;

public class MenuScreen implements Screen {
    private MainApp game;
    private SpriteBatch batch;
    private Texture img;
    private Rectangle startRectangle;
    private ShapeRenderer shapeRenderer;

    public MenuScreen(MainApp game) {
        this.game = game;
        batch = new SpriteBatch();
        img = new Texture("drop.png");
        startRectangle = new Rectangle(Gdx.graphics.getWidth()/2f - img.getWidth()/2f, Gdx.graphics.getHeight()/2f - img.getHeight()/2f, img.getWidth(), img.getHeight());
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLUE);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(startRectangle.x, startRectangle.y, startRectangle.width, startRectangle.height);
        shapeRenderer.end();

        batch.begin();
        batch.draw(img, Gdx.graphics.getWidth()/2f - img.getWidth()/2f, Gdx.graphics.getHeight()/2f - img.getHeight()/2f);
        batch.end();

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

            if(startRectangle.contains(x, y)){
                dispose();
                game.setScreen(new GameScreen(game));
            }
        }

    }

    @Override
    public void resize(int width, int height) {

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
        this.batch.dispose();
        this.img.dispose();
        this.shapeRenderer.dispose();
    }
}
