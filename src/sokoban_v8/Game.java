package sokoban_v8;

/**
 *
 * @author danie
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.util.HashMap;

public class Game extends JPanel {

    protected final int OFFSET = 16;
    private final int SPACE = 17;
    private final int LEFT_COLLISION = 1;
    private final int RIGHT_COLLISION = 2;
    private final int TOP_COLLISION = 3;
    private final int BOTTOM_COLLISION = 4;

    private Image backgroundImage;
    
    protected HashMap<String, Image> imageMap;

    private ArrayList<Wall> wall;
    private ArrayList<Crate> crate;
    private ArrayList<Diamond> diamond;

    private Player player;
    private int width = 0;
    private int height = 0;

    private int currentLevelIndex = 0;
    private boolean complete = false;

    public Game() {
        loadImages();
        loadLevel(currentLevelIndex);
        initBoard(); 
    }
    
    private void loadImages() {
    imageMap = new HashMap<>();
    try {
    File pathToFile = new File("src/graphics/Floor.png");
    imageMap.put("Floor", ImageIO.read(pathToFile));
    
    pathToFile = new File("src/graphics/Wall.png");
    imageMap.put("Wall", ImageIO.read(pathToFile));
    
    pathToFile = new File("src/graphics/Diamond.png");
    imageMap.put("Diamond", ImageIO.read(pathToFile));
    
    pathToFile = new File("src/graphics/Crate.png");
    imageMap.put("Crate", ImageIO.read(pathToFile));
    
    pathToFile = new File("src/graphics/WarehouseKeeper.png");
    imageMap.put("Player", ImageIO.read(pathToFile));
} catch (IOException e) {
    e.printStackTrace();
}

}

    private void loadLevel(int levelIndex) {
        currentLevelIndex = levelIndex; // Set the current level index
        System.out.println("Current level index set to: " + currentLevelIndex);
        String level = "";
        switch (levelIndex) {
            case 0:
                level = readLevelFromFile("level1.txt");
                break;
            case 1:
                level = readLevelFromFile("level2.txt");
                break;
            case 2:
                level = readLevelFromFile("level3.txt");
                break;
            case 3:
                level = readLevelFromFile("level4.txt");
                break;
            case 4:
                level = readLevelFromFile("level5.txt");
                break;
            default:
                System.out.println("Invalid level index: " + levelIndex);
                return;
        }

        if (level.isEmpty()) {
            System.out.println("Level data is empty or could not be read for level index: " + levelIndex);
            return;
        }

        System.out.println("Level loaded: \n" + level);
        initMap(level); // Load the level
    }

    private void loadNextLevel() {
        if (currentLevelIndex + 1 < 5) { // there are 5 levels indexed from 0 to 4
            System.out.println("Loading level: " + (currentLevelIndex + 1));
            loadLevel(currentLevelIndex + 1);
        } else {
            // Handle the completion of all levels (show message or restart the game)
            System.out.println("All levels completed. Congratulations!");
        }
    }

    private String readLevelFromFile(String filename) {
        StringBuilder levelStringBuilder = new StringBuilder();
        InputStream in = getClass().getResourceAsStream("/maps/" + filename);

        if (in == null) {
            System.out.println("Level file not found: " + filename);
            return "";
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                levelStringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return levelStringBuilder.toString();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        loadLevel(currentLevelIndex);
    }

    public int getBoardWidth() {
        return this.width;
    }

    public int getBoardHeight() {
        return this.height;
    }

    public int getMaxLevelWidth() {
        int maxWidth = 0;
        for (int i = 1; i <= 5; i++) { // there are 5 levels
            String level = readLevelFromFile("level" + i + ".txt");
            String[] lines = level.split("\n");
            for (String line : lines) {
                maxWidth = Math.max(maxWidth, line.length());
            }
        }
        return maxWidth * SPACE;
    }

    public int getMaxLevelHeight() {
        int maxHeight = 0;
        for (int i = 1; i <= 5; i++) { // there are 5 levels
            String level = readLevelFromFile("level" + i + ".txt");
            String[] lines = level.split("\n");
            maxHeight = Math.max(maxHeight, lines.length);
        }
        int extraSpace = 30;
        return maxHeight * SPACE + extraSpace;
    }

    private void initMap(String level) {
        System.out.println("Initializing map with level data...");
        wall = new ArrayList<>();
        crate = new ArrayList<>();
        diamond = new ArrayList<>();

        wall.clear();
        crate.clear();
        diamond.clear();
        player = null;

        width = 0;
        height = 0;

        int x = OFFSET;
        int y = OFFSET;

        Wall initWall;
        Crate initCrate;
        Diamond initDiamond;

        for (int i = 0; i < level.length(); i++) {

            char item = level.charAt(i);

            switch (item) {

                case '\n':
                    y += SPACE;

                    if (this.width < x) {
                        this.width = x;
                    }

                    x = OFFSET;
                    break;

                case 'X':
                    initWall = new Wall(x, y, imageMap);
                    this.wall.add(initWall);
                    x += SPACE;
                    break;

                case '*':
                    initCrate = new Crate(x, y, imageMap);
                    crate.add(initCrate);
                    x += SPACE;
                    break;

                case '.':
                    initDiamond = new Diamond(x, y, imageMap);
                    diamond.add(initDiamond);
                    x += SPACE;
                    break;

                case '@':
                    player = new Player(x, y, imageMap);
                    x += SPACE;
                    break;

                case ' ':
                    x += SPACE;
                    break;

                default:
                    break;
            }

            height = y;
        }
    }

    private void buildMap(Graphics g) {
        super.paintComponent(g);

        Image floor = imageMap.get("Floor");
         if (floor == null) {
    System.out.println("Floor image not found in the map.");
} else {
            for (int x = 0; x < getWidth(); x += OFFSET) {
                for (int y = 0; y < getHeight(); y += OFFSET) {
                    g.drawImage(floor, x, y, this);
                }
            }
        }
        
        

        ArrayList<MapElement> mapArray = new ArrayList<>();

        mapArray.addAll(wall);
        mapArray.addAll(diamond);
        mapArray.addAll(crate);
        mapArray.add(player);

        for (int i = 0; i < mapArray.size(); i++) {

            MapElement item = mapArray.get(i);

            if (item instanceof Player || item instanceof Crate) {

                g.drawImage(item.getImage(), item.getX() + 2, item.getY() + 2, this);
            } else {

                g.drawImage(item.getImage(), item.getX(), item.getY(), this);
            }

            if (complete) {

                g.setColor(new Color(0, 0, 0));
                g.drawString("Completed", 25, 20);
            }

        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        buildMap(g);
    }

    protected class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            if (complete) {
                return;
            }

            int key = e.getKeyCode();

            switch (key) {

                case KeyEvent.VK_LEFT:

                    if (wallCollision(player,
                            LEFT_COLLISION)) {
                        return;
                    }

                    if (crateCollision(LEFT_COLLISION)) {
                        return;
                    }

                    player.move(-SPACE, 0);

                    break;

                case KeyEvent.VK_RIGHT:

                    if (wallCollision(player, RIGHT_COLLISION)) {
                        return;
                    }

                    if (crateCollision(RIGHT_COLLISION)) {
                        return;
                    }

                    player.move(SPACE, 0);

                    break;

                case KeyEvent.VK_UP:

                    if (wallCollision(player, TOP_COLLISION)) {
                        return;
                    }

                    if (crateCollision(TOP_COLLISION)) {
                        return;
                    }

                    player.move(0, -SPACE);

                    break;

                case KeyEvent.VK_DOWN:

                    if (wallCollision(player, BOTTOM_COLLISION)) {
                        return;
                    }

                    if (crateCollision(BOTTOM_COLLISION)) {
                        return;
                    }

                    player.move(0, SPACE);

                    break;

                case KeyEvent.VK_R:

                    restartLevel();

                    break;

                default:
                    break;
            }

            repaint();
        }
    }

    private boolean wallCollision(MapElement elementCollision, int type) {

        switch (type) {

            case LEFT_COLLISION:

                for (int i = 0; i < wall.size(); i++) {

                    Wall wall = this.wall.get(i);

                    if (elementCollision.isLeftCollision(wall)) {

                        return true;
                    }
                }

                return false;

            case RIGHT_COLLISION:

                for (int i = 0; i < wall.size(); i++) {

                    Wall wall = this.wall.get(i);

                    if (elementCollision.isRightCollision(wall)) {
                        return true;
                    }
                }

                return false;

            case TOP_COLLISION:

                for (int i = 0; i < wall.size(); i++) {

                    Wall wall = this.wall.get(i);

                    if (elementCollision.isTopCollision(wall)) {

                        return true;
                    }
                }

                return false;

            case BOTTOM_COLLISION:

                for (int i = 0; i < wall.size(); i++) {

                    Wall wall = this.wall.get(i);

                    if (elementCollision.isBottomCollision(wall)) {

                        return true;
                    }
                }

                return false;

            default:
                break;
        }

        return false;
    }

    private boolean crateCollision(int type) {

        switch (type) {

            case LEFT_COLLISION:

                for (int i = 0; i < crate.size(); i++) {

                    Crate crateLeftCollision = crate.get(i);

                    if (player.isLeftCollision(crateLeftCollision)) {

                        for (int j = 0; j < crate.size(); j++) {

                            Crate item = crate.get(j);

                            if (!crateLeftCollision.equals(item)) {

                                if (crateLeftCollision.isLeftCollision(item)) {
                                    return true;
                                }
                            }

                            if (wallCollision(crateLeftCollision, LEFT_COLLISION)) {
                                return true;
                            }
                        }

                        crateLeftCollision.move(-SPACE, 0);
                        isCompleted();
                    }
                }

                return false;

            case RIGHT_COLLISION:

                for (int i = 0; i < crate.size(); i++) {

                    Crate crateRightCollision = crate.get(i);

                    if (player.isRightCollision(crateRightCollision)) {

                        for (int j = 0; j < crate.size(); j++) {

                            Crate item = crate.get(j);

                            if (!crateRightCollision.equals(item)) {

                                if (crateRightCollision.isRightCollision(item)) {
                                    return true;
                                }
                            }

                            if (wallCollision(crateRightCollision, RIGHT_COLLISION)) {
                                return true;
                            }
                        }

                        crateRightCollision.move(SPACE, 0);
                        isCompleted();
                    }
                }
                return false;

            case TOP_COLLISION:

                for (int i = 0; i < crate.size(); i++) {

                    Crate crateTopCollision = crate.get(i);

                    if (player.isTopCollision(crateTopCollision)) {

                        for (int j = 0; j < crate.size(); j++) {

                            Crate item = crate.get(j);

                            if (!crateTopCollision.equals(item)) {

                                if (crateTopCollision.isTopCollision(item)) {
                                    return true;
                                }
                            }

                            if (wallCollision(crateTopCollision, TOP_COLLISION)) {
                                return true;
                            }
                        }

                        crateTopCollision.move(0, -SPACE);
                        isCompleted();
                    }
                }

                return false;

            case BOTTOM_COLLISION:

                for (int i = 0; i < crate.size(); i++) {

                    Crate crateBottomCollision = crate.get(i);

                    if (player.isBottomCollision(crateBottomCollision)) {

                        for (int j = 0; j < crate.size(); j++) {

                            Crate item = crate.get(j);

                            if (!crateBottomCollision.equals(item)) {

                                if (crateBottomCollision.isBottomCollision(item)) {
                                    return true;
                                }
                            }

                            if (wallCollision(crateBottomCollision, BOTTOM_COLLISION)) {

                                return true;
                            }
                        }

                        crateBottomCollision.move(0, SPACE);
                        isCompleted();
                    }
                }

                break;

            default:
                break;
        }

        return false;
    }

    public void isCompleted() {
        int numCratesOnDiamonds = 0;

        for (Crate crate : crate) {
            for (Diamond diamond : diamond) {
                if (crate.getX() == diamond.getX() && crate.getY() == diamond.getY()) {
                    numCratesOnDiamonds++;
                }
            }
        }

        if (numCratesOnDiamonds == diamond.size()) {
            complete = true;
            repaint();
            System.out.println("Level completed. Loading next level...");
            // Delay loading the next level to allow the user to see the completed message
            SwingUtilities.invokeLater(() -> {
                complete = false; // Reset the complete flag
                loadNextLevel();
                repaint();
            });
        }
    }

    private void restartLevel() {
        loadLevel(currentLevelIndex); // Restart the current level
    }
}
