package cs2410.assn8.controller;

import cs2410.assn8.view.Cell;
import cs2410.assn8.view.Display;
import cs2410.assn8.view.ScoreBoard;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import java.net.URL;
import java.util.*;

/**
 * Handles all the board set up and manages gameplay and display of the game.
 *
 * @author Aylee Andersen
 * @version 1.0
 */
public class Controller implements Initializable{
    /**
     * The ComboBox with options for changing the size of the grid.
     */
    @FXML
    ComboBox<String> sizeOptions;
    /**
     * The ComboBox with options for changing the difficulty of the game.
     */
    @FXML
    ComboBox<String> difficultyOptions;
    /**
     * The start button for the game.
     */
    @FXML
    Button start;
    /**
     * Displays the number of bombs left.
     */
    @FXML
    Label bombsLeft;
    /**
     * Displays the time passed.
     */
    @FXML
    Label time;
    /**
     * The pane that holds the grid of cells.
     */
    @FXML
    GridPane grid;
    /**
     * The sound button for the sound effects.
     */
    @FXML
    Button sound;

    /**
     * Instance of the ScoreBoard class.
     */
    private ScoreBoard scoreBoard = new ScoreBoard();
    /**
     * Contains a list of all the bomb cells.
     */
    private ArrayList<Cell> bombList = new ArrayList<>();
    /**
     * 2D array that contains all the Cells.
     */
    private Cell[][] cellsArr;
    /**
     * Holds a list of all the cells. Used to fill the cellsArr Array.
     */
    private ArrayList<Character> gridArrList = new ArrayList<>();
    /**
     * Width of the grid.
     */
    private int gridWidth = 10;
    /**
     * Height of the grid.
     */
    private int gridHeight = 10;
    /**
     * Contains a number representing the percentage of Cells that need to be bombs.
     */
    private int percentBombs = 10;
    /**
     * Image to use for bomb cells.
     */
    private static Image bombImage = new Image("https://lh4.ggpht.com/d4ThZdKjGANziFu-sr_CElac-kjeZ2LbXeVRkTNk9RJhzb_4qFqGooCprBlalb3yLcgo=w300");
    /**
     * Image to use for flagged cells.
     */
    private static Image flaggedImage = new Image("http://z1035.com/wp-content/uploads/2016/03/Red-Flag.png");
    /**
     * Image to use for questioned cells.
     */
    private static Image questionedImage = new Image("https://vignette.wikia.nocookie.net/pj-masks/images/6/60/Question_Mark.png/revision/latest?cb=20160209134126");
    /**
     * Image to use for when the sound is on.
     */
    private static Image soundOnImage = new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/2/21/Speaker_Icon.svg/2000px-Speaker_Icon.svg.png");
    /**
     * Image to use for when the sound is off.
     */
    private static Image soundOffImage = new Image("https://d30y9cdsu7xlg0.cloudfront.net/png/424336-200.png");
    /**
     * Says whether the player won the game or not.
     */
    private boolean won = false;
    /**
     * Holds the number of normal cells that have been revealed.
     */
    private int numCellsRevealed = 0;
    /**
     * Contains the amount of time that has passed in seconds.
     */
    private int timePassed = 0;
    /**
     * The TimeLine that will be used to update the time Label.
     */
    private Timeline timer = new Timeline();
    /**
     * Says whether or not the sound should be on.
     */
    private boolean soundOn = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sizeOptions.setItems(FXCollections.observableArrayList("10x10", "25x25", "50x25"));
        sizeOptions.getSelectionModel().selectFirst();
        difficultyOptions.setItems(FXCollections.observableArrayList("Easy: 10%", "Medium: 25%", "Hard: 40%"));
        difficultyOptions.getSelectionModel().selectFirst();
        setImage(soundOnImage, sound);
        buildGrid();
        Display.resize();
        grid.getStylesheets().add("resources/styles.css");
        dialog("I implemented the following features:\n" +
                "10pts - Size feature. I believe this is fully functional.\n" +
                "10pts - Difficulty feature. I believe this is also fully functional.\n" +
                "15pts - Sound feature. Works like a charm!");

