
package com.sankovicmarko.pacman;

import java.awt.Graphics2D;

public class Teleport extends Item {

    private int moveX;
    private int moveY;
    private int enterDirection;

    public Teleport(Maze maze, int x, int y, int points) {
        super(maze, x, y, 0); // teleport ne donosi poene
    }

    public void setMoveDirection(int x, int y) {
        this.moveX = x;
        this.moveY = y;
    }

    public void setEnterDirection(int dir) {
        enterDirection = dir;
    }

    @Override
    public void act() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void paint(Graphics2D g) {
        // Teleport je nevidljiv
    }

    public boolean use(Pacman p) {
        if(p.moveDirection == enterDirection) {
            p.move(moveX, moveY);
        }
        return false;
    }
    
}
