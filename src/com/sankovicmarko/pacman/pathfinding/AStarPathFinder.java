package com.sankovicmarko.pacman.pathfinding;

import java.util.ArrayList;
import java.util.Collections;

public class AStarPathFinder implements PathFinder {

    // Set nodova koji su pretraženi
    private ArrayList<Node> closed = new ArrayList<Node>();
    // Set nodova koji nisu u potpunosti pretraženi
    private ArrayList<Node> open = new ArrayList<Node>();

    // Mapa, nodovi i heuristic objekat
    private TileBasedMap map;
    private Node[][] nodes;
    private AStarHeuristic heuristic;

    /**
     * AStarPathFinder konstruktor.
     *
     * @param heuristic Aproksimacija cene koštanja putanje od ovog čvora do krajnjeg
     * @param map Mapa koja se pretražuje
     */
    public AStarPathFinder(TileBasedMap map, AStarHeuristic heuristic) {
        this.map = map;
        this.heuristic = heuristic;

        // Instanciraj nodove na osnovu broja kvadrata po širini i visini u mapi
        nodes = new Node[map.getHeightInTiles()][map.getWidthInTiles()];
        for(int x=0; x<nodes.length; x++) {
            for(int y=0; y<nodes[x].length; y++) {
                nodes[x][y] = new Node(x, y);
            }
        }
    }

    /**
     * Unutrašnja klasa koja reprezentuje jedan čvor prilikom traženja putanje.
     */
    private static class Node implements Comparable<Node> {

        private int x; // x koordinata čvora
        private int y; // y koordinata čvora
        private float cost; // cena putanje od početnog čvora do ovog
        private Node parent; // prethodni roditeljski čvor
        private float heuristic; // cena putanje od tekućeg čvora do krajnjeg
        private int depth; // dubina, za jedan veće od roditeljskog noda

        private Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int setParent(Node parent) {
            depth = parent.depth + 1;
            this.parent = parent;
            return depth;
        }

        public int compareTo(Node o) {
            float f = heuristic + cost;
            float of = o.heuristic + o.cost;

            if(f < of) return -1;
            else if(f > of) return 1;
            else return 0;
        }

    }

    public Path findPath(Mover mover, int sx, int sy, int tx, int ty) {
        // Prva provera - krajnji čvor nije dostupan.
        if(map.blocked(mover, tx, ty)) return null;

        // Inicijalno stanje A* alogoritma. Grupa closed je prazna.
        // Početni kvadrat je u otvorenoj listi. Njegova cena je 0 jer smo već tu.
        nodes[sx][sy].cost = 0;
        nodes[sx][sy].depth = 0;
        closed.clear();
        open.clear();
        open.add(nodes[sx][sy]);

        nodes[tx][ty].parent = null;

        while(!open.isEmpty()) {
            // Dohvati prvi node iz otvorene liste. To je node sa najmanjim f().
            Node current = open.get(0);
            // Ako je to krajnji čvor zaustavljamo pretragu jer smo stigli do odredišta.
            if(current == nodes[tx][ty]) break;

            // Prebacujemo iz otvorene u zatvorenu listu i proveravamo okolne čvorove.
            open.remove(current);
            closed.add(current);

            // Pretraži sve okolne čvore.
            for(int x=-1; x<2; x++) {
                for(int y=-1; y<2; y++) {
                    // Preskoči ako je tekući čvor.
                    if((x == 0) && (y == 0)) continue;
                    // Nedozvoljavamo dijagonalno kretanje.
                    if((x != 0) && (y != 0)) continue;
                    // Koordinate okolnog čvora.
                    int nx = x + current.x;
                    int ny = y + current.y;

                    // Ako je potencijalna lokacija validna.
                    if(isValidLocation(mover, sx, sy, nx, ny)) {
                        // Cena narednog koraka je zbir cene tekućeg koraka i narednog.
                        float nextStepCost = current.cost + getMovementCost(mover, current.x, current.y, nx, ny);
                        Node neighbour = nodes[nx][ny];
                        
                        // Ako je cena narednog koraka manja nego što je ranije bila, tačku ne treba odbaciti i treba je ponovo proveriti.
                        if(nextStepCost < neighbour.cost) {
                            if(open.contains(neighbour)) open.remove(neighbour);
                            if(closed.contains(neighbour)) closed.remove(neighbour);
                        }

                        // Ako tačka nije proverena/odbačena postavlja se i dodaje se kao naredni korak u otvorenu listu.
                        if(!open.contains(neighbour) && !closed.contains(neighbour)) {
                            neighbour.cost = nextStepCost;
                            neighbour.heuristic = getHeuristicCost(mover, nx, ny, tx, ty);
                            neighbour.setParent(current);
                            open.add(neighbour);
                            // Ukoliko nije proveravan, naredni korak se dodaje u otvorenu listu.
                            // Sortiranje na osnovu pregažene compareTo metode u klasi Node. Postiže se da čvor sa najmanjim f() bude na početku, čime se favorizuju kvadrati bliži cilju.
                            Collections.sort(open);
                        }
                    }
                }
            }
        }

        // Putanja nije nadjena.
        if(nodes[tx][ty].parent == null) return null;

        // Polazeći od odredišnog čvora uz upotrebu parent reference možemo formirati putanju.
        Path path = new Path();
        Node target = nodes[tx][ty];
        while(target != nodes[sx][sy]) {
            path.prependStep(target.x, target.y);
            target = target.parent;
        }
        path.prependStep(sx, sy);

        return path;
    }

    /**
     * Provera validnosti narednog koraka.
     */
    protected boolean isValidLocation(Mover mover, int sx, int sy, int x, int y) {
        boolean invalid = (x < 0) || (y < 0) || (x >= map.getHeightInTiles()) || (y >= map.getWidthInTiles());

        if ((!invalid) && ((sx != x) || (sy != y))) {
            invalid = map.blocked(mover, x, y);
        }

        return !invalid;
    }

    /**
     * Cena koraka. Zavisi od tipa kvadrata.
     */
    public float getMovementCost(Mover mover, int sx, int sy, int tx, int ty) {
        return map.getCost(mover, sx, sy, tx, ty);
    }

    /**
     * Heuristic cena. Ovim se odredjuje redosled proveravanja.
     */
    public float getHeuristicCost(Mover mover, int x, int y, int tx, int ty) {
        return heuristic.getCost(map, mover, x, y, tx, ty);
    }
}
