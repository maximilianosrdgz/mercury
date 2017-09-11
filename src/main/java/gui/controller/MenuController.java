package gui.controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MenuController {

    @FXML
    private Label helloWorld;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnLoadClient;

    public void sayHelloWorld(ActionEvent actionEvent) {
        helloWorld.setText("Hello World!");
    }

    public void closeForm(ActionEvent actionEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void openLoadClient(ActionEvent actionEvent) throws IOException {
        Stage loadClient = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/client-load.fxml"));
        loadClient.setTitle("Cargar Cliente");
        loadClient.setScene(new Scene(root, 300, 275));
        loadClient.show();
    }

    @FXML
    private Button btnMenuBar;

    @FXML
    private Button openFrm2;

    @FXML
    private Button openFrm3;

    @FXML
    private VBox dataPane;

    public void setDataPane(Node node) {
        // update VBox with new form(FXML) depends on which button is clicked
        dataPane.getChildren().setAll(node);
    }

    public VBox fadeAnimate(String url) throws IOException {
        VBox v = (VBox) FXMLLoader.load(getClass().getResource(url));
        FadeTransition ft = new FadeTransition(Duration.millis(1500));
        ft.setNode(v);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
        return v;
    }

    public void loadPane(ActionEvent event) throws IOException {
        setDataPane(fadeAnimate("/client-load.fxml"));
    }

    public void loadPane2(ActionEvent event) throws IOException {
        setDataPane(fadeAnimate("/samplefx/view/FXML2.fxml"));
    }

    public void loadPane3(ActionEvent event) throws IOException {
        setDataPane(fadeAnimate("/samplefx/view/FXML3.fxml"));
    }
}
