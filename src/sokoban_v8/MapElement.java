package sokoban_v8;

/**
 *
 * @author danie
 */

import java.awt.Image;

public abstract class MapElement {

    private final int SPACE = 17;

    private int x;
    private int y;
    private Image image;
    
    // An abstract method to be overridden
    public abstract void interact();

    public MapElement(int x, int y) {
        
        this.x = x;
        this.y = y;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image img) {
        image = img;
    }

    public int getX() {
        
        return x;
    }

    public int getY() {
        
        return y;
    }

    public void setX(int x) {
        
        this.x = x;
    }

    public void setY(int y) {
        
        this.y = y;
    }

    public boolean isLeftCollision(MapElement elementLeftCollision) {
        
        return getX() - SPACE == elementLeftCollision.getX() && getY() == elementLeftCollision.getY();
    }

    public boolean isRightCollision(MapElement elementRightCollision) {
        
        return getX() + SPACE == elementRightCollision.getX() && getY() == elementRightCollision.getY();
    }

    public boolean isTopCollision(MapElement elementTopCollision) {
        
        return getY() - SPACE == elementTopCollision.getY() && getX() == elementTopCollision.getX();
    }

    public boolean isBottomCollision(MapElement elementBottomCollision) {
        
        return getY() + SPACE == elementBottomCollision.getY() && getX() == elementBottomCollision.getX();
    }
}