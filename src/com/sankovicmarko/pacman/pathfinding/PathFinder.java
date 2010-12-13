package com.sankovicmarko.pacman.pathfinding;

/**
 * Implementacija PathFinder-a je proizvoljna.
 * Svaki PathFinder treba da implementira ovaj interfejs i da definiše metod findPath.
 */
public interface PathFinder {
    public Path findPath(Mover mover, int sx, int sy, int tx, int ty);
}
