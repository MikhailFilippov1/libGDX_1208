package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    int distance = 0;
    int heroPositionX = 0;

    public GameScreen(MainApp game) {
        this.game = game;
        batch = new SpriteBatch();

        animationHero = new AnimationHero("elemental.png", 8, 1);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.YELLOW);

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

        batch.begin();
        batch.draw(animationHero.getFrame(), heroPositionX, 0);
        batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)){
            dispose();
            game.setScreen(new MenuScreen(game));
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

    }
}
