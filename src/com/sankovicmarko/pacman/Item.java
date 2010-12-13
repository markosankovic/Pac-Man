
package com.sankovicmarko.pacman;

/**
 * Item reprezentuje sve sa čim igrač ima kontakta u toku igre
 * i što mu može doneti poene. Misli se na: dot, energizer, fruits
 */
public abstract class Item extends GameObject implements UsableGameObject {
    // Većina predmeta igraču donosi odgovarajući broj poena.
    protected int points = 0;

    public Item(Maze maze, int x, int y, int points) {
        super(maze, x, y);
        this.points = points;
    }
}
