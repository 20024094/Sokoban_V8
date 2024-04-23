package sokoban_v8;

/**
 *
 * @author danie
 */

import java.awt.Image;
import java.util.HashMap;
import javax.swing.ImageIcon;

public class Crate extends MapElement {
    private Image image;

    // constructor setting up HashMap
    public Crate(int x, int y, HashMap<String, Image> imageMap) {
        super(x, y);
        initCrate(imageMap); // Pass the HashMap to initWall
    }
    
    private void initCrate(HashMap<String, Image> imageMap) {
        image = imageMap.get("Crate"); // Retrieve the Wall image from the HashMap
        setImage(image); // setImage sets the image for this element
    }
    
    public void move(int x, int y) {
        
        int dx = getX() + x;
        int dy = getY() + y;
        
        setX(dx);
        setY(dy);
    }
    
     @Override
    public void interact() {
        System.out.println("Crate is at coordinates: " + getX() + ", " + getY());
    }
}