
package com.sankovicmarko.pacman;

import com.sankovicmarko.pacman.pathfinding.Path;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ghost extends Actor implements Runnable {

    // Mod u kome se duh nalazi. Pogledati prateću dokumentaciju.
    protected byte mode;
    
    private String name = "";

    private Thread t;
    private boolean switchModes = true;
    protected static final int CHANGE_MODE_PERIOD = 7000;

    public final static byte MODE_JAIL        = 1;
    public final static byte MODE_CHASE       = 2;
    public final static byte MODE_SCATTER     = 3;
    public final static byte MODE_FRIGHTEN    = 4;

    // Duhove iscrtavamo dohvatajući odgovarajuće slike.
    // Duhovi imaju dve grafičke reprezentacije: normalnu i kada su u modu frighten, tj. kada beže od igrača.
    // Normalni sprite se zadaje u konstruktoru čime svaki duh može izgledati drugačije.
    protected Sprite sprite;
    protected String refNormal = "";
    protected static final String refFrighten = "com/sankovicmarko/pacman/sprites/frighten.png";
    
    // Putanja i koraci
    protected Path path;
    protected int nextStepIndex;
    private boolean needNewPath = true;

    private boolean debugPath = false;

    public Ghost(String name, Maze maze, int x, int y, String ref, boolean debugPath) {
        super(maze, x, y);
        if(ref != null) {
            refNormal = ref;
            this.sprite = SpriteManager.getInstance().getSprite(ref);
        }
        speed = .4f;
        this.name = name;
        this.debugPath = debugPath;
        t = new Thread(this);
        t.start();
    }

    public void run() {

        try {
            while(switchModes) {
                Thread.sleep(CHANGE_MODE_PERIOD);
                if(!isJailed() && !isFrightened()) {
                    if(isChasing()) setMode(Ghost.MODE_SCATTER);
                    else setMode(Ghost.MODE_CHASE);
                    updatePath(null);
                }
                System.out.println("New state: " + mode);
            }
            if(!switchModes) t = null;
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void kill() {
        switchModes = false;
    }

    public String getName() {
        return name;
    }

    public boolean isJailed() {
        return mode == MODE_JAIL;
    }

    public boolean isChasing() {
        return mode == MODE_CHASE;
    }

    public boolean isFrightened() {
        return mode == MODE_FRIGHTEN;
    }
    
    public boolean isScattered() {
        return mode == MODE_SCATTER;
    }

    public void setMode(byte mode) {
        this.mode = mode;
        if(mode == MODE_FRIGHTEN) sprite = SpriteManager.getInstance().getSprite(refFrighten);
        else sprite = SpriteManager.getInstance().getSprite(refNormal);
    }

    @Override
    public void act() {
        // Pomeri duha na naredni korak
        if(path != null && nextStepIndex < path.getLength()) {

            // Postavi smer kretanja u zavisnosti od narednog koraka
            setNextStepDirection();

            switch(moveDirection) {
                case MOVE_UP:
                    dx -= speed;
                    dy = 0;
                    if(Math.abs(dx) >= Maze.TILE_SIZE) {
                        dx = 0;
                        move(path.getX(nextStepIndex), path.getY(nextStepIndex));
                        nextStepIndex++;
                    }
                    break;
                case MOVE_RIGHT:
                    dx = 0;
                    dy += speed;
                    if(Math.abs(dy) >= Maze.TILE_SIZE) {
                        dy = 0;
                        move(path.getX(nextStepIndex), path.getY(nextStepIndex));
                        nextStepIndex++;
                    }
                    break;
                case MOVE_DOWN:
                    dx += speed;
                    dy = 0;
                    if(Math.abs(dx) >= Maze.TILE_SIZE) {
                        dx = 0;
                        move(path.getX(nextStepIndex), path.getY(nextStepIndex));
                        nextStepIndex++;
                    }
                    break;
                case MOVE_LEFT:
                    dx = 0;
                    dy -= speed;
                    if(Math.abs(dy) >= Maze.TILE_SIZE) {
                        dy = 0;
                        move(path.getX(nextStepIndex), path.getY(nextStepIndex));
                        nextStepIndex++;
                    }
                    break;
                default:
                    // Stoj u mestu.
                    dx = 0;
                    dy = 0;
                    break;
            }
        } else {
            needNewPath = true;
        }
    }

    @Override
    public void paint(Graphics2D g) {
        sprite.draw(g, positionY * Maze.TILE_SIZE - 6, positionX * Maze.TILE_SIZE - 6);

        if(path != null && debugPath) {
            for(int i = 0; i < path.getLength(); i++) {
                Path.Step s = path.getStep(i);
                g.setColor(Color.GRAY);
                g.drawLine(Maze.TILE_SIZE * s.getY(), Maze.TILE_SIZE * s.getX(), (Maze.TILE_SIZE * s.getY())+Maze.TILE_SIZE, (Maze.TILE_SIZE * s.getX())+Maze.TILE_SIZE);
            }
        }
    }

    /**
     * Nova putanja kojom će se duh kretati.
     */
    public void updatePath(Path p) {
        nextStepIndex = 1;

        path = p;
        needNewPath = false;
    }

    public boolean needNewPath() {
        return needNewPath;
    }

    private void setNextStepDirection() {
        // Nadji smer na osnovu narednog koraka.
        if((path.getY(nextStepIndex)-positionY) < 0) moveDirection = MOVE_LEFT;
        else if((path.getY(nextStepIndex)-positionY) > 0) moveDirection = MOVE_RIGHT;
        else if((path.getX(nextStepIndex)-positionX) > 0) moveDirection = MOVE_UP;
        else moveDirection = MOVE_DOWN;
    }
}
