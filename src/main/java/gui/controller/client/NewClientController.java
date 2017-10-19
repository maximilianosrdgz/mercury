package gui.controller.client;

import gui.util.AlertBuilder;
import gui.util.ComboBoxLoader;
import gui.util.TextFieldUtils;
import javafx.scene.control.ButtonType;
import dao.ClientDAO;
import dao.ProvinceDAO;
import domain.Client;
import domain.Province;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by MaxPower on 10/09/2017.
 */
@NoArgsConstructor
@Controller
public class NewClientController implements Initializable {

    @FXML
    private ComboBox<Province> cmbProvinces;
    @FXML
    private ComboBox<Integer> cmbBirthYears;
    @FXML
    private Button btnSaveClient;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private CheckBox chkBlacklisted;
    @FXML
    private CheckBox chkBuyer;
    @FXML
    private CheckBox chkConsultant;
    @FXML
    private CheckBox chkReceiver;

    private ClientDAO clientDAO;
    private ProvinceDAO provinceDAO;
    private AlertBuilder alertBuilder;

    @Autowired
    public NewClientController(ClientDAO clientDAO, ProvinceDAO provinceDAO,
                               AlertBuilder alertBuilder) {

        this.clientDAO = clientDAO;
        this.provinceDAO = provinceDAO;
        this.alertBuilder = alertBuilder;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initProvinceCombo();
        ComboBoxLoader.initYearsCombo(cmbBirthYears);
    }

    public void saveClient(ActionEvent actionEvent) {
        if(TextFieldUtils.fieldsFilled(txtName, txtEmail)) {
            Client client = buildClient();
            Alert alert = alertBuilder.builder()
                    .type(Alert.AlertType.CONFIRMATION)
                    .title("Nuevo Cliente")
                    .headerText("Guardando nuevo cliente: \n" + client.getName())
                    .contentText("¿Confirmar operación?")
                    .build();
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                clientDAO.create(client);
                alertBuilder.builder()
                        .type(Alert.AlertType.INFORMATION)
                        .title("Guardar Cliente")
                        .headerText("Cliente guardado exitosamente")
                        .contentText("Nuevo Cliente: " + clientDAO.find(client.getId()).getName())
                        .build()
                        .showAndWait();
                clearForm();
            }
        }
        else {
            alertBuilder.builder()
                    .type(Alert.AlertType.INFORMATION)
                    .title("Guardar Cliente")
                    .headerText("Datos incompletos")
                    .contentText("Por favor, complete TODOS los datos del cliente antes de confirmar.")
                    .build()
                    .showAndWait();
        }
    }

    private void clearForm() {
        this.txtName.setText("");
        this.txtEmail.setText("");
        this.cmbProvinces.getSelectionModel().selectFirst();
        this.cmbBirthYears.getSelectionModel().selectFirst();
        this.chkConsultant.setSelected(false);
        this.chkBuyer.setSelected(false);
        this.chkBlacklisted.setSelected(false);
    }

    private Client buildClient() {
        return Client.builder()
                .name(txtName.getText())
                .email(txtEmail.getText())
                .province(cmbProvinces.getSelectionModel().getSelectedItem())
                .birthYear((Integer) cmbBirthYears.getSelectionModel().getSelectedItem())
                .purchases(new ArrayList<>())
                .buyer(chkBuyer.isSelected())
                .consultant(chkConsultant.isSelected())
                .blackListed(chkBlacklisted.isSelected())
                .receiver(chkReceiver.isSelected())
                .build();
    }

    private void initProvinceCombo() {
        ObservableList<Province> provinceList = FXCollections.observableArrayList();
        provinceList.addAll(provinceDAO.findAll());
        cmbProvinces.setItems(provinceList);
        cmbProvinces.getSelectionModel().selectFirst();
    }

    public void checkConsultant(ActionEvent actionEvent) {
        chkConsultant.setSelected(true);
    }
}
