
package com.sankovicmarko.pacman;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class StateGame extends State {

    private int currentLevel;
    private int gameScore;
    private int livesRemaining;
    private boolean fruitAdded = false;

    private Maze maze;
    private Pacman pacman;

    AIGhosts ghostsAI;
    
    public StateGame(Game g) {
        super(g);
    }

    @Override
    public void reset() {
        // Resetovanje varijabli
        currentLevel = 0;
        gameScore = 0;
        livesRemaining = 2;
        // Prvi nivo
        respawn(true);
    }

    @Override
    public void logic() {
        Graphics2D g = getGame().getGraphicsContext();
        
        if(maze.getDotsRemaining() == 0) respawn(true);

        if(pacman.isDead()) lose();

        maze.paint(g);
        pacman.act();
        pacman.paint(g);

        // Dodaj trešnju na fiksnu poziciju nakon 70 pojedenih tačkica
        if(!fruitAdded && maze.getDotsEaten() == 70) {
            fruitAdded = maze.addItem(new Fruit(maze, 20, 13, 100, "com/sankovicmarko/pacman/sprites/cherry.png"));
        }

        // Logika duhova. Putanja, stanje i crtanje.
        ghostsAI.process();
        for(Ghost ghost : maze.getGhosts()) {
            ghost.act();
            ghost.paint(g);
        }

        // Iscrtaj rezultat
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, Maze.TILE_SIZE));
        g.drawString("NIVO", Maze.TILE_SIZE * 2, Maze.TILE_SIZE);
        g.drawString(String.valueOf(currentLevel), Maze.TILE_SIZE * 2, Maze.TILE_SIZE * 2);
        g.drawString("BROJ POENA", Maze.TILE_SIZE * 9, Maze.TILE_SIZE);
        g.drawString(String.valueOf(gameScore + pacman.getScore()), Maze.TILE_SIZE * 9, Maze.TILE_SIZE * 2);
        g.drawString("BROJ ŽIVOTA: " + String.valueOf(livesRemaining), Maze.TILE_SIZE * 2, Maze.TILE_SIZE * 35 + (int) Maze.TILE_SIZE / 2);
        g.drawString("NEPOJEDENIH", Maze.TILE_SIZE * 18, Maze.TILE_SIZE);
        g.drawString(String.valueOf(maze.getDotsRemaining()), Maze.TILE_SIZE * 18, Maze.TILE_SIZE * 2);
        if(pacman.isEnergized()) {
            g.setColor(Color.CYAN);
            g.drawString("ENERGIZED!", Maze.TILE_SIZE * 18, Maze.TILE_SIZE * 35 + (int) Maze.TILE_SIZE / 2);
        }
    }

    public void lose() {
        livesRemaining--;
        if(livesRemaining >= 0) respawn(false);
        else reset();
    }


    public void respawn(boolean nextLevel) {

        if(ghostsAI != null) {
            ghostsAI.clearReferences();
            ghostsAI = null;
        }
        
        if(nextLevel) {
            // Ako je sledeći nivo očisti sve reference.
            if(maze != null) {
                if(currentLevel != 0) gameScore += pacman.getScore();
                maze.clearReferences();
                maze = null;
            }
            
            maze = new Maze();
            pacman = maze.getPacman();
            
            currentLevel++;
            fruitAdded = false;
        } else {
            for(Ghost g : maze.getGhosts()) {
                g.move(g.getSpawnX(), g.getSpawnY());
                g.updatePath(null);
                g.setDead(false);
                if(!g.getName().equals("Blinky")) g.setMode(Ghost.MODE_JAIL);
            }
            
            pacman.setDead(false);
            pacman.move(pacman.getSpawnX(), pacman.getSpawnY());
        }

        ghostsAI = new AIGhosts(maze);
    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
                // ako igrač trenutno ne može u ovaj smer, čeka se dok se ne stvore uslovi da ide u smer nextDirection
                if(!maze.canMove(pacman, pacman.getX() - 1, pacman.getY())) pacman.setNextDirection(Actor.MOVE_UP);
                else {
                    pacman.setMoveDirection(Actor.MOVE_UP);
                    pacman.setNextDirection(Actor.MOVE_NONE); // ako se igrač predomisli ne menjaj mu smer na next
                }
            break;
            case KeyEvent.VK_RIGHT:
                // ako igrač trenutno ne može u ovaj smer, čeka se dok se ne stvore uslovi da ide u smer nextDirection
                if(!maze.canMove(pacman, pacman.getX(), pacman.getY() + 1)) pacman.setNextDirection(Actor.MOVE_RIGHT);
                else {
                    pacman.setMoveDirection(Actor.MOVE_RIGHT);
                    pacman.setNextDirection(Actor.MOVE_NONE); // ako se igrač predomisli ne menjaj mu smer na next
                }
            break;
            case KeyEvent.VK_DOWN:
                // ako igrač trenutno ne može u ovaj smer, čeka se dok se ne stvore uslovi da ide u smer nextDirection
                if(!maze.canMove(pacman, pacman.getX() + 1, pacman.getY())) pacman.setNextDirection(Actor.MOVE_DOWN);
                else {
                    pacman.setMoveDirection(Actor.MOVE_DOWN);
                    pacman.setNextDirection(Actor.MOVE_NONE); // ako se igrač predomisli ne menjaj mu smer na next
                }
            break;
            case KeyEvent.VK_LEFT:
                // ako igrač trenutno ne može u ovaj smer, čeka se dok se ne stvore uslovi da ide u smer nextDirection
                if(!maze.canMove(pacman, pacman.getX(), pacman.getY() - 1)) pacman.setNextDirection(Actor.MOVE_LEFT);
                else {
                    pacman.setMoveDirection(Actor.MOVE_LEFT);
                    pacman.setNextDirection(Actor.MOVE_NONE); // ako se igrač predomisli ne menjaj mu smer na next
                }
            break;
            case KeyEvent.VK_SPACE:
                pacman.setMoveDirection(Actor.MOVE_NONE);
            break;
        }
    }

}
