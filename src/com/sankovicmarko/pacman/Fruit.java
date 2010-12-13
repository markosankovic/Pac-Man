
package com.sankovicmarko.pacman;

import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

public class Fruit extends Item {

    // Sprite(slika) koja grafički predstavlja ovaj objekat.
    Sprite sprite;

    // Timer kojim se automatski uklanja ovaj objekat.
    private Timer timer;
    // Trajanje objekta.
    private long timeout = 10000; // vreme u milisekundama, podrazumevano 10 sekundi

    public Fruit(Maze maze, int x, int y, int points, String ref) {
        super(maze, x, y, points);
        sprite = SpriteManager.getInstance().getSprite(ref);
        timer = new Timer();
        timer.schedule(new FruitTimerTask(), timeout);
    }

    // Namena ove ugnježdene klase je da na nakon isteka vremena(timeout)
    // ukloni ovaj objekat iz lavirinta.
    private class FruitTimerTask extends TimerTask {
        @Override
        public void run() {
            maze.removeItem(positionX, positionY);
        }
    }

    public void setTimeout(long to) {
        timeout = to;
    }

    @Override
    public void act() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void paint(Graphics2D g) {
        sprite.draw(g, positionY * Maze.TILE_SIZE, positionX * Maze.TILE_SIZE);
    }

    public boolean use(Pacman p) {
        p.increaseScore(points);
        return true;
    }

}
