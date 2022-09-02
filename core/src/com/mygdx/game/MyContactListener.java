package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.screens.GameScreen;

public class MyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if(a.getUserData() != null && b.getUserData() != null){
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();

            if(tmpA.equals("Hero") && tmpB.equals("Coin")){
//           b.getBody().setActive(false);   // Делаем неактивным,чтобы "физика" с ним не работала и тело можно было бы удалить
                GameScreen.bodies.add(b.getBody());
            }
            if(tmpB.equals("Hero") && tmpA.equals("Coin")){
                GameScreen.bodies.add(a.getBody());
            }
            if(tmpA.equals("ноги") && tmpB.equals("floor")){
                GameScreen.canJump = true;
            }
            if(tmpB.equals("floor") && tmpA.equals("ноги")){
                GameScreen.canJump = true;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
