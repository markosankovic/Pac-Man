package com.sankovicmarko.pacman.pathfinding;

public class ManhattanHeuristic implements AStarHeuristic {

    /**
     * Rastojanje izmedju dve tačke je zbir apsolutnih razlika njihov koordinata.
     * http://en.wikipedia.org/wiki/Taxicab_geometry
     *
     * @param map
     * @param mover
     * @param x
     * @param y
     * @param tx
     * @param ty
     * @return
     */
    public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty) {
        return Math.abs(x-tx) + Math.abs(y-ty);
    }
    
}
