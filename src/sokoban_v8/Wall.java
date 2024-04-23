package sokoban_v8;

/**
 *
 * @author danie
 */

import java.awt.Image;
import java.util.HashMap;
import javax.swing.ImageIcon;

public class Wall extends MapElement {
    private Image image;

    // constructor setting up the HashMap
    public Wall(int x, int y, HashMap<String, Image> imageMap) {
        super(x, y);
        initWall(imageMap); // Pass the HashMap to initWall
    }
    
    private void initWall(HashMap<String, Image> imageMap) {
        image = imageMap.get("Wall"); // Retrieve the Wall image from the HashMap
        setImage(image); // setImage sets the image for this element
    }
    
    @Override
    public void interact() {
        System.out.println("Wall is at coordinates: " + getX() + ", " + getY());
    }
}
