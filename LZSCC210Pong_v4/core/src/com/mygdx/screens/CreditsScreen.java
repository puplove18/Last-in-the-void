package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.pong.PongGame;
import com.mygdx.helpers.ScreenType;

public class CreditsScreen extends ScreenAdapter {
    private Stage stage;
    private Skin skin;
    private Table table;

    public CreditsScreen() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel = new Label("Credits", skin);
        titleLabel.setFontScale(2.0f);
        Label creditsLabel = new Label("Developed by Group 2\nMusic by ArtistName\nAssets by LibGDX\nThank you for playing!", skin);
        creditsLabel.setWrap(true);
        creditsLabel.setAlignment(Align.center);
        creditsLabel.setWidth(400);
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PongGame.getInstance().changeScreen(CreditsScreen.this, ScreenType.MENU);
            }
        });

        table.top();
        table.add(titleLabel).padTop(50).center();
        table.row().padTop(50);
        table.add(creditsLabel).width(400).center();
        table.row().padTop(50);
        table.add(backButton).width(200).height(60).pad(10).center();
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
    }
}
