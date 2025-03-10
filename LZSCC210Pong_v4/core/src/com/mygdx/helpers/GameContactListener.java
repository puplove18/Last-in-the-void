package com.mygdx.helpers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.screens.GameScreen;

/**
 * This class takes care of contact events between physics bodies in the game
 */
public class GameContactListener implements ContactListener {

   private GameScreen gameScreen;

   public GameContactListener(GameScreen gameScreen) {
      this.gameScreen = gameScreen;
   }

   @Override
   public void beginContact(Contact contact) {
      // Will be used for spaceship collisions with planets, items, etc.
   }

   @Override
   public void endContact(Contact contact) {
      Fixture a = contact.getFixtureA();
      Fixture b = contact.getFixtureB();

      if (a == null || b == null)
         return;

      if (a.getUserData() == null || b.getUserData() == null)
         return;
      
      // Handle different contact types here
   }

   @Override
   public void preSolve(Contact contact, Manifold oldManifold) {
      // Pre-collision handling
   }

   @Override
   public void postSolve(Contact contact, ContactImpulse impulse) {
      // Post-collision handling
   }

   // More methods here to check contact types
   
   // This method checks whether the player ship is involved
   private boolean playerContact(Fixture a, Fixture b) {
      return a.getUserData() == ContactType.PLAYER || b.getUserData() == ContactType.PLAYER;
   }
}
