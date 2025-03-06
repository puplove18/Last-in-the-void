package com.mygdx.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.helpers.BodyHelper;
import com.mygdx.helpers.Constants;
import com.mygdx.helpers.ContactType;
import com.mygdx.pong.PongGame;
import com.mygdx.screens.GameScreen;
import com.mygdx.pong.PongGame;

public class Alien extends InteractiveObject{

    private String type;
    private Attitude attitude;

    enum Attitude {
        FRIEND,
        NUETRAL,
        ENEMY
    }

    public Alien(String type) {
        this.type = type;
        if (type.equals("Humanoid")) {
            this.attitude = Attitude.FRIEND;

        } else {
            this.attitude = Attitude.ENEMY;
        }
    }

    public void interact() {
        System.out.println("Encountered " + type + " alien: " + attitude);
    }
    public void greeting(){
        if (this.attitude == Attitude.FRIEND){
            System.out.println("\'Friedly greetimng from alien\'");
        }
        else{
            System.out.println("\'You feel bad intentions from this alien\'");
        }
    }

}