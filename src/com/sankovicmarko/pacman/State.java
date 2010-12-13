package com.sankovicmarko.pacman;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Stanja igre.
 */
public abstract class State implements KeyListener {
	
        // Stanja
	public static final int STATE_EXITING = 1;
	
	protected Game game;
	
	public State(Game g) {
            game = g;
            reset();
	}
	
	public Game getGame() {
            return game;
	}
	
	/**
	 * Resetovanje stanja igre.
	 */
	public abstract void reset();
	
	/**
	 * Logika petlje stanja.
	 */
	public abstract void logic();
	
	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
            // Esc
            switch(e.getKeyChar()) {
                    case 27:
                        game.requestChangeState(STATE_EXITING);
                        break;
                    default:
                        break;
            }
	}
}