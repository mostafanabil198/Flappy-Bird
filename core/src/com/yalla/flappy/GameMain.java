package com.yalla.flappy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import helpers.GameManager;
import scenes.GamePlay;
import scenes.MainMenu;

public class GameMain extends Game {
    SpriteBatch batch;
    AdHandler adHandler;

    public GameMain(AdHandler adHandler) {
        this.adHandler = adHandler;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        GameManager.getInstance().initializeGameData();
        GameManager.getInstance().setAdHandler(adHandler);
        setScreen(new MainMenu(this));
    }

    @Override
    public void render() {
        super.render();
    }


    public SpriteBatch getBatch() {
        return this.batch;
    }
}
