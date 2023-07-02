package com.devgamenoob.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;

public class SpaceShip extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, tNave, tMissil, tLogo, tGameOver;
	private Sprite nave, missil;
	private float posX, posY, spd, xMissile, yMissile, seconds_spaw_enemy;
	private boolean attack, collider_enemy_player;
	private List<Enemy> enemys = new ArrayList<Enemy>();
	private int max_enemys = 1,timer, state_game, score;


	private FreeTypeFontGenerator generator;
	private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
	private BitmapFont bitmap;

	/*
		state 0 = tela inicio
		state 1 = tela jogo
		state 2 = tela game over 

	*/

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("bg.png");
		tNave = new Texture("spaceship.png");
		nave = new Sprite(tNave);
		posX = 0;
		posY = 0;
		spd = 10;
		tMissil = new Texture("missile.png");
		missil = new Sprite(tMissil);
		xMissile = posX;
		yMissile = posY;
		attack = false;
		timer = 0;
		seconds_spaw_enemy = 2;
		state_game = 0;
		for(int m = 0;m < max_enemys;m++) {
			enemys.add(new Enemy());
		}

		score = 0;

		tLogo = new Texture("logo.png");
		tGameOver = new Texture("game_over.png");

		generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
    	parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parameter.size = 30;
		parameter.borderWidth = 1;
		parameter.borderColor = Color.BLACK;
		parameter.color = Color.WHITE;
		bitmap = generator.generateFont(parameter);

	}

	@Override
	public void render () {

		if(state_game == 0) {

			batch.begin();
			batch.draw(tLogo, 0, 0);
			batch.end();

			if(Gdx.input.isTouched()) {
				state_game = 1;
			}

		}else if(state_game == 1) {
			timer++;

			if(timer % (60*seconds_spaw_enemy) == 0) {
				max_enemys += 1;
				enemys.add(new Enemy());
			}

			if(timer % (60*5) == 0) {
				if(seconds_spaw_enemy > 0.5) {
					seconds_spaw_enemy -= 0.5f;
					for(int m = 0;m < max_enemys;m++) {
						Enemy enemy = enemys.get(m);
						enemy.set_speed(enemy.get_spd() + 0.5f);;
					}
				}
			}

			for(int m = 0;m < max_enemys;m++) {
				Enemy enemy = enemys.get(m);
				if(enemy.get_life() <= 0) {
					enemys.remove(enemy);
					max_enemys -= 1;
				}
			}

			this.moveNave();
			this.moveMissile();
			for(int m = 0;m < max_enemys;m++) {
				Enemy enemy = enemys.get(m);
				enemy.moveEnemy();
			}
			collider_enemy_player = this.collider_player_enemy();
			this.collider_bullet_enemy();

			ScreenUtils.clear(0, 0, 0, 1);
			batch.begin();
			batch.draw(img, 0, 0);
			batch.draw(missil,  xMissile + nave.getWidth()/2 - missil.getWidth()/2, yMissile + nave.getHeight()/2 - missil.getHeight()/2);
			batch.draw(nave, posX, posY);
			for(int m = 0;m < max_enemys;m++) {
				Enemy enemy = enemys.get(m);
				enemy.render(batch);
			}

			bitmap.draw(batch,"score: " + score, 10, Gdx.graphics.getHeight() - 20);
			bitmap.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 20);

			batch.end();
		}else if(state_game == 2) {

			batch.begin();
			batch.draw(tGameOver, 0, 0);
			bitmap.draw(batch, "Score: " + score, Gdx.graphics.getWidth()/2 - 50, Gdx.graphics.getHeight()/2 - 20);
			batch.end();

			if(Gdx.input.isKeyPressed(Input.Keys.R)) {
				enemys.clear();
				posX = 0;
				posY = 0;
				attack = false;
				collider_enemy_player = false;
				max_enemys = 1;
				enemys.add(new Enemy());
				timer = 0;
				seconds_spaw_enemy = 2;
				score = 0;
				state_game = 1;
			}

		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		tNave.dispose();
		tMissil.dispose();
		for(int m = 0;m < max_enemys;m++) {
			Enemy enemy = enemys.get(m);
			enemy.get_texture().dispose();
		}
		tLogo.dispose();
		tGameOver.dispose();
		bitmap.dispose();
	}

	private void moveNave() {
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			if(posX < Gdx.graphics.getWidth()/2 - nave.getWidth()) {
				posX += spd;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			if(posX > 0) {
				posX -= spd;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			if(posY < Gdx.graphics.getHeight() - nave.getHeight()) {
				posY += spd;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			if(posY > 0) {
				posY -= spd;
			}
		}
	}

	private void moveMissile() {
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && attack == false && collider_enemy_player == false) {
			attack = true;
		}

		if(attack) {
			xMissile += 20;
		}else {
			xMissile = posX;
			yMissile = posY;
		}

		if(xMissile > Gdx.graphics.getWidth()) {
			attack = false;
		}
		
	}

	private void collider_bullet_enemy() {
		for(int m = 0;m < max_enemys;m++) {
			Enemy enemy = enemys.get(m);
			if(xMissile > enemy.get_X() && xMissile < enemy.get_X() + enemy.get_sprite().getWidth()) {
				if(yMissile > (enemy.get_Y() - enemy.get_sprite().getHeight()/2) && yMissile < (enemy.get_Y() - enemy.get_sprite().getHeight()/2) + enemy.get_sprite().getHeight() + 10) {
					if(attack) {
						attack = false;
						enemys.remove(enemy);
						max_enemys -= 1;
						score += 5;
					}
				}
			}
		}
	}

	private boolean collider_player_enemy() {
		for(int m = 0;m < max_enemys;m++) {
			Enemy enemy = enemys.get(m);
			if(posX + nave.getWidth() > enemy.get_X() && posX + nave.getWidth() < enemy.get_X() + enemy.get_sprite().getWidth()) {
				if(posY > (enemy.get_Y() - enemy.get_sprite().getHeight()/2) && posY < (enemy.get_Y() - enemy.get_sprite().getHeight()/2) + enemy.get_sprite().getHeight()) {
					state_game = 2;
					return true;
				}
			}
		}
		
		return false;
	}

}
