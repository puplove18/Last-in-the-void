package com.mygdx.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.mygdx.helpers.FancyFontHelper;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class ShowStuff  {
    private Table showStuffTable;
    private Label showStuffLabel;
    private Stage stage;

    public ShowStuff(Stage stage) {
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        showStuffTable = new Table();
        showStuffTable.setBackground(createshowStuffBackground());
        showStuffTable.setVisible(false);
        showStuffTable.setTouchable(Touchable.disabled);

        showStuffLabel = new Label("", new Label.LabelStyle(
            FancyFontHelper.getInstance().getFont(Color.WHITE, 14), Color.WHITE
        ));
        showStuffTable.add(showStuffLabel).pad(5);

        stage.addActor(showStuffTable);
    }

    private NinePatchDrawable createshowStuffBackground() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.1f, 0.1f, 0.1f, 0.9f);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        NinePatch patch = new NinePatch(texture, 0, 0, 0, 0);
        return new NinePatchDrawable(patch);
    }

    public void show(String text, float x, float y) {
        showStuffLabel.setText(text);
        showStuffTable.pack();

        float posX = x + 10;
        float posY = y + 10;

        if (posX + showStuffTable.getWidth() > stage.getWidth()) {
            posX = stage.getWidth() - showStuffTable.getWidth() - 5;
        }
        if (posY + showStuffTable.getHeight() > stage.getHeight()) {
            posY = y - showStuffTable.getHeight() - 10;
        }

        showStuffTable.setPosition(posX, posY);
        showStuffTable.setVisible(true);
        showStuffTable.toFront();
    }

    public void hide() {
        showStuffTable.setVisible(false);
    }

}
