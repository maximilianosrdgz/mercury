package gui.controller;

import dao.StoreDAO;
import domain.Store;
import gui.util.AlertBuilder;
import gui.util.TextFieldUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@NoArgsConstructor
@Controller
public class ConfigStoreController implements Initializable {

    private static final String ALERT_TITLE = "Actualizar Negocio";
    private static final String ALERT_HEADER = "Actualizando información del negocio";

    @FXML
    private VBox configStoreForm;
    @FXML
    private Button btnShowCurrentPassword;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    private StoreDAO storeDAO;
    private AlertBuilder alertBuilder;

    private Store store;
    private boolean isMasked;

    @Autowired
    public ConfigStoreController(StoreDAO storeDAO, AlertBuilder alertBuilder) {
        this.storeDAO = storeDAO;
        this.alertBuilder = alertBuilder;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        store = storeDAO.find(1);
        loadStore(store);
    }

    public void updateStore(ActionEvent actionEvent) {
        if(TextFieldUtils.fieldsFilled(txtEmail, txtPassword)) {
            Alert alert = alertBuilder.buildAlert(Alert.AlertType.CONFIRMATION, ALERT_TITLE,
                    ALERT_HEADER, "¿Confirmar operación?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK) {
                store.setEmail(txtEmail.getText());
                store.setPassword(txtPassword.getText());
                storeDAO.update(store);
                alertBuilder.buildAlert(Alert.AlertType.INFORMATION, ALERT_TITLE,
                        ALERT_HEADER, "Información actualizada correctamente.")
                        .showAndWait();
                closeForm();
            }
        }
        else {
            alertBuilder.buildAlert(Alert.AlertType.INFORMATION, ALERT_TITLE,
                    "Datos incompletos",
                    "Por favor, complete TODOS los datos del producto antes de confirmar.")
                    .showAndWait();
        }
    }

    public void cancel(ActionEvent actionEvent) {
        Alert alert = alertBuilder.buildAlert(Alert.AlertType.CONFIRMATION, ALERT_TITLE,
                "Cancelar Modificación", "¿Desea cancelar la actualización?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            closeForm();
        }
    }

    public void showCurrentPassword(ActionEvent actionEvent) {
        if(isMasked) {
            btnShowCurrentPassword.setText(store.getPassword());
            isMasked = false;
        }
        else {
            btnShowCurrentPassword.setText("Mostrar/Ocultar contraseña actual");
            isMasked = true;
        }
    }

    private void loadStore(Store store) {
        txtEmail.setText(store.getEmail());
        txtPassword.setText(store.getPassword());
        isMasked = true;
    }

    private void closeForm() {
        Stage stage = (Stage) configStoreForm.getScene().getWindow();
        stage.close();
    }
}
