
package com.sankovicmarko.pacman;

import com.sankovicmarko.pacman.pathfinding.AStarPathFinder;
import com.sankovicmarko.pacman.pathfinding.ManhattanHeuristic;
import com.sankovicmarko.pacman.pathfinding.PathFinder;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Logika duhova. Biranje putanje, upravljanje stanjem...
 * @author marko
 */
public class AIGhosts {

    Maze maze;
    Pacman pacman;
    protected ArrayList<Ghost> ghosts;
    protected PathFinder finder;
    private long nextReleaseTime;

    protected static final int JAIL_TIME = 6000;
    
    public AIGhosts(Maze maze) {
        this.maze = maze;
        pacman = maze.getPacman();
        ghosts = maze.getGhosts();
        finder = new AStarPathFinder(maze, new ManhattanHeuristic());
        nextReleaseTime = System.currentTimeMillis() + AIGhosts.JAIL_TIME;
    }

    public void process() {
        
        // ako je igrač energized, onda su duhovi u strahu
        boolean fear = false;
        if(maze.getPacman().isEnergized()) fear = true;

        // Oslobodi duha ako je isteklo vreme za oslobadjanje
        if(System.currentTimeMillis() > nextReleaseTime) {
            for(Ghost ghost : ghosts) {
                if(ghost.isJailed()) {
                    ghost.setMode(Ghost.MODE_CHASE);
                    boolean move = ghost.move(14, 12);
                    nextReleaseTime = System.currentTimeMillis() + AIGhosts.JAIL_TIME;
                    break;
                }

            }
        }

        for(Ghost ghost : ghosts) {

            // Ako je duh pojeden od strane igrača.
            if(ghost.isDead()) {
                pacman.increaseScore(100);
                ghost.updatePath(null);
                ghost.setDead(false);
                ghost.setMode(Ghost.MODE_JAIL);
                if(ghost.getName().equals("Blinky")) {
                    ghost.move(16, 12);
                } else {
                    ghost.move(ghost.getSpawnX(), ghost.getSpawnY());
                }
            }

            // Postavi duhove na frighten ukoliko je igrač energized
            if(!ghost.isJailed()) {
                if(!ghost.isFrightened() && fear) {
                    ghost.updatePath(null);
                    ghost.setMode(Ghost.MODE_FRIGHTEN);
                }
                if(!fear) {
                    ghost.setMode(Ghost.MODE_CHASE);
                }
            }

            // Nova putanja na osnovu stanja.
            if(ghost.needNewPath()) {
                if(ghost.isChasing()) {
                    ghost.updatePath(finder.findPath(ghost, ghost.getX(), ghost.getY(), pacman.getX(), pacman.getY()));
                } else if(ghost.isScattered() || ghost.isFrightened()) {
                    Point p = maze.getRandomAccessiblePoint();
                    ghost.updatePath(finder.findPath(ghost, ghost.getX(), ghost.getY(), (int) p.getX(), (int) p.getY()));
                }
            }
        }

    }

    public void clearReferences() {
        maze = null;
        for(Ghost g : ghosts) {
            g.kill();
        }
        ghosts = null;
        finder = null;
        pacman = null;
    }
}
