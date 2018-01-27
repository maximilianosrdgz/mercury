package gui.controller.client;

import dao.ClientDAO;
import domain.Client;
import gui.util.AlertBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by MaxPower on 26/01/2018.
 */
@NoArgsConstructor
@Controller
public class ObservationClientController implements Initializable {

    @FXML
    private Button btnSaveObservation;
    @FXML
    private Button btnCancel;
    @FXML
    private TextArea txtObservation;

    private Client client;
    private ClientDAO clientDAO;
    private ListClientController listClientController;
    private AlertBuilder alertBuilder;

    @Autowired
    public ObservationClientController(ListClientController listClientController, ClientDAO clientDAO,
                                       AlertBuilder alertBuilder) {
        this.listClientController = listClientController;
        this.clientDAO = clientDAO;
        this.alertBuilder = alertBuilder;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = listClientController.getSelectedClient();

        txtObservation.setText(client.getObservations());
    }

    public void saveObservation(ActionEvent actionEvent) {
        Alert alert = alertBuilder.builder()
                .type(Alert.AlertType.CONFIRMATION)
                .title("Modificar Cliente")
                .headerText("Está por modificar el cliente: \n" + client.getName())
                .contentText("¿Confirmar operación?")
                .build();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            client.setObservations(txtObservation.getText());
            clientDAO.update(client);
            closeForm(actionEvent);
        }
    }

    public void cancel(ActionEvent actionEvent) {
        Alert alert = alertBuilder.builder()
                .type(Alert.AlertType.CONFIRMATION)
                .title("Modificar Cliente")
                .headerText("Está por cancelar la operación")
                .contentText("¿Cancelar SIN guardar?")
                .build();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            closeForm(actionEvent);
        }
    }

    private void closeForm(ActionEvent actionEvent) {
        Stage stage = (Stage) btnSaveObservation.getScene().getWindow();
        stage.close();
    }
}
