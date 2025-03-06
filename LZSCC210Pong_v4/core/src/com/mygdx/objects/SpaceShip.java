package com.mygdx.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.helpers.Constants;

public class SpaceShip extends SpaceShipBody {
    
    public SpaceShip(float x, float y, Body body) {
        super(x, y, body);
        
        Pixmap pixmap = new Pixmap(Constants.PLAYER_PADDLE_WIDTH, Constants.PLAYER_PADDLE_HEIGHT, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE); 
        pixmap.fill();
        
        this.texture = new Texture(pixmap);
        pixmap.dispose();
    }
    
    public void update() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) { 
            x = Gdx.input.getX();
            y = Gdx.graphics.getHeight() - Gdx.input.getY(); 
        }
    }
    
    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, x, y, Constants.PLAYER_PADDLE_WIDTH, Constants.PLAYER_PADDLE_HEIGHT);
    }
}
