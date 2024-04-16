package sokoban_v8;

/**
 *
 * @author danie
 */

import java.awt.Image;
import java.util.HashMap;
import javax.swing.ImageIcon;

public class Player extends MapElement {
    private Image image;

    // Modify constructor to accept HashMap
    public Player(int x, int y, HashMap<String, Image> imageMap) {
        super(x, y);
        initPlayer(imageMap); // Pass the HashMap to initWall
    }
    
    private void initPlayer(HashMap<String, Image> imageMap) {
        image = imageMap.get("Player"); // Retrieve the Wall image from the HashMap
        setImage(image); // setImage sets the image for this element
    }
    
    public void move(int x, int y) {

        int dx = getX() + x;
        int dy = getY() + y;
        
        setX(dx);
        setY(dy);
    }
}