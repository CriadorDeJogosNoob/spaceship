package com.devgamenoob.game;


//import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Random;

import com.badlogic.gdx.Gdx;

public class Enemy {
    
    private Texture texture;
    private Sprite sprite;
    private float x, y, spd;
    private int life = 1;

    public Enemy() {
        texture = new Texture("enemy.png");
        sprite = new Sprite(texture);
        x = Gdx.graphics.getWidth();
        y = new Random().nextInt(0, (int) (Gdx.graphics.getHeight() - this.sprite.getHeight()));
        spd = 5f;
    }

    public void render(SpriteBatch batch) {
        if(life > 0) {
            batch.draw(sprite, x, y);
        }
    }

    public void moveEnemy() {
        if(x > 0 - this.sprite.getWidth()) {
            x -= spd;
        }else {
            this.life = 0;
        }
    }

    public float get_X() {
        return this.x;
    }

    public float get_Y() {
        return this.y;
    }

    public Sprite get_sprite() {
        return this.sprite;
    }

    public void set_life(int life) {
        this.life = life;
    }

    public int get_life() {
        return this.life;
    }

    public Texture get_texture() {
        return texture;
    }

    public float get_spd() {
        return this.spd;
    }

    public void set_speed(float spd_new) {
        if(spd_new > 0) {
            if(spd < 10) {
                this.spd = spd_new;
            }
        }
    }


}
