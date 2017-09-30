package gui.form;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Menu extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox mainPane = (VBox) SpringFxmlLoader.load("/menu.fxml");
        Scene scene = new Scene(mainPane);
        primaryStage.setTitle("Mercury");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);    // make the main form fit to the screen
        primaryStage.show();
    }
}
