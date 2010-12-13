
package com.sankovicmarko.pacman;

import java.awt.Color;
import java.awt.Graphics2D;

public class Pacman extends Actor {

    private int score = 0;
    private boolean isEnergized = false;
    private long energizedExpireTime;
    private int nextDirection = Actor.MOVE_NONE;

    public Pacman(Maze maze, int x, int y) {
        super(maze, x, y);
        speed = .6f;
    }

    @Override
    public void act() {

        Ghost g = maze.getGhost(positionX, positionY);
        if(g != null) {
            if(!isEnergized) {
                setDead(true);
                    return;
            } else {
                g.setDead(true);
            }
        }

        if(isEnergized && System.currentTimeMillis() > energizedExpireTime) {
            setEnergized(false);
        }
        
        Item item = maze.getItem(positionX, positionY);
        boolean itemDestroy = false;
        if(item != null) itemDestroy = item.use(this);
        if(itemDestroy) maze.removeItem(positionX, positionY);



        // Based on the direction, increment the movement delta and set the appropriate orientation
        // The delta's represent the screen position (in pixels) since the last official change in position on the grid
        // When a delta in a certain direction passes the TILE_SIZE, the object can change position in the map grid. This makes for smooth transitions between tiles

        // Provera da li je postavljen naredni smer kretanja i preusmeravanje Pac-Man u zadati smer.
        // Naredni smer kretanja se postavlja ako igrač u trenutku pritiska na taster ne može da usmeri Pac-Man u željeni pravac.
        // Taj pravac se pamti, a tekući pravac se menja na zapamćeni ukoliko se stvori uslov(slobodna ćelija)
        if(nextDirection != Actor.MOVE_NONE) {
            switch(nextDirection) {
                case Actor.MOVE_UP:
                    if(maze.canMove(this, positionX - 1, positionY)) {
                        this.setMoveDirection(nextDirection);
                        nextDirection = Actor.MOVE_NONE;
                    }
                    break;
                case Actor.MOVE_RIGHT:
                    if(maze.canMove(this, positionX, positionY + 1)) {
                        this.setMoveDirection(nextDirection);
                        nextDirection = Actor.MOVE_NONE;
                    }
                    break;
                case Actor.MOVE_DOWN:
                    if(maze.canMove(this, positionX + 1, positionY)) {
                        this.setMoveDirection(nextDirection);
                        nextDirection = Actor.MOVE_NONE;
                    }
                    break;
                case Actor.MOVE_LEFT:
                    if(maze.canMove(this, positionX, positionY - 1)) {
                        this.setMoveDirection(nextDirection);
                        nextDirection = Actor.MOVE_NONE;
                    }
                    break;
            }
        }

        switch(moveDirection) {
                case Actor.MOVE_UP:
                    if(maze.canMove(this, positionX - 1, positionY)) {
                        dx -= speed;
                        dy = 0;
                        if(Math.abs(dx) >= Maze.TILE_SIZE) {
                            dx = 0;
                            move(positionX - 1, positionY);
                        }
                    }
                    moveOrientation = 90;
                    break;
                case Actor.MOVE_RIGHT:
                    if(maze.canMove(this, positionX, positionY + 1)) {
                        dx = 0;
                        dy += speed;
                        if(Math.abs(dy) >= Maze.TILE_SIZE) {
                            dy = 0;
                            move(positionX, positionY + 1);
                        }
                    }
                    moveOrientation = 0;
                    break;
                case Actor.MOVE_DOWN:
                    if(maze.canMove(this, positionX + 1, positionY)) {
                        dx += speed;
                        dy = 0;
                        if(Math.abs(dx) >= Maze.TILE_SIZE) {
                            dx = 0;
                            move(positionX + 1, positionY);
                        }
                    }
                    moveOrientation = -90;
                    break;

                case Actor.MOVE_LEFT:
                    if(maze.canMove(this, positionX, positionY - 1)) {
                        dx = 0;
                        dy -= speed;
                        if(Math.abs(dy) >= Maze.TILE_SIZE) {
                            dy = 0;
                            move(positionX, positionY - 1);
                        }
                    }
                    moveOrientation = 180;
                    break;
                default:
                    // MOVE_NONE (stand still)
                    dx = 0;
                    dy = 0;
                    break;
        }
    }

    @Override
    public void paint(Graphics2D g) {

        int moveX = (int) (positionX * Maze.TILE_SIZE - 6 + dx);
        int moveY = (int) (positionY * Maze.TILE_SIZE - 6 + dy);

        g.setColor(Color.yellow);

        // Animacija usta. Kada je na pola ćelije otvoriti usta, zatvoriti na početku nove.
        if((Math.abs(dx) >= Maze.TILE_SIZE/2) || Math.abs(dy) >= Maze.TILE_SIZE/2) {
            g.fillArc(moveY, moveX, 28, 28, 0 + moveOrientation, 360);
        } else {
            g.fillArc(moveY, moveX, 28, 28, 45 + moveOrientation, 270);
        }
    }

    public boolean canMove(int dir) {
        boolean rt = false;
        switch(dir) {
                case Actor.MOVE_UP: rt = maze.canMove(this, positionX - 1, positionY);
                case Actor.MOVE_RIGHT: rt = maze.canMove(this, positionX, positionY + 1);
                case Actor.MOVE_DOWN: rt = maze.canMove(this, positionX + 1, positionY);
                case Actor.MOVE_LEFT: rt = maze.canMove(this, positionX, positionY - 1);
        }
        return rt;
    }

    /**
     * Povećaj tekući rezultat. Važi u toku jedne faze, s obzirom da se
     * igrač ponovo instancira. Rezultat se pre instanciranja(respawn)
     * prepisuje u gameScore GameState objekta.
     * 
     * @param points
     */
    public void increaseScore(int points) {
        score += points;
    }

    public int getScore() {
        return score;
    }

    /**
     * Kada je energized igrač se kreće brže u zadatom periodu.
     * Takodje, igrač može da pojede duhove čime dobija dodatne poene.
     *
     * @param energized
     */
    public void setEnergized(boolean energized) {
        if(energized) {
            isEnergized = true;
            energizedExpireTime = System.currentTimeMillis() + 6000; // energized u narednih 10 sekundi
            speed = .8f;
        } else {
            isEnergized = false;
            speed = .6f;
        }
    }

    public boolean isEnergized() {
        return isEnergized;
    }

    public void setNextDirection(int dir) {
        nextDirection = dir;
    }
}
