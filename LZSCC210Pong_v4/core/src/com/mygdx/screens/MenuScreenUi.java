package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.SpaceGame;
import com.mygdx.helpers.ScreenType;
import com.mygdx.audio.AudioManager;

public class MenuScreenUi extends ScreenAdapter {
    private Stage stage;
    private Skin skin;
    private Table table;
    private Texture backgroundTexture;

    public MenuScreenUi() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        backgroundTexture = new Texture(Gdx.files.internal("bg5.jpg"));
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));
        table = new Table();
        table.setFillParent(true);
        table.setBackground(backgroundDrawable);
        stage.addActor(table);
        Label titleLabel = new Label("Last in the void", skin);
        titleLabel.setFontScale(1.0f);
        TextButton playButton = new TextButton("Play", skin);
        TextButton creditsButton = new TextButton("Credits", skin);
        TextButton exitButton = new TextButton("Exit", skin);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SpaceGame.getInstance().changeScreen(MenuScreenUi.this, ScreenType.GAME);
            }
        });
        creditsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SpaceGame.getInstance().changeScreen(MenuScreenUi.this, ScreenType.CREDITS);
            }
        });
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SpaceGame.getInstance().exit(MenuScreenUi.this);
            }
        });
        Button musicButton = new Button(skin, "music");
        musicButton.setChecked(true);
        AudioManager.getInstance().playMusic();
        musicButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.getInstance().toggleMusic();
            }
        });
        table.top();
        table.add(titleLabel).padTop(50).center();
        table.row().padTop(100);
        table.add(playButton).width(200).height(60).pad(10);
        table.row();
        table.add(creditsButton).width(200).height(60).pad(10);
        table.row();
        table.add(exitButton).width(200).height(60).pad(10);
        table.row().padTop(50);
        table.add(musicButton).width(60).height(60).pad(10).center();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
    }
}
