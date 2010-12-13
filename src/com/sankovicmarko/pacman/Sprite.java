
package com.sankovicmarko.pacman;

import java.awt.Graphics;
import java.awt.Image;

public class Sprite {
    
    private Image image;

    /**
     * Kreiranje novog sprite-a na osnovu slike
     *
     * @param image
     */
    public Sprite(Image image) {
        this.image = image;
    }

    /**
     * Dohvati širinu
     *
     * @return Širina izražena u pikselima
     */
    public int getWidth() {
        return image.getWidth(null);
    }

    /**
     * Dohvati visinu
     *
     * @return Visina izražena u pikselima
     */
    public int getHeight() {
        return image.getHeight(null);
    }

    /**
     * Iscrtavanje slike na zadatu grafiku (Graphics)
     *
     * @param g
     * @param x
     * @param y
     */
    public void draw(Graphics g, int x, int y) {
        g.drawImage(image, x, y, null);
    }
}
