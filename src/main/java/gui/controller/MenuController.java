package gui.controller;

import gui.form.SpringFxmlLoader;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class MenuController {

    @FXML
    private Button btnClose;
    @FXML
    private Button openFrm3;
    @FXML
    private VBox dataPane;
    @FXML
    private Button btnOpenFormClientLoad;
    @FXML
    private Button btnClientList;

    public void setDataPane(Node node) {
        // update VBox with new form(FXML) depends on which button is clicked
        dataPane.getChildren().setAll(node);
    }

    public VBox fadeAnimate(String url) throws IOException {
        VBox v = (VBox) SpringFxmlLoader.load(url);
        FadeTransition ft = new FadeTransition(Duration.millis(500));
        ft.setNode(v);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
        return v;
    }

    public void closeForm(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void loadPane(ActionEvent event) throws IOException {
        setDataPane(fadeAnimate("/new-client.fxml"));
    }

    public void loadNewClientPane(ActionEvent event) throws IOException {
        setDataPane(fadeAnimate("/new-client.fxml"));
    }

    public void loadPane3(ActionEvent event) throws IOException {
        setDataPane(fadeAnimate("/FXML3.fxml"));
    }

    public void loadClientListPane(ActionEvent actionEvent) throws IOException {
        setDataPane(fadeAnimate("/client-list.fxml"));
    }
}
