package gui.controller;

import dao.ClientDAO;
import dao.ProvinceDAO;
import domain.Client;
import domain.Province;
import gui.util.AlertBuilder;
import gui.util.ComboBoxLoader;
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
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@NoArgsConstructor
@Controller
public class UpdateClientController implements Initializable {

    private ListClientController listController;
    private MenuController menuController;
    private ProvinceDAO provinceDAO;
    private ClientDAO clientDAO;
    private AlertBuilder alertBuilder;

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private Button btnUpdateClient;
    @FXML
    private ComboBox<Province> cmbProvinces;
    @FXML
    private ComboBox cmbBirthYears;
    @FXML
    private CheckBox chkBlacklisted;
    @FXML
    private CheckBox chkBuyer;
    @FXML
    private CheckBox chkConsultant;

    @Autowired
    public UpdateClientController(ListClientController listController,
                                  MenuController menuController, ProvinceDAO provinceDAO,
                                  ClientDAO clientDAO, AlertBuilder alertBuilder) {
        this.listController = listController;
        this.menuController = menuController;
        this.provinceDAO = provinceDAO;
        this.clientDAO = clientDAO;
        this.alertBuilder = alertBuilder;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initProvinceCombo();
        ComboBoxLoader.initBirthYearsCombo(cmbBirthYears);

        Client client = listController.getSelectedClient();

        txtId.setText(String.valueOf(client.getId()));
        txtName.setText(client.getName());
        txtEmail.setText(client.getEmail());
        cmbProvinces.getSelectionModel().select(client.getProvince());
        cmbBirthYears.getSelectionModel().select(new Integer(client.getBirthYear()));
        chkBlacklisted.setSelected(client.isBlackListed());
        chkBuyer.setSelected(client.getBooleanBuyer());
        chkConsultant.setSelected(client.getBooleanConsultant());
    }

    private void initProvinceCombo() {
        ObservableList<Province> provinceList = FXCollections.observableArrayList();
        provinceList.addAll(provinceDAO.findAll());
        cmbProvinces.setItems(provinceList);
        cmbProvinces.getSelectionModel().selectFirst();
    }

    public void updateClient(ActionEvent actionEvent) throws IOException {
        Client client = buildClient();
        clientDAO.update(client);
        alertBuilder.builder()
                .type(Alert.AlertType.INFORMATION)
                .title("Modificar Cliente")
                .headerText("Cliente modificado exitosamente")
                .contentText("Cliente modificado: " + client)
                .build()
                .showAndWait();
        menuController.loadListClientPane(actionEvent);
        Stage stage = (Stage) btnUpdateClient.getScene().getWindow();
        stage.close();
    }

    private Client buildClient() {
        return Client.builder()
                .id(Integer.parseInt(txtId.getText()))
                .name(txtName.getText())
                .email(txtEmail.getText())
                .province(cmbProvinces.getSelectionModel().getSelectedItem())
                .birthYear((Integer) cmbBirthYears.getSelectionModel().getSelectedItem())
                .buyer(chkBuyer.isSelected())
                .consultant(chkConsultant.isSelected())
                .blackListed(chkBlacklisted.isSelected())
                .build();
    }
}
