
package com.sankovicmarko.pacman;

import java.awt.Color;
import java.awt.Graphics2D;

public class Dot extends Item {

    public Dot(Maze maze, int x, int y) {
        super(maze, x, y, 10);
    }

    @Override
    public void act() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void paint(Graphics2D g) {
        g.setColor(new Color(248, 176, 144));
        g.fillRect(positionX * Maze.TILE_SIZE + 6, positionY * Maze.TILE_SIZE + 6, 4, 4);
    }

    public boolean use(Pacman p) {
        p.increaseScore(points);
        return true;
    }
    
}
