package cs2410.assn8.view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Loads the FXML file and sets up and displays the stage.
 *
 * @author Aylee Andersen
 * @version 1.0
 */
public class Display extends Application {
    /**
     * Main stage for the program.
     */
    private static Stage primaryStage;

    @Override
    public void start(Stage primary) throws Exception{
        primaryStage = primary;
        Parent root = FXMLLoader.load(getClass().getResource("../../../resources/sweeper.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    /**
     * Resizes the stage to fit the scene size.
     */
    public static void resize() {
        primaryStage.sizeToScene();
    }

    /**
     * Launches the program.
     * @param args Program arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
