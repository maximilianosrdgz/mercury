package gui.controller;

import controller.EntityManagerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by MaxPower on 10/09/2017.
 */
public class ClientLoadController implements Initializable {
    public Button btnSave;
    public Button btnClose;
    private EntityManagerUtils emu = new EntityManagerUtils();

    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void saveClient(ActionEvent actionEvent) {
        emu.saveClient();
        buildAlert("Client Saved", emu.findClient(1).getName()).showAndWait();
    }

    public void closeForm(ActionEvent actionEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void saveCategory(ActionEvent actionEvent) {
        emu.saveCategory();
    }

    public void saveMaterial(ActionEvent actionEvent) {
        emu.saveMaterial();
    }

    public void saveProduct(ActionEvent actionEvent) {
        emu.saveProduct();
    }

    private Alert buildAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(header);
        alert.setContentText(content);

        return alert;
    }
}
