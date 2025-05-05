package com.mygdx.ui;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable; 
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap; 
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.helpers.FancyFontHelper;
import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import com.mygdx.pong.PongGame;


/**
 * Event UI component that displays the event popup dialog
 * with its description and choices.
 */
public class EventUI {
    private Stage stage;
    private Table mainTable;
    private Table choicesTable;
    private Texture backgroundTexture;
    private NinePatchDrawable panelBackground;
    private Event currentEvent;
    private Player player;
    private boolean isVisible = false;
    private EventCompletionListener completionListener;
    private ObjectMap<TextButton, Drawable> originalButtonBackgrounds = new ObjectMap<>();

    // Colors for styling
    private final Color TITLE_COLOR = new Color(0.9f, 0.6f, 0.2f, 1f); // Gold/amber color
    private final Color TEXT_COLOR = new Color(0.9f, 0.9f, 0.9f, 1f); // Almost white
    private final Color BUTTON_COLOR = new Color(0.2f, 0.3f, 0.7f, 1f); // Blue
    private final Color BUTTON_HOVER_COLOR = new Color(0.3f, 0.5f, 0.9f, 1f); // Lighter blue
    private final Color SUCCESS_COLOR = new Color(0.2f, 0.8f, 0.2f, 1f); // Green
    private final Color FAILURE_COLOR = new Color(0.8f, 0.2f, 0.2f, 1f); // Red

     // Interface for notifying when an event is completed.     
    public interface EventCompletionListener {
        void onEventCompleted();
    }

    public EventUI(Player player, EventCompletionListener listener) {
        this.player = player;
        this.completionListener = listener;

        stage = new Stage(new ScreenViewport());

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();
        backgroundTexture = new Texture(pixmap);
        pixmap.dispose();

        createPanelBackground();

        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        setVisible(false);
    }

