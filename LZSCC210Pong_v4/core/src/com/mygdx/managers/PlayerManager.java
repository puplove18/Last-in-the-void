package com.mygdx.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.helpers.BodyHelper;
import com.mygdx.helpers.Constants;
import com.mygdx.helpers.ContactType;
import com.mygdx.helpers.FancyFontHelper;
import com.mygdx.objects.Inventory;
import com.mygdx.objects.Player;
import com.mygdx.objects.SpaceShip;
import com.mygdx.pong.PongGame;

/**
 * Manages the player state, ship, stats, and inventory
 */
public class PlayerManager {
    private Player player;
    private Inventory inventory;
    private SpaceShip playerShip;
    
    // Fonts for player stats 
    private BitmapFont statsFontWhite;
    private BitmapFont statsFontGreen;
    private BitmapFont statsFontYellow;
    private BitmapFont statsFontRed;
    
    public PlayerManager(Player player, Inventory inventory, World world) {
        this.player = player;
        this.inventory = inventory;
        
        // Initialize space ship
        Body shipBody = BodyHelper.createRectangularBody(
                PongGame.getInstance().getWindowWidth() / 2,
                PongGame.getInstance().getWindowHeight() / 2,
                Constants.PLAYER_PADDLE_WIDTH,
                Constants.PLAYER_PADDLE_HEIGHT,
                BodyType.KinematicBody, 1f, world, ContactType.PLAYER);
        
        this.playerShip = new SpaceShip(
                PongGame.getInstance().getWindowWidth() / 2,
                PongGame.getInstance().getWindowHeight() / 2,
                shipBody);
        
        initializeFonts();
    }
    
    private void initializeFonts() {
        FancyFontHelper fontHelper = FancyFontHelper.getInstance();
        this.statsFontWhite = fontHelper.getFont(Color.WHITE, 16);
        this.statsFontGreen = fontHelper.getFont(Color.GREEN, 16);
        this.statsFontYellow = fontHelper.getFont(Color.YELLOW, 16);
        this.statsFontRed = fontHelper.getFont(Color.RED, 16);
    }
    
    public void update() {
        playerShip.update();
    }
    
    public void renderShip(SpriteBatch batch) {
        playerShip.render(batch);
    }
    
    public void renderPlayerStats(SpriteBatch batch) {
        float statsX = 10;
        float statsY = PongGame.getInstance().getWindowHeight() - 20;
        double currentHealth = player.getHealth();
        double currentFuel = player.getFuel();
        double currentOxygen = player.getOxygen();

        // Use the pre-initialized white font
        statsFontWhite.draw(batch, "Name: Space Explorer", statsX, statsY);

        // Select the correct pre-initialized font based on health color
        Color healthColor = getResourceColor(currentHealth);
        BitmapFont healthFont;
        if (healthColor.equals(Color.GREEN)) {
            healthFont = statsFontGreen;
        } else if (healthColor.equals(Color.YELLOW)) {
            healthFont = statsFontYellow;
        } else {
            healthFont = statsFontRed;
        }
        healthFont.draw(batch, "Health: " + (int) currentHealth + "%", statsX, statsY - 20);

        // Select the correct pre-initialized font based on fuel color
        Color fuelColor = getResourceColor(currentFuel);
        BitmapFont fuelFont;
        if (fuelColor.equals(Color.GREEN)) {
            fuelFont = statsFontGreen;
        } else if (fuelColor.equals(Color.YELLOW)) {
            fuelFont = statsFontYellow;
        } else {
            fuelFont = statsFontRed;
        }
        fuelFont.draw(batch, "Fuel: " + (int) currentFuel + "%", statsX, statsY - 40);

        // Select the correct pre-initialized font based on oxygen color
        Color oxygenColor = getResourceColor(currentOxygen);
        BitmapFont oxygenFont;
        if (oxygenColor.equals(Color.GREEN)) {
            oxygenFont = statsFontGreen;
        } else if (oxygenColor.equals(Color.YELLOW)) {
            oxygenFont = statsFontYellow;
        } else {
            oxygenFont = statsFontRed;
        }
        oxygenFont.draw(batch, "Oxygen: " + (int) currentOxygen + "%", statsX, statsY - 60);
    }
    
    private Color getResourceColor(double value) {
        if (value > 70)
            return Color.GREEN;
        else if (value > 30)
            return Color.YELLOW;
        else
            return Color.RED;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public Inventory getInventory() {
        return inventory;
    }
    
    public SpaceShip getPlayerShip() {
        return playerShip;
    }
    
    public void dispose() {
        if (statsFontWhite != null) statsFontWhite.dispose();
        if (statsFontGreen != null) statsFontGreen.dispose();
        if (statsFontYellow != null) statsFontYellow.dispose();
        if (statsFontRed != null) statsFontRed.dispose();
    }
}
