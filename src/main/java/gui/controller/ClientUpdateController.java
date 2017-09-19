package gui.controller;

import dao.ClientDAO;
import dao.ProvinceDAO;
import domain.Client;
import domain.Province;
import gui.form.SpringFxmlLoader;
import gui.util.AlertBuilder;
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

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

@NoArgsConstructor
@Controller
public class ClientUpdateController implements Initializable {

    private ClientListController listController;
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
    public ClientUpdateController(ClientListController listController, ProvinceDAO provinceDAO,
                                  ClientDAO clientDAO, AlertBuilder alertBuilder) {
        this.listController = listController;
        this.provinceDAO = provinceDAO;
        this.clientDAO = clientDAO;
        this.alertBuilder = alertBuilder;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initProvinceCombo();
        initBirthYearsCombo();

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

    private void initBirthYearsCombo() {
        ObservableList<Integer> years = FXCollections.observableArrayList();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1940; i--) {
            years.add(i);
        }
        cmbBirthYears.setItems(years);
        cmbBirthYears.getSelectionModel().selectFirst();
    }

    public void updateClient(ActionEvent actionEvent) {
        Client client = buildClient();
        clientDAO.update(client);
        listController.initClientsTable();
        alertBuilder.builder()
                .type(Alert.AlertType.INFORMATION)
                .title("Modificar Cliente")
                .headerText("Cliente modificado exitosamente")
                .contentText("Cliente modificado: " + client.getName())
                .build()
                .showAndWait();
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