        setUpSoundHandlers();
        setUpGridHandlers();
        timeHandlers();
        startHandler();
    }

    /**
     * Contains an EventHandler for when the sound button is pressed.
     * If the sound is turned on, the image is changed and the soundOn member variable gets updated to true.
     * If the sound is turned off, the image is changed and the soundOn member variable gets updated to false.
     */
    private void setUpSoundHandlers() {
        sound.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (soundOn) {
                    soundOn = false;
                    setImage(soundOffImage, sound);
                } else {
                    soundOn = true;
                    setImage(soundOnImage, sound);
                }
            }
        });
    }

    /**
     * Contains EventHandlers for when the size and difficulty are changed.
     * If the size is changed, the Cells are removed from grid, the gridHeight and gridWidth are updated with the new values, and resetGame() is called.
     * If the difficultly is changed, the Cells are removed from grid, the percentBombs is updated with the new value, and resetGame() is called.
     */
    private void setUpGridHandlers() {
        sizeOptions.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (int row = 0, i = 0; row < gridHeight; row++) {
                    for (int col = 0; col < gridWidth; col++, i++) {
                        GridPane.clearConstraints(cellsArr[row][col]);
                        grid.getChildren().remove(cellsArr[row][col]);
                    }
                }
                if (sizeOptions.getValue().equals("10x10")) {
                    gridHeight = 10;
                    gridWidth = 10;
                }
                else if (sizeOptions.getValue().equals("25x25")) {
                    gridHeight = 25;
                    gridWidth = 25;
                }
                else if (sizeOptions.getValue().equals("50x25")) {
                    gridHeight = 50;
                    gridWidth = 25;
                }
                resetGame();
            }
        });

        difficultyOptions.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (int row = 0, i = 0; row < gridHeight; row++) {
                    for (int col = 0; col < gridWidth; col++, i++) {
                        GridPane.clearConstraints(cellsArr[row][col]);
                        grid.getChildren().remove(cellsArr[row][col]);
                    }
                }
                if (difficultyOptions.getValue().equals("Easy: 10%")) {
                    percentBombs = 10;
                }
                else if (difficultyOptions.getValue().equals("Medium: 25%")) {
                    percentBombs = 25;
                }
                else {
                    percentBombs = 40;
                }
                resetGame();
            }
        });
    }

    /**
     * gridArrList is filled first with the correct number of bomb cells, then the correct number of normal cells.
     * gridArrList is then shuffled. cellsArr is then iterated through and initialized to the next Cell in gridArrList.
     * The cellsArr is iterated through again and scoreBoard's numNeighbors method is called to set the numNeighbors for each of the cells.
     */
    private void buildGrid() {
        if (start.getText().equals("Reset")) {start.setText("Start");}
        cellsArr = new Cell[gridHeight][gridWidth];
        int numB = 0;
        for (int i = 0; i < (int)((percentBombs/100.0)*(gridWidth*gridHeight)); i++) {
            gridArrList.add('b');
            numB++;
        }
        for (int i = (int)((percentBombs/100.0)*(gridWidth*gridHeight)); i < (gridHeight*gridWidth); i++) {
            gridArrList.add('n');
        }
        bombsLeft.setText(Integer.toString(numB));
        Collections.shuffle(gridArrList);
        for (int row = 0, i = 0; row < gridHeight; row++) {
            for (int col = 0; col < gridWidth; col++, i++) {
                Cell cell;
                if (gridArrList.get(i).equals('b')) {
                    cell = new Cell('b', row, col);
                    bombList.add(cell);
                    setBombHandlers(cell);
                }
                else {
                    cell = new Cell('n', row, col);
                    setNormalHandlers(cell);
                }
                cell.setPrefSize(30,30);
                cell.setId("initial-board-set-up");
                GridPane.setConstraints(cell, row, col);
                grid.getChildren().add(cell);
                cellsArr[row][col] = cell;
            }
        }
        for (int row = 0; row < gridHeight; row++) {
            for (int col = 0; col < gridWidth; col++) {
                if (cellsArr[row][col].getCellLetter() != 'b') {
                    cellsArr[row][col].setNumNeighbors(scoreBoard.getNumNeighbors(cellsArr, cellsArr[row][col], gridHeight, gridWidth));
                } else {
                    cellsArr[row][col].setNumNeighbors(9);
                }
            }
        }
    }

    /**
     * Contains an EventHandler for when a bomb Cell is pressed. If the Cell is right-clicked, the setRightClick method is called.
     * If the Cell is left-clicked and the Cell is neither flagged nor questioned, scoreBoard's bomb sound is played and endGame is called.
     * @param cell The cell that the EventHandler is applied to.
     */
    private void setBombHandlers(Cell cell) {
        cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (start.getText().equals("Start")) {start.setText("Reset");}
                MouseButton button = event.getButton();
                if (button != MouseButton.PRIMARY) {
                    if (!cell.isMouseTransparent()) {
                        setRightClick(cell);
                    }
                } else {
                    if (!cell.isQuestioned() && !cell.isFlagged()) {
                        if (soundOn) {
                            scoreBoard.getBombSounds().play();
                        }
                        endGame();
                    }
                }
            }
        });
    }

    /**
     * Contains an EventHandler for when a normal Cell is pressed. If the Cell is right-clicked, the setRightClick method is called.
     * If the Cell is left-clicked then the timer starts, scoreBoard's normal sound is played, and the numCellsRevealed is incremented.
     * If the Cell's numNeighbors is 0, the cell is set to filled and the floodFill method is called.
     * If the Cell's numNeighors is not 0, then the Cell's text is set to its numNeighbors.
     * If numCellsRevealed is equal to the amount of normal cells, won is set to true, the timer stops, and endGame is called.
     * @param cell The cell that the EventHandler is applied to.
     */
    private void setNormalHandlers(Cell cell) {
        cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (start.getText().equals("Start")) {
                    start.setText("Reset");
                    timer.playFromStart();
                }
                MouseButton button = event.getButton();
                if (button != MouseButton.PRIMARY) {
                    if (!cell.isMouseTransparent()) {
                        setRightClick(cell);
                    }
                }
                else {
                    if (!cell.isFlagged() && !cell.isQuestioned()) {
                        cell.setMouseTransparent(true);
                        cell.setId("normal-cell-revealed");
                        if (soundOn) {
                            scoreBoard.getNormalSounds().play();
                        }
                        numCellsRevealed++;
                        cell.setMouseTransparent(true);
                        if (cell.getNumNeighbors() == 0) {
                            //flood fill that cell
                            cell.setFilled(true);
                            floodFill(cell);
                        } else {
                            cell.setText(Integer.toString(cell.getNumNeighbors()));
                            cell.setFilled(true);
                        }
                        if (numCellsRevealed == Math.ceil((gridHeight*gridWidth)*((100-percentBombs)/100.0))) {
                            won = true;
                            scoreBoard.setEndTime(timePassed);
                            endGame();
                        }
                    }
                }
            }
        });
    }

    /**
     * If a cell is right-clicked, scoreBoard's toggle sound will play. If it's the first cell clicked, the timer will start counting.
     * If the cell is normal, it will be set to flagged and decrementBombsLeft will be called.
     * If the cell is flagged, it will be set to questioned.
     * If the cell is questioned, it will be set back to normal and incrementBombsLeft will be called.
     * @param cell The cell that the right-click action applies to.
     */
    private void setRightClick(Cell cell) {
        if (!cell.isMouseTransparent()) {
            if (soundOn) {
                scoreBoard.getToggleSounds().play();
            }
            if (start.getText().equals("Start")) {
                start.setText("Reset");
                timer.playFromStart();
            }
            if (!cell.isFlagged() && !cell.isQuestioned()) {
                cell.setFlagged(true);
                cell.setId("cell-flagged");
                decrementBombsLeft();
                setImage(flaggedImage, cell);
            } else if (cell.isFlagged()) {
                cell.setFlagged(false);
                cell.setQuestioned(true);
                cell.setId("cell-questioned");
                setImage(questionedImage, cell);
            } else if (cell.isQuestioned()) {
                cell.setQuestioned(false);
                cell.setId("initial-board-set-up");
                incrementBombsLeft();
                setImage(null, cell);
                cell.setText(null);
            }
        }
    }

    /**
     * Checks all the surrounding Cells. If the cell is not already filled, it will fill it and increment numCellsRevealed.
     * If the neighboring cell has a numNeighbor value of 0, floodFill is called for that Cell. Otherwise, the text of that Cell is set to its numNeighbors.
     * @param cell The Cell that is having its neighbors checked.
     */
    private void floodFill(Cell cell) {
        for (int row = cell.getRowPos() - 1; row <= cell.getRowPos() + 1; row++) {
            for (int col = cell.getColPos() - 1; col <= cell.getColPos() + 1; col++) {
                if (row < gridWidth && row >= 0 && col < gridHeight && col >= 0) {
                    if (!cellsArr[row][col].isFilled()) {
                        cellsArr[row][col].setId("normal-cell-revealed");
                        cellsArr[row][col].setFilled(true);
                        numCellsRevealed++;
                        cellsArr[row][col].setMouseTransparent(true);
                        if (cellsArr[row][col].getNumNeighbors() != 0) {
                            cellsArr[row][col].setText(Integer.toString(cellsArr[row][col].getNumNeighbors()));
                        } else {
                            floodFill(cellsArr[row][col]);
                        }
                    }
                }
            }
        }
    }

    /**
     * Contains an EventHandler for the start button.
     * If it is pressed then the timer will start counting and the button will be set to "Reset".
     * If it is set while it says "Reset," resetGame is called.
     */
    private void startHandler() {
        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (start.getText().equals("Start")) {
                    start.setText("Reset");
                    timer.playFromStart();
                }
                else {
                    start.setText("Start");
                    resetGame();
                }
            }
        });
    }

    /**
     * Stops the timer and sets scoreBoard's endTime with the value of timePassed.
     * If won is false, the cellsArr is iterated through and the bombs are revealed (missed ones in red, flagged ones in green).
     * scoreBoard's lose sound is played and its loseMessage is displayed in a dialog.
     * If won is true, the bombs are revealed in green, scoreBoard's win sound is played, and its winMessage is displayed in a dialog.
     */
    private void endGame() {
        timer.stop();
        scoreBoard.setEndTime(timePassed);
        if (!won) {
            for (int row = 0; row < gridHeight; row++) {
                for (int col = 0; col < gridWidth; col++) {
                    if (cellsArr[row][col].getCellLetter() == 'b') {
                        setImage(bombImage, cellsArr[row][col]);
                        if (cellsArr[row][col].isQuestioned() || cellsArr[row][col].isFlagged()) {
                            cellsArr[row][col].setId("won-bomb-cells");
                        } else {
                            cellsArr[row][col].setId("bomb-cell");
                        }
                    } else {
                        if (cellsArr[row][col].isFlagged() || cellsArr[row][col].isQuestioned()) {
                            cellsArr[row][col].setId("incorrectly-marked");
                        }
                    }
                }
            }
            if (soundOn) {
                scoreBoard.getLoseSounds().play();
            }
            dialog(scoreBoard.getLoseMessage());
        } else {
            for (Cell bomb : bombList) {
                bomb.setGraphic(null);
                setImage(bombImage, bomb);
                bomb.setId("won-bomb-cells");
            }
            if (soundOn) {
                scoreBoard.getWinSounds().play();
            }
            dialog(scoreBoard.getWinMessage());
        }
        grid.setMouseTransparent(true);
    }

    /**
     * Creates an alert dialog with a given message.
     * @param message The message to be displayed in the dialog.
     */
    private void dialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.getDialogPane().setPrefSize(400, 200);
        alert.setResizable(true);
        alert.setGraphic(null);
        alert.showAndWait();

        alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent event) {
                resetGame();
            }
        });
    }

    /**
     * Creates a new ImageView with the desired Image and sets the cell to display the Image.
     * @param image The image to be displayed.
     * @param cell The Button or Cell where the image should be displayed.
     */
    private void setImage(Image image, Button cell) {
        if (image == null) {
            cell.setGraphic(null);
            return;
        }
        ImageView view = new ImageView(image);
        view.setFitWidth(20);
        view.setFitHeight(20);
        cell.setPadding(new Insets(0,0,0,0));
        cell.setGraphic(view);
    }

    /**
     * Clears the gridArrList and bombList, stops the timer, resets numCellsRevealed and timePassed and won.
     * Enables start and resizes the grid.
     */
    private void resetGame() {
        gridArrList.clear();
        numCellsRevealed = 0;
        timer.stop();
        timePassed = 0;
        time.setText("0");
        scoreBoard.setEndTime(0);
        bombList.clear();
        start.setDisable(false);
        won = false;
        grid.setMouseTransparent(false);
        buildGrid();
        Display.resize();
    }

    /**
     * Contains an EventHandler for timer. It is set to increment timePassed every second.
     * The time Label is then updated and scoreBoard's endTime is updated with the value of timePasssed.
     */
    private void timeHandlers() {
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            timePassed++;
            time.setText(Integer.toString(timePassed));
            scoreBoard.setEndTime(timePassed);
        }));
    }

    /**
     * Converts the bombsLeft Label to an integer, decrements it and then sets the Label to the new value.
     */
    private void decrementBombsLeft() {
        int left = Integer.parseInt(bombsLeft.getText());
        left--;
        bombsLeft.setText(Integer.toString(left));
    }

    /**
     * Converts the bombsLeft Label to an integer, increments it and then sets the Label to the new value.
     */
    private void incrementBombsLeft() {
        int left = Integer.parseInt(bombsLeft.getText());
        left++;
        bombsLeft.setText(Integer.toString(left));
    }
}
