import GUI.MainFrame;
import enums.CONNECTION;

import javax.swing.*;
import java.io.PrintStream;

/**
 * @dev :   devpar
 * @date :   05-Jun-2021
 */
public class MainClass {
    private static final PrintStream out;
    private static MainFrame mainFrame;

    static {
        out = System.out;
    }

    public static void main(final String $[]) {
        out.println("main");
        SwingUtilities.invokeLater(() -> {
            mainFrame = new MainFrame();
        });
    }
}
