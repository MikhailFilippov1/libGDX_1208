package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	int clk;
	AnimationHero animationHero;
	boolean heroDirection = true;
	boolean lookRight = true;
	int distance = 0;
	int heroPositionX = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();

		animationHero = new AnimationHero("elemental.png", 8, 1);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1);

		animationHero.setTime(Gdx.graphics.getDeltaTime());
		float x = Gdx.input.getX() - animationHero.getFrame().getRegionWidth()/2;
		float y = Gdx.graphics.getHeight() - Gdx.input.getY() - animationHero.getFrame().getRegionHeight()/2;

		if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) clk++;
		Gdx.graphics.setTitle("Clicks " + clk + " times!");

		if(Gdx.input.isKeyJustPressed(Input.Keys.L)) heroDirection = true;
		if(Gdx.input.isKeyJustPressed(Input.Keys.R)) heroDirection = false;

		if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) lookRight = false;
		if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) lookRight = true;
		if(heroPositionX + 128 >= Gdx.graphics.getWidth()) lookRight = false;
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
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		animationHero.dispose();
	}
}
