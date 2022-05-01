package edu.cmu.cs.cs214.hw4.gui;

import java.util.Objects;

/**
 * A class to represent a position on tile board in GUI.
 */
public class ButtonPosition {

    private final int row;
    private final int col;

    /**
     * Initialize a position.
     * @param newRow row
     * @param newCol column
     */
    public ButtonPosition(int newRow, int newCol) {
        row = newRow;
        col = newCol;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ButtonPosition)) {
            return false;
        }
        ButtonPosition position = (ButtonPosition) o;

        return getRow() == position.getRow() && getCol() == position.getCol();
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

}













