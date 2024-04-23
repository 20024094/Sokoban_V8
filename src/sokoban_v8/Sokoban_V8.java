package sokoban_v8;

/**
 *
 * @author danie
 */

import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class Sokoban_V8 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sokoban");
            Game sokobanGame = new Game();
            
            // Polymorphic state
            Game game = new Game();
            game.printAllElementStatuses(); // This will print the positions of all game elements
            
            // Use the methods from Game class to determine the size needed
            int frameWidth = sokobanGame.getMaxLevelWidth() + sokobanGame.OFFSET * 2;
            int frameHeight = sokobanGame.getMaxLevelHeight() + sokobanGame.OFFSET * 2 + frame.getInsets().top;

            frame.setPreferredSize(new Dimension(frameWidth, frameHeight));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.pack(); // Adjust the frame size to fit the preferred size and layouts of its subcomponents
            frame.setLocationRelativeTo(null); // Center the frame
            frame.add(sokobanGame);
            frame.setVisible(true);
        });
    }
}