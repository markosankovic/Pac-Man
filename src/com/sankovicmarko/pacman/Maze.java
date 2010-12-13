
package com.sankovicmarko.pacman;

import com.sankovicmarko.pacman.pathfinding.Mover;
import com.sankovicmarko.pacman.pathfinding.TileBasedMap;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Maze implements TileBasedMap {

    // Tipovi objekata u lavirintu.
    public static final byte OBJECT_NONE = 0;
    public static final byte OBJECT_WALL = 1;
    public static final byte OBJECT_DOT = 2;
    public static final byte OBJECT_ENERGIZER = 3;
    public static final byte OBJECT_TELEPORT = 4;

    // Broj redova i kolona lavirinta.
    public final static int TILE_GRID_ROWS = 36;
    public final static int TILE_GRID_COLS = 28;

    // Veličina stranice kvadrata u pikselima.
    public final static int TILE_SIZE = 16;

    public final static int TOTAL_DOTS = 244;
    private int dotsRemaining = Maze.TOTAL_DOTS;

    // Igrač, predmeti i duhovi.
    private Pacman pacman;
    private Item items[][] = new Item[36][28];
    private ArrayList<Ghost> ghosts;

    private ArrayList<Point> accesiblePoints;

    private Thread energizerAnimThread;

    public Maze() {
        // Kreiraj i postavi Pacman-a.
        pacman = new Pacman(this, 26, 14);

        // Kreiraj duhove.
        ghosts = new ArrayList<Ghost>();

        Ghost blinky = new Ghost("Blinky", this, 14, 12, "com/sankovicmarko/pacman/sprites/blinky.png", false);
        blinky.setMode(Ghost.MODE_CHASE);
        blinky.setSpeed(0.4f);

        Ghost pinky = new Ghost("Pinky", this, 16, 15, "com/sankovicmarko/pacman/sprites/pinky.png", false);
        pinky.setMode(Ghost.MODE_JAIL);
        pinky.setSpeed(0.4f);

        Ghost inky = new Ghost("Inky", this, 18, 12, "com/sankovicmarko/pacman/sprites/inky.png", false);
        inky.setMode(Ghost.MODE_JAIL);
        inky.setSpeed(0.4f);

        Ghost clyde = new Ghost("Clyde", this, 18, 15, "com/sankovicmarko/pacman/sprites/clyde.png", false);
        clyde.setMode(Ghost.MODE_JAIL);
        clyde.setSpeed(0.4f);

        ghosts.add(blinky);
        ghosts.add(pinky);
        ghosts.add(inky);
        ghosts.add(clyde);

        // Instanciraj sve predmete.
        for(int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                switch(data[i][j]) {
                    case Maze.OBJECT_DOT:
                        items[i][j] = new Dot(this, j, i);
                        break;
                    case Maze.OBJECT_ENERGIZER:
                        items[i][j] = new Energizer(this, j, i);
                        break;
                    case Maze.OBJECT_TELEPORT:
                        items[i][j] = new Teleport(this, j, i, 0);
                        break;
                }
            }
        }

        // Kreiraj dva teleporta i postavi ih na fiksnu lokaciju.
        Teleport teleport1 = (Teleport) getItem(17, 0);
        teleport1.setEnterDirection(Actor.MOVE_LEFT);
        teleport1.setMoveDirection(17, 27);
        Teleport teleport2 = (Teleport) getItem(17, 27);
        teleport2.setEnterDirection(Actor.MOVE_RIGHT);
        teleport2.setMoveDirection(17, 0);

        accesiblePoints = new ArrayList<Point>();
        for(int i = 4; i < 33; i++) {
            for(int j = 1; j < 27; j++) {
                if(this.canMove(null, i, j)) accesiblePoints.add(new Point(i, j));
            }
        }
    }
    
    // Niz nizova kojim se definiše lavirint.
    // Vrednosti odgovaraju tipovima objekata.
    private byte[][] data = {
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,2,2,2,2,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,2,2,2,2,1},
        {1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1},
        {1,3,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,3,1},
        {1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1},
        {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
        {1,2,1,1,1,1,2,1,1,2,1,1,1,1,1,1,1,1,2,1,1,2,1,1,1,1,2,1},
        {1,2,1,1,1,1,2,1,1,2,1,1,1,1,1,1,1,1,2,1,1,2,1,1,1,1,2,1},
        {1,2,2,2,2,2,2,1,1,2,2,2,2,1,1,2,2,2,2,1,1,2,2,2,2,2,2,1},
        {1,1,1,1,1,1,2,1,1,1,1,1,0,1,1,0,1,1,1,1,1,2,1,1,1,1,1,1},
        {1,1,1,1,1,1,2,1,1,1,1,1,0,1,1,0,1,1,1,1,1,2,1,1,1,1,1,1},
        {1,1,1,1,1,1,2,1,1,0,0,0,0,0,0,0,0,0,0,1,1,2,1,1,1,1,1,1},
        {1,1,1,1,1,1,2,1,1,0,1,1,1,1,1,1,1,1,0,1,1,2,1,1,1,1,1,1},
        {1,1,1,1,1,1,2,1,1,0,1,0,0,0,0,0,0,1,0,1,1,2,1,1,1,1,1,1},
        {4,0,0,0,0,0,2,0,0,0,1,0,0,0,0,0,0,1,0,0,0,2,0,0,0,0,0,4},
        {1,1,1,1,1,1,2,1,1,0,1,0,0,0,0,0,0,1,0,1,1,2,1,1,1,1,1,1},
        {1,1,1,1,1,1,2,1,1,0,1,1,1,1,1,1,1,1,0,1,1,2,1,1,1,1,1,1},
        {1,1,1,1,1,1,2,1,1,0,0,0,0,0,0,0,0,0,0,1,1,2,1,1,1,1,1,1},
        {1,1,1,1,1,1,2,1,1,0,1,1,1,1,1,1,1,1,0,1,1,2,1,1,1,1,1,1},
        {1,1,1,1,1,1,2,1,1,0,1,1,1,1,1,1,1,1,0,1,1,2,1,1,1,1,1,1},
        {1,2,2,2,2,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,2,2,2,2,1},
        {1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1},
        {1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1},
        {1,3,2,2,1,1,2,2,2,2,2,2,2,0,0,2,2,2,2,2,2,2,1,1,2,2,3,1},
        {1,1,1,2,1,1,2,1,1,2,1,1,1,1,1,1,1,1,2,1,1,2,1,1,2,1,1,1},
        {1,1,1,2,1,1,2,1,1,2,1,1,1,1,1,1,1,1,2,1,1,2,1,1,2,1,1,1},
        {1,2,2,2,2,2,2,1,1,2,2,2,2,1,1,2,2,2,2,1,1,2,2,2,2,2,2,1},
        {1,2,1,1,1,1,1,1,1,1,1,1,2,1,1,2,1,1,1,1,1,1,1,1,1,1,2,1},
        {1,2,1,1,1,1,1,1,1,1,1,1,2,1,1,2,1,1,1,1,1,1,1,1,1,1,2,1},
        {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    };



    /**
     * Iscrtaj lavirint.
     * @param g
     */
    public void paint(Graphics2D g) {
        // Dohvati pozadinu i iscrtaj.
        Sprite background = SpriteManager.getInstance().getSprite("com/sankovicmarko/pacman/sprites/maze.png");
        background.draw(g, 0, 0);
        // Iscrtaj sve predmete.
        for(int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if(items[i][j] != null) items[i][j].paint(g);
            }
        }
    };

    /**
     * Dohvati preostali broj tačaka.
     * @return
     */
    public int getDotsRemaining() {
        return dotsRemaining;
    }

    /**
     * Dohvati broj pojedenih tačaka.
     * @return
     */
    public int getDotsEaten() {
        return Maze.TOTAL_DOTS - dotsRemaining;
    }

    /**
     * Provera da li Mover može da se pomeri u traženu ćeliju.
     * Pomeraj je moguć samo ukoliko ćelija nije tipa WALL.
     *
     * @param actor
     * @param x
     * @param y
     * @return
     */
    boolean canMove(Mover mover, int x, int y) {
        // Proveriti granice.
        if(x < 0 || y < 0 || x >= Maze.TILE_GRID_ROWS || y >= Maze.TILE_GRID_COLS) return false;
        // Ako je ćelija na koju se ide tipa WALL Actor ne može da se pomeri u tu ćeliju.
        if(data[x][y] == Maze.OBJECT_WALL) return false;
        // U ostalim slučajevima Actor može da se premesti u zahtevanu ćeliju.
        return true;
    }

    /**
     * Dohvati Pac-Man.
     * @return
     */
    public Pacman getPacman() {
        return pacman;
    }

    public Point getRandomAccessiblePoint() {
        int rand = (int) Math.round(Math.random() * (accesiblePoints.size() - 1));
        return accesiblePoints.get(rand);
    }

    /**
     * Dodaj predmet.
     * Pre dodavanja predmet mora imati zadate pozicije.
     * Vraćam true ukoliko sam dodao predmet u suprotnom false.
     *
     * @param item
     * @return
     */
    public boolean addItem(Item item) {
        if(item == null) return false;
        // Proveri granice.
        if(item.getX() < 0 || item.getY() < 0 || item.getX() >= Maze.TILE_GRID_ROWS || item.getY() >= Maze.TILE_GRID_COLS) return false;
        items[item.getX()][item.getY()] = item;
        return true;
    }

    /**
     * Ukloni predmet na poziciji.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean removeItem(int x, int y) {
        boolean removed = false;
        if(items[x][y] != null) {
            if(items[x][y] instanceof Dot || items[x][y] instanceof Energizer) dotsRemaining--;
            items[x][y] = null;
            removed = true;
        }
        return removed;
    }

    /**
     * Dohvati predmet na poziciji.
     * 
     * @param x
     * @param y
     * @return
     */
    public Item getItem(int x, int y) {
        return items[x][y];
    }

    /**
     * Dohvati duha na zadatoj poziciji.
     * 
     * @param positionX
     * @param positionY
     * @return
     */
    public Ghost getGhost(int positionX, int positionY) {
        Ghost g = null;
        for(Ghost ghost : ghosts) {
            if(ghost.getX() == positionX && ghost.getY() == positionY) {
                g = ghost; break;
            }
        }
        return g;
    }

    /**
     * Dohvati sve duhove.
     * @return
     */
    public ArrayList<Ghost> getGhosts() {
        return ghosts;
    }

    public int getWidthInTiles() {
        return Maze.TILE_GRID_COLS;
    }

    public int getHeightInTiles() {
        return Maze.TILE_GRID_ROWS;
    }

    public boolean blocked(Mover mover, int x, int y) {
        return !this.canMove(mover, x, y);
    }

    public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
        // Svaki pomeraj u lavirintu košta 1. Ovo je posledica toga da nemamo teren tipa: voda, vazduh itd
        // Duhovi se kreću hodnicima lavirinta gde je cena kretanja za svaki kvadrat ista.
        return 1;
    }

    public void clearReferences() {
        
        pacman = null;
        accesiblePoints = null;

        for(Ghost g : ghosts) {
            g.kill();
        }
        ghosts = null;

        for(int i=0; i<items.length; i++) {
            for(int j=0; j<items[i].length; j++) {
                if(items[i][j] instanceof Energizer) {
                    ((Energizer) items[i][j]).kill();
                }
            }
        }
        items = null;
    }
}
