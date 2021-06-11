package fai.utb.gui;

import java.awt.*;

/**
 * For run the application run this main class
 *
 * @author Šimon Zouvala
 */
public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
