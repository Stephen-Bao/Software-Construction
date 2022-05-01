package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.Feature;
import edu.cmu.cs.cs214.hw4.core.FeatureType;
import edu.cmu.cs.cs214.hw4.core.GameChangeListener;
import edu.cmu.cs.cs214.hw4.core.GameSystem;
import edu.cmu.cs.cs214.hw4.core.Orientation;
import edu.cmu.cs.cs214.hw4.core.Player;
import edu.cmu.cs.cs214.hw4.core.Segment;
import edu.cmu.cs.cs214.hw4.core.Tile;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.cmu.cs.cs214.hw4.core.Board.NUM_X;
import static edu.cmu.cs.cs214.hw4.core.Board.NUM_Y;
import static edu.cmu.cs.cs214.hw4.core.Board.START_X;
import static edu.cmu.cs.cs214.hw4.core.Board.START_Y;
import static edu.cmu.cs.cs214.hw4.core.Tile.DEGREE_90;

/**
 * The GUI class for Carcassonne game.
 */
public class GameGUI implements GameChangeListener {

    public static final String GAME_NAME = "Carcassonne";
    public static final String IMAGE_FILENAME = "Carcassonne.png";
    public static final int PARENT_FRAME_WIDTH = 800;
    public static final int PARENT_FRAME_HEIGHT = 100;
    public static final int CHILD_FRAME_WIDTH = 1000;
    public static final int CHILD_FRAME_HEIGHT = 700;
    public static final int FLOWLAYOUT_HGAP = 40;
    public static final int TILE_PANEL_WIDTH = 1000;
    public static final int TILE_PANEL_HEIGHT = 100;
    public static final int IMAGE_PER_ROW = 6;
    public static final int TEXTFIELD_LENGTH = 5;
    public static final int LINEBORDER_THICKNESS = 4;
    public static final int SCROLLER_UNIT_INCREMENT = 16;
    public static final int SCROLLER_DEFAULT_X = 6100;
    public static final int SCROLLER_DEFAULT_Y = 6350;
    public static final int RADIUS = 10;
    public static final int IMAGE_WIDTH = 90;
    public static final int IMAGE_HEIGHT = 90;

    private GameSystem gameSystem;
    private Tile currentTile;
    private int currentTilePositionRow;
    private int currentTilePositionCol;
    private BufferedImage currentTileImage;
    private Map<Tile, Integer> tileIndexMap;
    private Map<ButtonPosition, BufferedImage> originalImageMap;

    private JFrame parentFrame;
    private JFrame childFrame;
    private JLabel[] playerLabels;
    private JLabel currentPlayerLabel;
    private JTextField deckSizeTextField;
    private JButton currentTileButton;
    private BufferedImage image;
    private JButton[][] boardButtons;
    private JButton placeTileButton;
    private JButton placeTileWithMeepleButton;
    private ButtonGroup group;
    private JRadioButton leftButton;
    private JRadioButton upButton;
    private JRadioButton rightButton;
    private JRadioButton downButton;
    private JRadioButton centerButton;

