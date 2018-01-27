package gui.controller.supplier;

import dao.SupplierDAO;
import domain.Supplier;
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
public class ObservationSupplierController implements Initializable {

    @FXML
    private Button btnSaveObservation;
    @FXML
    private Button btnCancel;
    @FXML
    private TextArea txtObservation;

    private Supplier supplier;
    private ListSupplierController listSupplierController;
    private SupplierDAO supplierDAO;
    private AlertBuilder alertBuilder;

    @Autowired
    public ObservationSupplierController(ListSupplierController listSupplierController, SupplierDAO supplierDAO,
                                         AlertBuilder alertBuilder) {

        this.listSupplierController = listSupplierController;
        this.supplierDAO = supplierDAO;
        this.alertBuilder = alertBuilder;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        supplier = listSupplierController.getSelectedSupplier();
        txtObservation.setText(supplier.getObservations());
    }

    public void saveObservation(ActionEvent actionEvent) {
        Alert alert = alertBuilder.builder()
                .type(Alert.AlertType.CONFIRMATION)
                .title("Modificar Proveedor")
                .headerText("Está por modificar el proveedor: \n" + supplier.getName())
                .contentText("¿Confirmar operación?")
                .build();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            supplier.setObservations(txtObservation.getText());
            supplierDAO.update(supplier);
            closeForm(actionEvent);
        }
    }

    public void cancel(ActionEvent actionEvent) {
        Alert alert = alertBuilder.builder()
                .type(Alert.AlertType.CONFIRMATION)
                .title("Modificar Proveedor")
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
