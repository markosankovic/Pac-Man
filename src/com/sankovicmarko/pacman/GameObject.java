
package com.sankovicmarko.pacman;

import java.awt.Graphics2D;

public abstract class GameObject {

    // Tekuća pozicija objekta. Pozicija se odredjuje kvadratom u kojem se tekući objekat nalazi.
    public int positionX; // pozicija x se odnosi na red
    public int positionY; // pozicija y se odnosi na kolonu

    // Referenca na lavirint. Objekti postoje unutar lavirinta.
    protected final Maze maze;

    public GameObject(Maze maze, int x, int y) {
        this.maze = maze;
        this.positionX = x;
        this.positionY = y;
    }

    public int getX() {
        return positionX;
    }

    public int getY() {
        return positionY;
    }

    public abstract void act();

    public abstract void paint(Graphics2D g);
}
