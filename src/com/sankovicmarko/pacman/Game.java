
package com.sankovicmarko.pacman;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends Canvas implements Runnable {
    
    Thread t = new Thread(this);

    /** Ova klasa omogućava korišćenje 'page flipping' čime se ubrzava iscrtavanje,
     * tako što se prethodno spremljeni buffer u jednom koraku,
     * bilo kopiranjem podataka ili pomeranjem video pokazivača,
     * prebacuje na ekran.
     */
    private BufferStrategy strategy;

    /**
     * Stanja
     */
    private State currentState;
    private boolean changeStateRequested;
    private int requestedState;

    /** True ako je igra u petlji */
    private boolean gameRunning = true;

    public Game() {
        // JFrame - kontejner za igru.
        JFrame container = new JFrame("Pac-Man - Projekat 2 - JAVA - Sankovic Marko NRT-89/03");

        // Dohtavanje sadržaja igre i postavljanje rezolucije.
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(448,576));
        panel.setLayout(null);

        // Postavljanje veličine platna(canvas) i stavljanja istog u sadržaj kontejnera.
        setBounds(0, 0, 448, 576);
        panel.add(this);

        // Tekuće stanje.
        currentState = new StateGame(this);
        // Svako stanje na odgovarajući način upravlja pritiskom na tastere. State implementira KeyListener.
        container.addKeyListener(currentState);

        // Crtanje se izvršava kroz metod page-flipping, pa iz to razloga ignorišemo AWT repaint.
        setIgnoreRepaint(true);

        // Učiniti prozor vidljivim.
        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        // Listener kojim se zatvara aplikacija.
        container.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Kreiranje BufferStrategy.
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
    }

    public Graphics2D getGraphicsContext() {
        return (Graphics2D) strategy.getDrawGraphics();
    }

    private void gameLoop() {
        t.start();
    }
    
    void requestChangeState(int state) {
        requestedState = state;
        changeStateRequested = true;
    }

    public static void main(String[] argv) {
        Game g = new Game();
        g.gameLoop();
    }

    public void run() {
        try {
            while (gameRunning) {
                Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

                // Logika stanja se izvršava u petlji.
                currentState.logic();

                // Završeno crtanje, očistiti grafiku i obrnuti buffer.
                g.dispose();
                strategy.show();
                Thread.sleep(1);
            }
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
