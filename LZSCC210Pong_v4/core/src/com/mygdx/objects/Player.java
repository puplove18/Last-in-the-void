package com.mygdx.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.helpers.Constants;

Public class Player{
    private String name;
    private float health;
    private float oxygen;
    private Array inventory;

    public void setHealth(val) {
        this.health = val;
    }

    public float getHealth() {
        return (float) this.health;
    }

    public void setOxygen(val) {
        this.oxygen = val;
    }

    public float getOxygen() {
        return (float) this.oxygen;
    }    

    public void setName(player_name) {
        this.name = player_name;
    }

	public void setInventory(inventory) {
		this.inventory = inventory;
	}

	public void getInventory() {
		return this.inventory;
	}

    

}
