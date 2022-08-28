package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationHero {
//    private Texture img;
    private TextureAtlas atlas;
    private com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> anmHero;
    private float time;

    public AnimationHero(String nameOfHero, int col, int row){
//        img = new Texture(nameOfHero);
//
//        TextureRegion region = new TextureRegion(img);
//        int xCnt = region.getRegionWidth()/col;
//        int yCnt = region.getRegionHeight()/row;
//        TextureRegion[][] region1 = region.split(xCnt, yCnt);
//        TextureRegion[] regions = new TextureRegion[region1.length * region1[0].length];
//        int count = 0;
//        for (int i = 0; i < region1.length; i++) {
//            for (int j = 0; j < region1[0].length; j++) {
//                regions[count++] = region1[i][j];
//            }
//        }

        atlas = new TextureAtlas("atlas/walk.atlas");

//        anmHero = new com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>(1 / 15f, regions);
        anmHero = new com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>(1 / 15f, atlas.findRegions("walk"));
        anmHero.setPlayMode(com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP);

        time += Gdx.graphics.getDeltaTime();
    }

    public TextureRegion getFrame(){
        return anmHero.getKeyFrame(time);
    }

    public void setTime(float time){
        this.time += time;
    }

    public void zeroTime(){
        this.time = 0;
    }

    public boolean isAnimationOver(){
        return anmHero.isAnimationFinished(time);
    }

    public void setPlayMode(com.badlogic.gdx.graphics.g2d.Animation.PlayMode playMode){
        anmHero.setPlayMode(playMode);
    }

    public void dispose(){
//        img.dispose();
        atlas.dispose();
    }
}
