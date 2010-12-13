package com.sankovicmarko.pacman.pathfinding;

/**
 * Interfejs koji implementiraju mape nad kojima se određuje putanja(pathfinding).
 */
public interface TileBasedMap {
	/**
	 * Broj pločica po širini.
	 */
	public int getWidthInTiles();

	/**
	 * Broj pločica po visini.
         */
	public int getHeightInTiles();
	
	/**
	 * Provera da li je pločica blokirana.
         */
	public boolean blocked(Mover mover, int x, int y);
	
	/**
	 * Cena koštanja kretanja kroz određenu pločicu.
	 */
	public float getCost(Mover mover, int sx, int sy, int tx, int ty);
}
