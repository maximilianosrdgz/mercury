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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by MaxPower on 17/09/2017.
 */
@NoArgsConstructor
@Controller
public class ListClientController implements Initializable {

    
    private MenuController menuController;
    private ProvinceDAO provinceDAO;
    private ClientDAO clientDAO;
    private AlertBuilder alertBuilder;

    @FXML
    private VBox clientListForm;
    @FXML
    private TableView<Client> tblClients;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TableColumn colProvince;
    @FXML
    private TableColumn colAge;
    @FXML
    private TableColumn colBuyer;
    @FXML
    private TableColumn colConsultant;
    @FXML
    private TableColumn colBlacklist;

    @FXML
    private Button btnUpdateClient;
    @FXML
    private TextField txtName;
    @FXML
    private Button btnFilter;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtAge;
    @FXML
    private CheckBox chkBuyer;
    @FXML
    private CheckBox chkConsultant;
    @FXML
    private CheckBox chkBlacklist;
    @FXML
    private ComboBox<Province> cmbProvince;
    @FXML
    private Button btnResetFilter;

    @Autowired
    public ListClientController(MenuController menuController, ProvinceDAO provinceDAO, ClientDAO clientDAO, AlertBuilder alertBuilder) {
        this.menuController = menuController;
        this.provinceDAO = provinceDAO;
        this.clientDAO = clientDAO;
        this.alertBuilder = alertBuilder;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initClientsTable(clientDAO.findAll());
        initProvinceCombo();
        txtId.requestFocus();
    }

    private void initProvinceCombo() {
        ObservableList<Province> provinceList = FXCollections.observableArrayList();
        provinceList.addAll(provinceDAO.findAll());
        cmbProvince.setItems(provinceList);
        cmbProvince.getSelectionModel().select(-1);
    }

    public void initClientsTable(List<Client> clientList) {
        colId.setCellValueFactory(new PropertyValueFactory<Client, String>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Client, String>("email"));
        colProvince.setCellValueFactory(new PropertyValueFactory<Client, String>("province"));
        colAge.setCellValueFactory(new PropertyValueFactory<Client, String>("age"));
        colBuyer.setCellValueFactory(new PropertyValueFactory<Client, String>("buyer"));
        colConsultant.setCellValueFactory(new PropertyValueFactory<Client, String>("consultant"));
        colBlacklist.setCellValueFactory(new PropertyValueFactory<Client, String>("blacklist"));
        ObservableList<Client> clients = FXCollections.observableArrayList();
        clients.addAll(clientList);
        tblClients.setItems(clients);
        tblClients.getSelectionModel().selectFirst();
    }

    public void updateClient(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent)SpringFxmlLoader.load("/update-client.fxml"), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Modificar Cliente");
        stage.setScene(scene);
        stage.show();
    }

    public Client getSelectedClient(){
        return tblClients.getSelectionModel().getSelectedItem();
    }

    public void filterTable(ActionEvent actionEvent) throws IOException {
        int id = 0;
        int age = -1;
        if(!txtId.getText().equals("")) {
            id = Integer.parseInt(txtId.getText());
        }
        if(!txtAge.getText().equals("")) {
            age = Integer.parseInt(txtAge.getText());
        }

        List<Client> results = filterTable(id,
                txtName.getText(), txtEmail.getText(),
                cmbProvince.getSelectionModel().getSelectedItem(), age);
        initClientsTable(results);
    }

    public void filterTableByFlags(ActionEvent actionEvent) {
        List<Client> results = filterByFlags(chkBuyer.isSelected(),
                chkConsultant.isSelected(),
                chkBlacklist.isSelected());
        initClientsTable(results);
    }

    private List<Client> filterTable(int id, String name, String email, Province province, int age) {
        ObservableList<Client> clients = tblClients.getItems();
        return clients.stream()
                .filter(client -> client.getId() == id || id == 0)
                .filter(client -> StringUtils.containsIgnoreCase(
                        Normalizer.normalize(client.getName(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
                        , Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")) || name.equals(""))
                .filter(client -> StringUtils.containsIgnoreCase(
                        Normalizer.normalize(client.getEmail(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
                        , Normalizer.normalize(email, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")) || email.equals(""))
                .filter(client -> client.getProvince().equals(province) || province == null)
                .filter(client -> client.getAge() == age || age == -1)
                .collect(Collectors.toList());
    }

    private List<Client> filterByFlags(boolean buyer, boolean consultant, boolean blacklisted) {
        ObservableList<Client> clients = tblClients.getItems();

        if(buyer && consultant && blacklisted) {
            return clients.stream()
                    .filter(client -> client.getBooleanBuyer() == buyer)
                    .filter(client -> client.getBooleanConsultant() == consultant)
                    .filter(client -> client.getBooleanBlacklisted() == blacklisted)
                    .collect(Collectors.toList());
        }
        else {
            if(buyer && consultant) {
                return clients.stream()
                        .filter(client -> client.getBooleanBuyer() == buyer)
                        .filter(client -> client.getBooleanConsultant() == consultant)
                        .collect(Collectors.toList());
            }
            else {
                if(consultant && blacklisted) {
                    return clients.stream()
                            .filter(client -> client.getBooleanConsultant() == consultant)
                            .filter(client -> client.getBooleanBlacklisted() == blacklisted)
                            .collect(Collectors.toList());
                }
                else {
                    if(buyer && blacklisted) {
                        return clients.stream()
                                .filter(client -> client.getBooleanBuyer() == buyer)
                                .filter(client -> client.getBooleanBlacklisted() == blacklisted)
                                .collect(Collectors.toList());
                    }
                    else {
                        if(buyer) {
                            return clients.stream()
                                    .filter(client -> client.getBooleanBuyer() == buyer)
                                    .collect(Collectors.toList());
                        }
                        else {
                            if(consultant) {
                                return clients.stream()
                                        .filter(client -> client.getBooleanConsultant() == consultant)
                                        .collect(Collectors.toList());
                            }
                            else {
                                if(blacklisted) {
                                    return clients.stream()
                                            .filter(client -> client.getBooleanBlacklisted() == blacklisted)
                                            .collect(Collectors.toList());
                                }
                                else {
                                    return clients.stream()
                                            .filter(client -> client.getBooleanBuyer() == buyer)
                                            .filter(client -> client.getBooleanConsultant() == consultant)
                                            .filter(client -> client.getBooleanBlacklisted() == blacklisted)
                                            .collect(Collectors.toList());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void reloadForm(ActionEvent actionEvent) throws IOException {
        menuController.loadListClientPane(actionEvent);
    }

    public void filterWithEnter(KeyEvent keyEvent) throws IOException {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            filterTable(null);
        }
    }

    public void openCombo(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            cmbProvince.show();
        }
    }
}
