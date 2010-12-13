/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sankovicmarko.pacman;

import com.sankovicmarko.pacman.pathfinding.Mover;

/**
 * Actor reprezentuje igrača(Pac-Man) i neprijatelje(duhove).
 */
public abstract class Actor extends GameObject implements Mover {

    // Konstante smerova u kojima se kreću akteri.
    public static final int MOVE_NONE = 0;
    public static final int MOVE_UP = 1;
    public static final int MOVE_RIGHT = 2;
    public static final int MOVE_DOWN = 4;
    public static final int MOVE_LEFT = 8;

    // Stanja
    protected boolean isDead;

    // Kretanje, lokacija, smer, brzina
    protected int spawnX;
    protected int spawnY;
    protected int moveDirection = MOVE_NONE;
    protected int moveOrientation = 0;
    protected float dx = 0;
    protected float dy = 0;
    protected float speed = 0.4f;

    public Actor(Maze maze, int x, int y) {
        super(maze, x, y);
        spawnX = x;
        spawnY = y;
    }

    public int getSpawnX() {
        return spawnX;
    }

    public int getSpawnY() {
        return spawnY;
    }

    /**
     * Izmena smera kretanja.
     * @param dir jedan od smerova tipa: MOVE_NONE, MOVE_UP, MOVE_RIGHT, MOVE_DOWN, MOVE_LEFT
     */
    void setMoveDirection(int dir) {
        moveDirection = dir;
    }

    public void setDead(boolean s) {
        isDead = s;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Pokušaj pomeranja na zadatu x, y lokaciju. Proverava se lavirint.
     * Akteri nisu u mogućnosti da se pomere na zadatu lokaciju
     * ukoliko se na njoj nalazi zid.
     *
     * @param x 
     * @param y 
     * @return true ukoliko je pomeraj moguć, u suprotnom false
     */
    public boolean move(int x, int y) {
        boolean res = maze.canMove(this, x, y);
        if(res) {
            positionX = x;
            positionY = y;
        }
        //System.out.println("x: " + positionX + ", y: " + positionY);
        return res;
    }
}
