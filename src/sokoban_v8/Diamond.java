package sokoban_v8;

/**
 *
 * @author danie
 */

import java.awt.Image;
import java.util.HashMap;
import javax.swing.ImageIcon;

public class Diamond extends MapElement {
    private Image image;

    // constructor setting up the HashMap
    public Diamond(int x, int y, HashMap<String, Image> imageMap) {
        super(x, y);
        initDiamond(imageMap); // Pass the HashMap to initWall
    }
    
    private void initDiamond(HashMap<String, Image> imageMap) {
        image = imageMap.get("Diamond"); // Retrieve the Diamond image from the HashMap
        setImage(image); // setImage sets the image for this element
    }
}
