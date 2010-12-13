package com.sankovicmarko.pacman;


import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * Menadžer sprajtova. Učitava i vodi računa o tome da se samo jedna instanca istog resursa
 * na osnovu putanje učitava.
 */
public class SpriteManager {
    /** Singleton */
    private static SpriteManager single = new SpriteManager();

    /**
     * Dohvati instancu menadžera sprajtova.
     *
     */
    public static SpriteManager getInstance() {
        return single;
    }

    /** Keširani sprajtovi. Ključ je referenca na sprite(url, putanja na fs), a vrednost Sprite. */
    private HashMap sprites = new HashMap();

    /**
     * Dohvati sprajt
     */
    public Sprite getSprite(String ref) {
        // ako je sprajt u kešu dohvati
        if(sprites.get(ref) != null) {
            return (Sprite) sprites.get(ref);
        }
        
        BufferedImage sourceImage = null;

        try {
            URL url = this.getClass().getClassLoader().getResource(ref);

            if(url == null) {
                fail("Slika ne postoji: " + ref);
            }
            sourceImage = ImageIO.read(url);
        } catch (IOException e) {
            fail("Ne mogu da učitam: " + ref);
        }

        // ubrzana slika (koristi lokalni device)
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);

        // Iscrtaj sliku.
        image.getGraphics().drawImage(sourceImage, 0, 0, null);

        // Kreiraj novi sprite, dodaj ga u cache i vrati.
        Sprite sprite = new Sprite(image);
        sprites.put(ref, sprite);

        return sprite;
    }

    private void fail(String message) {
        // Ako nema slike za sprite, nema ni igre!!
        System.err.println(message);
        System.exit(0);
    }
}