    private void createPanelBackground() {
        Pixmap pixmap = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.05f, 0.05f, 0.2f, 0.9f);
        pixmap.fill();
        pixmap.setColor(0.2f, 0.4f, 0.8f, 1f);
        pixmap.drawRectangle(0, 0, 3, 3);
        Texture borderTexture = new Texture(pixmap);
        pixmap.dispose();
        NinePatch ninePatch = new NinePatch(borderTexture, 1, 1, 1, 1);
        panelBackground = new NinePatchDrawable(ninePatch);
    }

    public void showEvent(Event event) {
        this.currentEvent = event;
        originalButtonBackgrounds.clear(); // Clear stored backgrounds from previous event

        mainTable.clear();
        mainTable.clearActions();
        mainTable.getColor().a = 1f;

        int windowWidth = PongGame.getInstance().getWindowWidth();

        Table dialogBox = new Table();
        dialogBox.setBackground(panelBackground);

        BitmapFont titleFont = FancyFontHelper.getInstance().getFont(TITLE_COLOR, 32);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, TITLE_COLOR);
        Label titleLabel = new Label(event.getTitle(), titleStyle);
        titleLabel.setAlignment(Align.center);
        dialogBox.add(titleLabel).pad(20).expandX().fillX().row();

        BitmapFont descFont = FancyFontHelper.getInstance().getFont(TEXT_COLOR, 20);
        Label.LabelStyle descStyle = new Label.LabelStyle(descFont, TEXT_COLOR);
        Label descLabel = new Label(event.getDescription(), descStyle);
        descLabel.setWrap(true);
        descLabel.setAlignment(Align.center);
        dialogBox.add(descLabel).width(windowWidth * 0.7f).pad(20).expandX().fillX().row();

        choicesTable = new Table();
        for (int i = 0; i < event.getChoices().size(); i++) {
            final int choiceIndex = i;
            Event.Choice choice = event.getChoices().get(i);

            BitmapFont buttonFont = FancyFontHelper.getInstance().getFont(TEXT_COLOR, 18);
            TextButton.TextButtonStyle buttonStyle = createButtonStyle(buttonFont);

            TextButton choiceButton = new TextButton(choice.getText(), buttonStyle);
            originalButtonBackgrounds.put(choiceButton, buttonStyle.up); // Store the original background

            choiceButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    handleChoice(choiceIndex);
                }
            });

            choiceButton.addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    if (pointer == -1) { // Only react to mouse hover, not touch
                        TextButton button = (TextButton) event.getListenerActor();
                        button.getLabel().setColor(Color.WHITE);
                        button.getStyle().up = button.getStyle().over; // Use hover background
                    }
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                     if (pointer == -1) { // Only react to mouse hover, not touch
                        TextButton button = (TextButton) event.getListenerActor();
                        button.getLabel().setColor(TEXT_COLOR);
                        Drawable originalBg = originalButtonBackgrounds.get(button);
                        if (originalBg != null) {
                            button.getStyle().up = originalBg;
                        }
                    }
                }
            });

            choicesTable.add(choiceButton).pad(10).expandX().fillX().row();
        }

        dialogBox.add(choicesTable).pad(20).expandX().fillX();

        mainTable.add(dialogBox).width(windowWidth * 0.8f);

        dialogBox.getColor().a = 0f; // Dialog box start invisible
        dialogBox.setOrigin(Align.center);
        dialogBox.setScale(0.8f);
        dialogBox.addAction(Actions.sequence(
                Actions.fadeIn(3f), // Fade in the dialog box with 3 seconds duration
                Actions.scaleTo(1f, 1f, 0.3f, Interpolation.swingOut)
        ));

        setVisible(true);
        Gdx.input.setInputProcessor(stage);
    }

    private TextButton.TextButtonStyle createButtonStyle(BitmapFont font) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.fontColor = TEXT_COLOR;

        Pixmap pixmapUp = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapUp.setColor(BUTTON_COLOR);
        pixmapUp.fill();
        Texture textureUp = new Texture(pixmapUp);

        Pixmap pixmapOver = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapOver.setColor(BUTTON_HOVER_COLOR);
        pixmapOver.fill();
        Texture textureOver = new Texture(pixmapOver);

        Pixmap pixmapDown = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapDown.setColor(BUTTON_COLOR.cpy().mul(0.8f));
        pixmapDown.fill();
        Texture textureDown = new Texture(pixmapDown);

        // Store drawables associated with this style instance
        style.up = new NinePatchDrawable(new NinePatch(textureUp, 0, 0, 0, 0));
        style.over = new NinePatchDrawable(new NinePatch(textureOver, 0, 0, 0, 0));
        style.down = new NinePatchDrawable(new NinePatch(textureDown, 0, 0, 0, 0));

        // Dispose Pixmaps, Textures will be managed by Drawables/NinePatch
        pixmapUp.dispose();
        pixmapOver.dispose();
        pixmapDown.dispose();


        return style;
    }

    private void handleChoice(int choiceIndex) {
        if (currentEvent == null || player == null) return; 

        Event.Choice choice = currentEvent.getChoices().get(choiceIndex);
        boolean success = choice.execute(player);
        String outcomeMessage = success ? currentEvent.getSuccessMessage() : currentEvent.getFailureMessage();

        clearChoiceUI();
        displayOutcomeUI(success, outcomeMessage);
    }

    
     // Clears the choices table and resets stored button backgrounds.
    private void clearChoiceUI() {
        choicesTable.clear(); // Clear the table holding previous buttons
        originalButtonBackgrounds.clear(); // Clear stored backgrounds
    }

    private void displayOutcomeUI(boolean success, String outcomeMessage) {
        Label resultLabel = createResultLabel(success);
        TextButton continueButton = createContinueButton(); // Create button before message label if message depends on it

        choicesTable.add(resultLabel).pad(20).expandX().fillX().row();

        // Only add the message label and scroll pane if there's a message
        if (outcomeMessage != null && !outcomeMessage.isEmpty()) {
            addOutcomeMessage(outcomeMessage);
        }

        choicesTable.add(continueButton).pad(10).width(200).height(50).expandX().center();

        // Add animation to the result label
        resultLabel.addAction(Actions.sequence(
                Actions.scaleTo(1.2f, 1.2f, 0.2f),
                Actions.scaleTo(1f, 1f, 0.2f)
        ));
    }

    private Label createResultLabel(boolean success) {
        Color color = success ? SUCCESS_COLOR : FAILURE_COLOR;
        String text = success ? "Success!" : "Failed!";

        BitmapFont resultFont = FancyFontHelper.getInstance().getFont(color, 24);
        Label.LabelStyle resultStyle = new Label.LabelStyle(resultFont, color);
        Label resultLabel = new Label(text, resultStyle);
        resultLabel.setAlignment(Align.center);
        return resultLabel;
    }

     private void addOutcomeMessage(String outcomeMessage) {
         BitmapFont messageFont = FancyFontHelper.getInstance().getFont(TEXT_COLOR, 18);
         Label.LabelStyle messageStyle = new Label.LabelStyle(messageFont, TEXT_COLOR);
         Label messageLabel = new Label(outcomeMessage, messageStyle);
         messageLabel.setWrap(true);
         messageLabel.setAlignment(Align.center);

         Table scrollTable = new Table();
         scrollTable.add(messageLabel).width(PongGame.getInstance().getWindowWidth() * 0.6f).pad(10);

         ScrollPane scrollPane = new ScrollPane(scrollTable); // Pass the table, not the label directly
         scrollPane.setFadeScrollBars(false);
         scrollPane.setScrollingDisabled(true, false); // Allow only vertical scrolling

         choicesTable.add(scrollPane).maxHeight(200).expandX().fillX().pad(10).row();
     }

    private TextButton createContinueButton() {
        BitmapFont buttonFont = FancyFontHelper.getInstance().getFont(TEXT_COLOR, 18);
        TextButton.TextButtonStyle buttonStyle = createButtonStyle(buttonFont); 
        TextButton continueButton = new TextButton("Continue", buttonStyle);

        originalButtonBackgrounds.put(continueButton, buttonStyle.up); 

        // Listener to hide the event
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideEvent();
            }
        });

        // Listener for hover effects 
        addHoverListener(continueButton); 

        return continueButton;
    }

    private void addHoverListener(final TextButton button) {
         button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) { // Mouse hover
                    button.getLabel().setColor(Color.WHITE);
                    button.getStyle().up = button.getStyle().over; // Use hover background
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                 if (pointer == -1) { // Mouse hover
                    button.getLabel().setColor(TEXT_COLOR);
                    Drawable originalBg = originalButtonBackgrounds.get(button);
                    if (originalBg != null) {
                        button.getStyle().up = originalBg; // Restore original background
                    }
                }
            }
        });
    }


    public void hideEvent() {
        if (!isVisible) return;

        mainTable.addAction(Actions.sequence(
                Actions.fadeOut(0.3f),
                Actions.run(() -> {
                    mainTable.clearActions();
                    if (choicesTable != null) {
                        choicesTable.clear();
                    }
                    mainTable.clear();
                    currentEvent = null;
                    setVisible(false);
                    originalButtonBackgrounds.clear(); // Clear stored backgrounds on hide

                    mainTable.getColor().a = 1f;

                    if (completionListener != null) {
                        completionListener.onEventCompleted();
                    }
                })
        ));
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
        mainTable.setVisible(visible);

        if (!visible) {
            // Let GameScreen handle input processor reset
        } else {
            Gdx.input.setInputProcessor(stage);
        }
    }

    public void render() {
        if (isVisible) {
             stage.getBatch().begin();
             stage.getBatch().setColor(1, 1, 1, mainTable.getColor().a);
             stage.getBatch().draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
             stage.getBatch().setColor(1, 1, 1, 1);
             stage.getBatch().end();

            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        if (panelBackground != null && panelBackground.getPatch() != null) {
             // Dispose the texture used by the NinePatch if it's not shared/managed elsewhere
            try {
                 panelBackground.getPatch().getTexture().dispose();
            } catch (Exception e) {
                 // Handle potential errors if texture is already disposed
                 System.err.println("Error disposing panel background texture: " + e.getMessage());
            }
        }
    }
}