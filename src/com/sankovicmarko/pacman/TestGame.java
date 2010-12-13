
package com.sankovicmarko.pacman;

import com.sankovicmarko.pacman.pathfinding.AStarPathFinder;
import com.sankovicmarko.pacman.pathfinding.ManhattanHeuristic;
import com.sankovicmarko.pacman.pathfinding.Path;
import com.sankovicmarko.pacman.pathfinding.Path.Step;

public class TestGame {
    public static void main(String[] argv) {
        Maze m = new Maze();
        AStarPathFinder finder = new AStarPathFinder(m, new ManhattanHeuristic());
        Path findPath = finder.findPath(m.getPacman(), 26, 14, 17, 19);
        for(Step s : findPath.getSteps()) {
            System.out.println(s.getX() + " " + s.getY());
        }
    }
}
