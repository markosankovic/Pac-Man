package com.sankovicmarko.pacman.pathfinding;

/**
 * Interfejs za sve aproksimacije cene od tekuće tačke do krajnje.
 */
public interface AStarHeuristic {
    public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty);
}
