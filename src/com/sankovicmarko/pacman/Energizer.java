
package com.sankovicmarko.pacman;

import java.awt.Color;
import java.awt.Graphics2D;

public class Energizer extends Item implements Runnable {

    private Thread t;
    private boolean animate = true;

    // Crna i originalna boja se smenjuju na zadati broj milisekundi.
    private boolean isColorized = true;

    public Energizer(Maze maze, int x, int y) {
        super(maze, x, y, 50);
        t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
            while(animate) {
                Thread.sleep(200);
                isColorized = !isColorized;
            }
            if(!animate) t = null;
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void act() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void paint(Graphics2D g) {
        // Izmenom boje u zadatom intervalu postiže se animacija.
        //if(isColorized) g.setColor(new Color(248, 176, 144));
        if(isColorized) g.setColor(Color.CYAN);
        else g.setColor(Color.BLACK);
        g.fillOval(positionX * Maze.TILE_SIZE, positionY * Maze.TILE_SIZE, Maze.TILE_SIZE, Maze.TILE_SIZE);
    }

    public boolean use(Pacman p) {
        // Uvećaj rezultat igrača.
        p.increaseScore(points);
        // Promeni stanje igrača čime se omogućava da igrač pojede duhove
        // i dobije poene. Pojedeni duhovi se vraćaju u kućicu.
        p.setEnergized(true);
        return true;
    }

    public void kill() {
        animate = false;
    }
}