    /**
     * Constructor to initialize GUI.
     */
    public GameGUI() {

        // Parent frame
        JFrame frame = new JFrame(GAME_NAME);
        frame.setSize(PARENT_FRAME_WIDTH, PARENT_FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.parentFrame = frame;

        // Game start panel
        JPanel panel = new JPanel();
        JLabel playerNumLabel = new JLabel("Enter player number here (2 - 5): ");
        panel.add(playerNumLabel);
        JTextField playerNumField = new JTextField(5);
        panel.add(playerNumField);
        JButton startButton = new JButton("Start Game");

        ActionListener startGameListener = e -> {
            try {
                int playerNum = Integer.parseInt(playerNumField.getText());
                if (playerNum >= 2 && playerNum <= 5) {
                    startMainGameGUI(playerNum);
                }
                else {
                    throw new NumberFormatException();
                }
            }
            catch (NumberFormatException n) {
                showDialog(frame, "error", "Please enter a number that is between 2 and 5!");
            }
            catch (IOException i) {
                showDialog(frame, "error", "Can not load image file!");
            }
        };

        // Add start game listener
        startButton.addActionListener(startGameListener);
        playerNumField.addActionListener(startGameListener);
        panel.add(startButton);

        // Add panel to frame
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    /**
     * Starts the main game GUI after taking the player number. Invoked in GameGUI constructor.
     * @param playerNum number of players
     * @throws IOException on error loading tile image
     */
    public void startMainGameGUI(int playerNum) throws IOException {

        // Close parent frame
        parentFrame.dispose();
        parentFrame = null;

        gameSystem = new GameSystem(playerNum);
        gameSystem.addGameChangeListener(this);
        image = ImageIO.read(new File(IMAGE_FILENAME));
        createTileIndexMap();

        // Frame
        JFrame frame = new JFrame(GAME_NAME);
        frame.setSize(CHILD_FRAME_WIDTH, CHILD_FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        childFrame = frame;

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // PlayerPanel
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, FLOWLAYOUT_HGAP, 0));

        playerLabels = new JLabel[playerNum];
        for (int i = 0; i < playerNum; i++) {
            playerLabels[i] = new JLabel();
            playerLabels[i].setOpaque(true);
            // Set different color for different players
            switch (i + 1) {
                case 1:
                    playerLabels[i].setBackground(Color.WHITE);
                    break;
                case 2:
                    playerLabels[i].setBackground(Color.RED);
                    break;
                case 3:
                    playerLabels[i].setBackground(Color.CYAN);
                    break;
                case 4:
                    playerLabels[i].setBackground(Color.YELLOW);
                    break;
                case 5:
                    playerLabels[i].setBackground(Color.GREEN);
                    break;
                default:
                    throw new IllegalArgumentException("Player number wrong!");
            }
            playerPanel.add(playerLabels[i]);
        }
        // Initialize player info
        playerInfoChanged();

        // Tile Panel
        JPanel tilePanel = new JPanel();
        tilePanel.setMinimumSize(new Dimension(TILE_PANEL_WIDTH, TILE_PANEL_HEIGHT));

        currentPlayerLabel = new JLabel("Player" + gameSystem.getCurrentPlayer()
                + ": (click tile to rotate by 90 degree clockwise)");
        tilePanel.add(currentPlayerLabel);

        // Initialize some GUI-keeping info
        currentTile = gameSystem.drawTile();
        currentTilePositionRow = -1;
        currentTilePositionCol = -1;
        int tileIndex = tileIndexMap.get(currentTile);
        int tempX = (tileIndex % IMAGE_PER_ROW) * IMAGE_WIDTH;
        int tempY = (tileIndex / IMAGE_PER_ROW) * IMAGE_HEIGHT;
        currentTileImage = image.getSubimage(tempX, tempY, IMAGE_WIDTH, IMAGE_HEIGHT);

        currentTileButton = new JButton();
        currentTileButton.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        ImageIcon icon = new ImageIcon(currentTileImage);
        currentTileButton.setIcon(icon);
        // Click to rotate
        currentTileButton.addActionListener(e -> {
            if (currentTileButton.getIcon() != null) {
                currentTile = gameSystem.rotateTile(currentTile, DEGREE_90);
                currentTileImage = rotateClockwise(currentTileImage, 1);
                ImageIcon newIcon = new ImageIcon(currentTileImage);
                currentTileButton.setIcon(newIcon);
            }
        });
        tilePanel.add(currentTileButton);

        JLabel deckSizeLabel = new JLabel("Deck size: ");
        tilePanel.add(deckSizeLabel);
        deckSizeTextField = new JTextField(TEXTFIELD_LENGTH);
        deckSizeTextField.setText(String.valueOf(gameSystem.getDeckSize()));
        deckSizeTextField.setEditable(false);
        tilePanel.add(deckSizeTextField);

        // Board panel
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(NUM_X, NUM_Y, 0, 0));
        boardButtons = new JButton[NUM_X][NUM_Y];
        // Initialize board buttons
        for (int i = 0; i < NUM_X; i++) {
            for (int j = 0; j < NUM_Y; j++) {
                // Place buttons on a subpanel to fix size
                JPanel subPanel = new JPanel();
                subPanel.setPreferredSize(new Dimension(IMAGE_WIDTH + 2, IMAGE_HEIGHT + 2));
                boardButtons[i][j] = new JButton();
                boardButtons[i][j].setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
                boardButtons[i][j].setOpaque(true);
                boardButtons[i][j].setVisible(false);

                int row = i; int col = j;
                boardButtons[i][j].addActionListener(e -> {
                    // If game has ended, do nothing
                    if (currentTile == null) {
                        return;
                    }
                    // If no icon on button, display current tile image on it
                    if (boardButtons[row][col].getIcon() == null) {
                        if (currentTilePositionRow == -1 || currentTilePositionCol == -1) {
                            currentTileButton.setIcon(null);
                            // Enable placement buttons
                            placeTileButton.setEnabled(true);
                            placeTileWithMeepleButton.setEnabled(true);
                            leftButton.setEnabled(true);
                            upButton.setEnabled(true);
                            rightButton.setEnabled(true);
                            downButton.setEnabled(true);
                            centerButton.setEnabled(true);
                        }
                        else {
                            boardButtons[currentTilePositionRow][currentTilePositionCol].setIcon(null);
                            boardButtons[currentTilePositionRow][currentTilePositionCol]
                                    .setBorder(UIManager.getBorder("Button.border"));
                        }
                        ImageIcon newIcon = new ImageIcon(currentTileImage);
                        boardButtons[row][col].setIcon(newIcon);
                        Border border = new LineBorder(Color.YELLOW, LINEBORDER_THICKNESS);
                        boardButtons[row][col].setBorder(border);

                        currentTilePositionRow = row;
                        currentTilePositionCol = col;
                    }
                    // If it's current tile position, rotate by 90 degree clockwise
                    else if (row == currentTilePositionRow && col == currentTilePositionCol) {
                        currentTile = gameSystem.rotateTile(currentTile, DEGREE_90);
                        currentTileImage = rotateClockwise(currentTileImage, 1);
                        ImageIcon newIcon = new ImageIcon(currentTileImage);
                        boardButtons[row][col].setIcon(newIcon);
                    }
                });
                subPanel.add(boardButtons[i][j]);
                boardPanel.add(subPanel);
            }
        }

        // Add a scroller to button panel
        JPanel outerBoardPanel = new JPanel();
        outerBoardPanel.setLayout(new BorderLayout());
        outerBoardPanel.add(boardPanel, BorderLayout.SOUTH);
        JScrollPane scroller = new JScrollPane(boardPanel);
        scroller.getHorizontalScrollBar().setUnitIncrement(SCROLLER_UNIT_INCREMENT);
        scroller.getVerticalScrollBar().setUnitIncrement(SCROLLER_UNIT_INCREMENT);
        outerBoardPanel.add(scroller, BorderLayout.CENTER);
        displayStarterTile(START_X, START_Y);
        scroller.getViewport().setViewPosition(new Point(SCROLLER_DEFAULT_X, SCROLLER_DEFAULT_Y));

        // Placement panel
        JPanel placementPanel = new JPanel();
        placeTileButton = new JButton("Place tile only");
        placeTileWithMeepleButton = new JButton("Place tile with meeple");
        placeTileButton.setEnabled(false);
        placeTileWithMeepleButton.setEnabled(false);

        placeTileButton.addActionListener(e -> {
            // If tile is not on the board, throw an exception
            if (currentTilePositionRow == -1 || currentTilePositionCol == -1) {
                throw new UnsupportedOperationException("Tile placement operation wrong!");
            }
            // Redraw current tile to erase possible meeple on it
            ImageIcon noMeepleIcon = new ImageIcon(currentTileImage);
            boardButtons[currentTilePositionRow][currentTilePositionCol].setIcon(noMeepleIcon);

            // Convert (row, col) coordinates to (x, y) coordinates
            int x = currentTilePositionCol;
            int y = NUM_Y - currentTilePositionRow - 1;
            boolean success = gameSystem.placeNewTile(currentTile, x, y);
            if (!success) {
                showDialog(frame, "Ilegal tile position",
                        "Can not place tile in this position, please check your placement!");
            }
            if (success) {
                if (gameSystem.getDeckSize() > 0) {
                    gameSystem.nextRound();
                }
                else if (gameSystem.getDeckSize() == 0) {
                    gameSystem.computeFinalScore();
                }
                else {
                    throw new UnsupportedOperationException("Deck size wrong!");
                }
            }
        });
        placementPanel.add(placeTileButton);
        placementPanel.add(placeTileWithMeepleButton);

        // Button group for meeple position selection
        group = new ButtonGroup();
        leftButton = new JRadioButton("left");
        upButton = new JRadioButton("up");
        rightButton = new JRadioButton("right");
        downButton = new JRadioButton("down");
        centerButton = new JRadioButton("center");
        group.add(leftButton);
        group.add(upButton);
        group.add(rightButton);
        group.add(downButton);
        group.add(centerButton);
        leftButton.addActionListener(new MeepleButtonActionListener());
        upButton.addActionListener(new MeepleButtonActionListener());
        rightButton.addActionListener(new MeepleButtonActionListener());
        downButton.addActionListener(new MeepleButtonActionListener());
        centerButton.addActionListener(new MeepleButtonActionListener());
        leftButton.setEnabled(false);
        upButton.setEnabled(false);
        rightButton.setEnabled(false);
        downButton.setEnabled(false);
        centerButton.setEnabled(false);
        placementPanel.add(leftButton);
        placementPanel.add(upButton);
        placementPanel.add(rightButton);
        placementPanel.add(downButton);
        placementPanel.add(centerButton);

        originalImageMap = new HashMap<>();
        placeTileWithMeepleButton.addActionListener(e -> {
            if (currentTilePositionRow == -1 || currentTilePositionCol == -1) {
                throw new UnsupportedOperationException("Tile placement operation wrong!");
            }

            int x = currentTilePositionCol;
            int y = NUM_Y - currentTilePositionRow - 1;
            // Get meeple orientation from button
            Orientation orient;
            if (leftButton.isSelected()) {
                orient = Orientation.LEFT;
            }
            else if (upButton.isSelected()) {
                orient = Orientation.UP;
            }
            else if (rightButton.isSelected()) {
                orient = Orientation.RIGHT;
            }
            else if (downButton.isSelected()) {
                orient = Orientation.DOWN;
            }
            else if (centerButton.isSelected()){
                orient = Orientation.CENTER;
            }
            else {
                showDialog(frame, "No orientation selected", "Please select an orientation!");
                return;
            }

            boolean success = gameSystem.placeNewTileWithMeeple(currentTile, x, y, orient);
            if (!success) {
                showDialog(frame, "Ilegal tile/meeple position",
                        "Can not place tile/meeple in this position, please check your placement!");
            }
            if (success) {
                if (gameSystem.getDeckSize() > 0) {
                    originalImageMap.put(
                            new ButtonPosition(currentTilePositionRow, currentTilePositionCol), currentTileImage);
                    gameSystem.nextRound();
                }
                else if (gameSystem.getDeckSize() == 0) {
                    gameSystem.computeFinalScore();
                }
                else {
                    throw new UnsupportedOperationException("Deck size wrong!");
                }
            }
        });


        // Add panel to frame
        panel.add(playerPanel);
        panel.add(tilePanel);
        panel.add(outerBoardPanel);
        panel.add(placementPanel);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    /**
     * Action listener invoked when clicking on the meeple orientation buttons.
     */
    private class MeepleButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Determine meeple color based on player id
            Color meepleColor;
            switch (gameSystem.getCurrentPlayer()) {
                case 1:
                    meepleColor = Color.WHITE;
                    break;
                case 2:
                    meepleColor = Color.RED;
                    break;
                case 3:
                    meepleColor = Color.CYAN;
                    break;
                case 4:
                    meepleColor = Color.YELLOW;
                    break;
                case 5:
                    meepleColor = Color.GREEN;
                    break;
                default:
                    throw new UnsupportedOperationException("Player number is wrong!");
            }

            // Determine circle position based on user selection
            int x;
            int y;
            if (e.getSource() == leftButton) {
                x = RADIUS;
                y = IMAGE_HEIGHT / 2;
            }
            else if (e.getSource() == upButton) {
                x = IMAGE_WIDTH / 2;
                y = RADIUS;
            }
            else if (e.getSource() == rightButton) {
                x = IMAGE_WIDTH - RADIUS;
                y = IMAGE_HEIGHT / 2;
            }
            else if (e.getSource() == downButton) {
                x = IMAGE_WIDTH / 2;
                y = IMAGE_HEIGHT - RADIUS;
            }
            else {
                x = IMAGE_WIDTH / 2;
                y = IMAGE_HEIGHT / 2;
            }

            // Display a circle on current tile to represent a meeple
            if (currentTilePositionRow != -1 && currentTilePositionCol != -1) {
                BufferedImage imageWithCircle = withCircle(currentTileImage, meepleColor, x, y, RADIUS);
                ImageIcon icon = new ImageIcon(imageWithCircle);
                boardButtons[currentTilePositionRow][currentTilePositionCol].setIcon(icon);
            }
        }
    }

    @Override
    public void playerInfoChanged() {
        // Redraw each player label
        int playerNum = gameSystem.getTotalPlayers();
        for (int i = 0; i < playerNum; i++) {
            Player player = gameSystem.getPlayerList().get(i);
            String labelInfo = "Player" + (i + 1) + ": "
                    + player.getScore() + " points, " + player.getMeepleNumber() + "meeples";
            playerLabels[i].setText(labelInfo);
        }
    }

    @Override
    public void gameBoardChanged(int x, int y) {
        if (x < 0 || x > NUM_X - 1 || y < 0 || y > NUM_Y - 1) {
            throw new IllegalArgumentException("x, y out of game board range!");
        }

        if (gameSystem.getTileFromBoard(x, y) != null) {
            // Convert from (x, y) to (row, col)
            int row = NUM_Y - y - 1;
            int col = x;
            // Set neighboring buttons as visible
            boardButtons[row][col].setVisible(true);
            boardButtons[row - 1][col].setVisible(true);
            boardButtons[row][col - 1].setVisible(true);
            boardButtons[row + 1][col].setVisible(true);
            boardButtons[row][col + 1].setVisible(true);
        }
    }

    @Override
    public void roundChanged() {

        currentPlayerLabel.setText("Player" + gameSystem.getCurrentPlayer() + ": ");
        // Disable border on newly placed tile
        boardButtons[currentTilePositionRow][currentTilePositionCol]
                .setBorder(UIManager.getBorder("Button.border"));

        // Draw a new tile. If all positions are illegal, discard and redraw
        if (gameSystem.getDeckSize() == 0) {
            gameSystem.computeFinalScore();
            return;
        }
        boolean isLegal;
        do {
            currentTile = gameSystem.drawTile();
            isLegal = gameSystem.checkPossiblePosition(currentTile);
            if (!isLegal) {
                showDialog(childFrame, "No where to place tile",
                        "There are no legal place for current tile. It will be discarded and replaced!");
            }
            currentTilePositionRow = -1;
            currentTilePositionCol = -1;
            int tileIndex = tileIndexMap.get(currentTile);
            int tempX = (tileIndex % IMAGE_PER_ROW) * IMAGE_WIDTH;
            int tempY = (tileIndex / IMAGE_PER_ROW) * IMAGE_HEIGHT;
            currentTileImage = image.getSubimage(tempX, tempY, IMAGE_WIDTH, IMAGE_HEIGHT);
            ImageIcon newIcon = new ImageIcon(currentTileImage);
            currentTileButton.setIcon(newIcon);
            deckSizeTextField.setText(String.valueOf(gameSystem.getDeckSize()));
        } while (!isLegal);

        // Disable and initialize meeple buttons
        placeTileButton.setEnabled(false);
        placeTileWithMeepleButton.setEnabled(false);
        leftButton.setEnabled(false);
        upButton.setEnabled(false);
        rightButton.setEnabled(false);
        downButton.setEnabled(false);
        centerButton.setEnabled(false);
        group.clearSelection();
    }

    @Override
    public void featureCompleted(Feature f) {

        for (Segment s : f.getSegmentList()) {
            // Redraw tile if there was a meeple on it
            if (s.getMeeple() != 0) {
                int row = NUM_Y - s.getY() - 1;
                int col = s.getX();
                BufferedImage originalImage = originalImageMap.get(new ButtonPosition(row, col));
                if (originalImage == null) {
                    originalImage = currentTileImage;
                }
                ImageIcon icon = new ImageIcon(originalImage);
                boardButtons[row][col].setIcon(icon);
            }
        }
    }

    @Override
    public void gameEnded() {

        // Clear some GUI keeping info
        currentTile = null;
        currentTileImage = null;
        currentTilePositionRow = -1;
        currentTilePositionCol = -1;
        deckSizeTextField.setText(String.valueOf(gameSystem.getDeckSize()));

        // Disable placement buttons
        placeTileButton.setEnabled(false);
        placeTileWithMeepleButton.setEnabled(false);
        leftButton.setEnabled(false);
        upButton.setEnabled(false);
        rightButton.setEnabled(false);
        downButton.setEnabled(false);
        centerButton.setEnabled(false);
        group.clearSelection();

        // Declare winner(s) in a dialog window
        List<Player> winners = gameSystem.findWinner();
        StringBuilder sb = new StringBuilder();
        sb.append("Congratulations! The winners are: \n");
        for (Player p : winners) {
            sb.append("Player").append(p.getId()).append(" with score ")
                    .append(p.getScore()).append("\n");
        }
        showDialog(childFrame, "Game over!", sb.toString());
    }

    private static void showDialog(Component component, String title, String message) {
        JOptionPane.showMessageDialog(component, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void createTileIndexMap() {
        tileIndexMap = new HashMap<>();

        // A
        tileIndexMap.put(new Tile(FeatureType.FIELD, FeatureType.FIELD, FeatureType.FIELD, FeatureType.ROAD,
                List.of(FeatureType.MONASTERY), false), 0);
        // B
        tileIndexMap.put(new Tile(FeatureType.FIELD, FeatureType.FIELD, FeatureType.FIELD, FeatureType.FIELD,
                List.of(FeatureType.MONASTERY), false), 1);
        // C
        tileIndexMap.put(new Tile(FeatureType.CITY, FeatureType.CITY, FeatureType.CITY, FeatureType.CITY,
                List.of(FeatureType.CITY), true), 2);
        // D
        tileIndexMap.put(new Tile(FeatureType.FIELD, FeatureType.ROAD, FeatureType.CITY, FeatureType.ROAD,
                List.of(FeatureType.ROAD), false), 3);
        // E
        tileIndexMap.put(new Tile(FeatureType.FIELD, FeatureType.CITY, FeatureType.FIELD, FeatureType.FIELD,
                List.of(), false), 4);
        // F
        tileIndexMap.put(new Tile(FeatureType.CITY, FeatureType.FIELD, FeatureType.CITY, FeatureType.FIELD,
                List.of(FeatureType.CITY), true), 5);
        // G
        tileIndexMap.put(new Tile(FeatureType.FIELD, FeatureType.CITY, FeatureType.FIELD, FeatureType.CITY,
                List.of(FeatureType.CITY), false), 6);
        // H
        tileIndexMap.put(new Tile(FeatureType.CITY, FeatureType.FIELD, FeatureType.CITY, FeatureType.FIELD,
                List.of(), false), 7);
        // I
        tileIndexMap.put(new Tile(FeatureType.FIELD, FeatureType.FIELD, FeatureType.CITY, FeatureType.CITY,
                List.of(), false), 8);
        // J
        tileIndexMap.put(new Tile(FeatureType.FIELD, FeatureType.CITY, FeatureType.ROAD, FeatureType.ROAD,
                List.of(FeatureType.ROAD), false), 9);
        // K
        tileIndexMap.put(new Tile(FeatureType.ROAD, FeatureType.ROAD, FeatureType.CITY, FeatureType.FIELD,
                List.of(FeatureType.ROAD), false), 10);
        // L
        tileIndexMap.put(new Tile(FeatureType.ROAD, FeatureType.ROAD, FeatureType.CITY, FeatureType.ROAD,
                List.of(FeatureType.CROSSING), false), 11);
        // M
        tileIndexMap.put(new Tile(FeatureType.CITY, FeatureType.CITY, FeatureType.FIELD, FeatureType.FIELD,
                List.of(FeatureType.CITY), true), 12);
        // N
        tileIndexMap.put(new Tile(FeatureType.CITY, FeatureType.CITY, FeatureType.FIELD, FeatureType.FIELD,
                List.of(FeatureType.CITY), false), 13);
        // O
        tileIndexMap.put(new Tile(FeatureType.CITY, FeatureType.CITY, FeatureType.ROAD, FeatureType.ROAD,
                List.of(FeatureType.CITY, FeatureType.ROAD), true), 14);
        // P
        tileIndexMap.put(new Tile(FeatureType.CITY, FeatureType.CITY, FeatureType.ROAD, FeatureType.ROAD,
                List.of(FeatureType.CITY, FeatureType.ROAD), false), 15);
        // Q
        tileIndexMap.put(new Tile(FeatureType.CITY, FeatureType.CITY, FeatureType.CITY, FeatureType.FIELD,
                List.of(FeatureType.CITY), true), 16);
        // R
        tileIndexMap.put(new Tile(FeatureType.CITY, FeatureType.CITY, FeatureType.CITY, FeatureType.FIELD,
                List.of(FeatureType.CITY), false), 17);
        // S
        tileIndexMap.put(new Tile(FeatureType.CITY, FeatureType.CITY, FeatureType.CITY, FeatureType.ROAD,
                List.of(FeatureType.CITY), true), 18);
        // T
        tileIndexMap.put(new Tile(FeatureType.CITY, FeatureType.CITY, FeatureType.CITY, FeatureType.ROAD,
                List.of(FeatureType.CITY), false), 19);
        // U
        tileIndexMap.put(new Tile(FeatureType.FIELD, FeatureType.ROAD, FeatureType.FIELD, FeatureType.ROAD,
                List.of(FeatureType.ROAD), false), 20);
        // V
        tileIndexMap.put(new Tile(FeatureType.ROAD, FeatureType.FIELD, FeatureType.FIELD, FeatureType.ROAD,
                List.of(FeatureType.ROAD), false), 21);
        // W
        tileIndexMap.put(new Tile(FeatureType.ROAD, FeatureType.FIELD, FeatureType.ROAD, FeatureType.ROAD,
                List.of(FeatureType.CROSSING), false), 22);
        // X
        tileIndexMap.put(new Tile(FeatureType.ROAD, FeatureType.ROAD, FeatureType.ROAD, FeatureType.ROAD,
                List.of(FeatureType.CROSSING), false), 23);
    }

    private static BufferedImage rotateClockwise(BufferedImage src, int n) {
        int weight = src.getWidth();
        int height = src.getHeight();

        AffineTransform at = AffineTransform.getQuadrantRotateInstance(n, weight / 2.0, height / 2.0);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        BufferedImage dest = new BufferedImage(weight, height, src.getType());
        op.filter(src, dest);
        return dest;
    }

    private static BufferedImage withCircle(BufferedImage src, Color color, int x, int y, int radius) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());

        Graphics2D g = (Graphics2D) dest.getGraphics();
        g.drawImage(src, 0, 0, null);
        g.setColor(color);
        g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
        g.dispose();

        return dest;
    }

    private void displayStarterTile(int x, int y) {
        Tile newTile = gameSystem.getTileFromBoard(x, y);
        int tileIndex = tileIndexMap.get(newTile);
        int offsetX = (tileIndex % IMAGE_PER_ROW) * IMAGE_WIDTH;
        int offsetY = (tileIndex / IMAGE_PER_ROW) * IMAGE_HEIGHT;
        BufferedImage tileImage = image.getSubimage(offsetX, offsetY, IMAGE_WIDTH, IMAGE_HEIGHT);
        ImageIcon icon = new ImageIcon(tileImage);
        boardButtons[x][y].setIcon(icon);
        // Set neighboring buttons as visible
        boardButtons[x][y].setVisible(true);
        boardButtons[x - 1][y].setVisible(true);
        boardButtons[x][y - 1].setVisible(true);
        boardButtons[x + 1][y].setVisible(true);
        boardButtons[x][y + 1].setVisible(true);
    }

}












