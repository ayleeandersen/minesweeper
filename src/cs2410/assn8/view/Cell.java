package cs2410.assn8.view;

import javafx.scene.control.Button;

/**
 * Contains information about an individual cell.
 *
 * @author Aylee Andersen
 * @version 1.0
 */
public class Cell extends Button {
    /**
     * Holds either 'b' or 'n' to represent either a bomb cell or a normal cell.
     */
    private char cellLetter;
    /**
     * Says whether a cell is marked as questioned or not.
     */
    private boolean questioned = false;
    /**
     * Says whether a cell is marked as flagged or not.
     */
    private boolean flagged = false;
    /**
     * Contains the y position of the cell in the grid.
     */
    private int rowPos;
    /**
     * Contains the x position of the cell in the grid.
     */
    private int colPos;
    /**
     * Contains the number of bombs surrounding the cell.
     */
    private int numNeighbors = 0;
    /**
     * States whether or not that cell has been filled in or selected already.
     */
    private boolean filled = false;

    /**
     * Creates a new Cell.
     * @param letter The letter representing whether the cell is a bomb cell or a normal cell.
     * @param row The y position of the cell in the grid.
     * @param col The x posiiton of the cell in the grid.
     */
    public Cell(char letter, int row, int col) {
        cellLetter = letter;
        rowPos = row;
        colPos = col;
    }

    /**
     * Returns the cell's letter.
     * @return Value of cellLetter member variable.
     */
    public char getCellLetter() {return cellLetter;}
    /**
     * Returns whether or not the cell is marked as questioned.
     * @return the value of the questioned member variable.
     */
    public boolean isQuestioned() {
        return questioned;
    }
    /**
     * Returns whether or not the cell is marked as flagged.
     * @return the value of the flagged member variable.
     */
    public boolean isFlagged() {
        return flagged;
    }
    /**
     * Sets the value of the questioned member variable.
     * @param questioned The value with which to replace the questioned member variable.
     */
    public void setQuestioned(boolean questioned) {
        this.questioned = questioned;
    }
    /**
     * Sets the value of the flagged member variable.
     * @param flagged The value with which to replace the flagged member variable.
     */
    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
    /**
     * Returns the cell's row position.
     * @return The value of the rowPos member variable.
     */
    public int getRowPos() {return rowPos;}
    /**
     * Returns the cell's col position.
     * @return The value of the colPos member variable.
     */
    public int getColPos() {return colPos;}
    /**
     * Sets the value of the numNeighbors member variable.
     * @param num The value to replace numNeighbors with.
     */
    public void setNumNeighbors(int num) {numNeighbors = num;}
    /**
     * Returns the cell's number of bomb neighbors.
     * @return The value of the numNeighbors member variable.
     */
    public int getNumNeighbors() {return numNeighbors;}
    /**
     * Sets the value of the filled member variable.
     * @param fill The value to replace filled with.
     */
    public void setFilled(boolean fill) {filled = fill;}
    /**
     * Returns whether or not the cell has been filled or selected already.
     * @return The value of the filled member variable.
     */
    public boolean isFilled() {return filled;}
}
