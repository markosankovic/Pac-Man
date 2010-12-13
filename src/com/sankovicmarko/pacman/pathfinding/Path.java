package com.sankovicmarko.pacman.pathfinding;

import java.util.ArrayList;

public class Path {

    // Niz koraka koji čine ovu putanju
    private ArrayList steps = new ArrayList();

    public Path() {}

    /**
     * Broj koraka
     */
    public int getLength() {
        int len = 0;
        if(steps != null) len = steps.size();
        return len;
    }

    /**
     * Dohvatiti korak na indexu.
     */
    public Step getStep(int index) {
        return (Step) steps.get(index);
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    /**
     * Dohvatiti x koordiantu za korak po indexu.
     */
    public int getX(int index) {
        return getStep(index).x;
    }

    /**
     * Dohvatiti y koordiantu za korak po indexu.
     */
    public int getY(int index) {
        return getStep(index).y;
    }

    /**
     * Dodati korak na kraj putanje.
     */
    public void appendStep(int x, int y) {
        steps.add(new Step(x,y));
    }

    /**
     * Dodati korak na početak putanje.
     */
    public void prependStep(int x, int y) {
        steps.add(0, new Step(x, y));
    }

    /**
     * Da li korak postoji
     */
    public boolean contains(int x, int y) {
        return steps.contains(new Step(x,y));
    }

    /**
     * Korak unutar putanje.
     */
    public class Step {

        private int x;
        private int y;

        public Step(int x, int y) {
                this.x = x;
                this.y = y;
        }

        public int getX() {
                return x;
        }

        public int getY() {
                return y;
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Step) {
                Step o = (Step) other;
                return (o.x == x) && (o.y == y);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + this.x;
            hash = 97 * hash + this.y;
            return hash;
        }
    }
}
