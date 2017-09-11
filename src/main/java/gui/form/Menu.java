package gui.form;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Menu extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Parent root = FXMLLoader.load(getClass().getResource("/menu.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();*/

        // load main form in to VBox (Root)
        VBox mainPane = (VBox) FXMLLoader.load( getClass().getResource("/menu.fxml" ) );
        // add main form into the scene
        Scene scene = new Scene(mainPane);

        primaryStage.setTitle("Sample FX Multiple Forms");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);    // make the main form fit to the screen
        primaryStage.show();
    }
}
