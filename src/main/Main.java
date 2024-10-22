package main;

import java.awt.EventQueue;

import jdbc.DDL;
import ui.UI;

/**
 * The type Main.
 * * @author Izan Gómez de la Fuente
 *  * @author Javier Agudo Fernández
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     *
     */
    public static void main(String[] args) {
        DDL.getConnection();
        EventQueue.invokeLater(() -> {
            try {
                UI frame = new UI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}