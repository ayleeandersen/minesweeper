package cs2410.assn8.view;

import javafx.scene.media.AudioClip;
import java.io.File;

/**
 * Handles the winning and losing messages as well the audio. Counts the number of neighbors a cell has.
 *
 * @author Aylee Andersen
 * @version 1.0
 */
public class ScoreBoard {
    /**
     * The amount of time the game took.
     */
    private int endTime = 0;
    /**
     * The sound clip for when a player loses.
     */
    private AudioClip loseSounds = new AudioClip(new File("src/resources/lose.wav").toURI().toString());
    /**
     * The sound clip for when a player wins.
     */
    private AudioClip winSounds = new AudioClip(new File("src/resources/win.wav").toURI().toString());
    /**
     * The sound clip for when a player selects a bomb.
     */
    private AudioClip bombSounds = new AudioClip(new File("src/resources/bomb.wav").toURI().toString());
    /**
     * The sound clip for when a player selects a normal cell.
     */
    private AudioClip normalSounds = new AudioClip(new File("src/resources/normal.wav").toURI().toString());
    /**
     * The sound clip for when a player toggles a cell.
     */
    private AudioClip toggleSounds = new AudioClip(new File("src/resources/toggle.wav").toURI().toString());

    /**
     * Calculates the number of bombs surrounding a cell.
     * @param arr 2D array of cells.
     * @param cell Current cell being calculated.
     * @param height Height of the array.
     * @param width Width of the array.
     * @return Number of bombs surronding a cell.
     */
    public int getNumNeighbors(Cell[][] arr, Cell cell, int height, int width) {
        int numNeighbors = 0;
        for (int row = cell.getRowPos() - 1; row <= cell.getRowPos() + 1; row++) {
            for (int col = cell.getColPos() - 1; col <= cell.getColPos() + 1; col++) {
                if (row < height && row >= 0 && col < width && col >= 0) {
                    if (arr[row][col].getCellLetter() == 'b') {
                        numNeighbors++;
                    }
                }
            }
        }
        return numNeighbors;
    }

    /**
     * Gets the message for when a player loses.
     * @return The message for when a player loses.
     */
    public String getLoseMessage() {
        return "You lost in " + endTime + " seconds! Better luck next time.";
    }
    /**
     * Gets the message for when a player wins.
     * @return The message for when a player wins.
     */
    public String getWinMessage() {
        return "Congratulations! It only took you " + endTime + " seconds to win the game!";
    }
    /**
     * Sets the value of the endTime member variable.
     * @param time The value to set endTime to.
     */
    public void setEndTime(int time) {endTime = time;}
    /**
     * Gets the AudioClip containing the sound to be played when a player loses.
     * @return The AudioClip containing the sound to be played when a player loses.
     */
    public AudioClip getLoseSounds() {
        return loseSounds;
    }
    /**
     * Gets the AudioClip containing the sound to be played when a player wins.
     * @return The AudioClip containing the sound to be played when a player wins.
     */
    public AudioClip getWinSounds() {
        return winSounds;
    }
    /**
     * Gets the AudioClip containing the sound to be played when a player selects a bomb.
     * @return The AudioClip containing the sound to be played when a player selects a bomb.
     */
    public AudioClip getBombSounds() {
        return bombSounds;
    }
    /**
     * Gets the AudioClip containing the sound to be played when a player selects a normal cell.
     * @return The AudioClip containing the sound to be played when a player selects a normal cell.
     */
    public AudioClip getNormalSounds() {
        return normalSounds;
    }
    /**
     * Gets the AudioClip containing the sound to be played when a player toggles a cell.
     * @return The AudioClip containing the sound to be played when a player toggles a cell.
     */
    public AudioClip getToggleSounds() {
        return toggleSounds;
    }
}
