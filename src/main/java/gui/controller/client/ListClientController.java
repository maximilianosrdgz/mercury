package gui.controller.client;

import dao.ClientDAO;
import domain.Client;
import domain.Province;
import gui.controller.report.CategoryReportController;
import gui.controller.report.ClientReportController;
import gui.controller.MenuController;
import gui.controller.purchase.NewPurchaseController;
import gui.controller.email.SendEmailController;
import gui.controller.report.ProductReportController;
import gui.form.SpringFxmlLoader;
import gui.util.ComboBoxLoader;
import gui.util.TextFieldUtils;
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
import javafx.stage.Modality;
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

    @FXML
    private Button btnObservations;
    @FXML
    private Button btnSelectClientReport;
    @FXML
    private Button btnSelectClient;
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
    private TableColumn colReceiver;
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

    private MenuController menuController;
    private ClientDAO clientDAO;
    private NewPurchaseController newPurchaseController;
    private ComboBoxLoader comboBoxLoader;
    private SendEmailController sendEmailController;
    private ClientReportController clientReportController;
    private ProductReportController productReportController;
    private CategoryReportController categoryReportController;

    @Autowired
    public ListClientController(MenuController menuController, ClientDAO clientDAO,
                                NewPurchaseController newPurchaseController, ComboBoxLoader comboBoxLoader,
                                SendEmailController sendEmailController, ClientReportController clientReportController,
                                ProductReportController productReportController, CategoryReportController categoryReportController) {

        this.menuController = menuController;
        this.clientDAO = clientDAO;
        this.newPurchaseController = newPurchaseController;
        this.comboBoxLoader = comboBoxLoader;
        this.sendEmailController = sendEmailController;
        this.clientReportController = clientReportController;
        this.productReportController = productReportController;
        this.categoryReportController = categoryReportController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TextFieldUtils.setNumericOnly(txtId, txtAge);
        initClientsTable(clientDAO.findAll());
        comboBoxLoader.initProvinceCombo(cmbProvince, -1);
        txtId.requestFocus();
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
        colReceiver.setCellValueFactory(new PropertyValueFactory<Client, String>("receiver"));
        ObservableList<Client> clients = FXCollections.observableArrayList();
        clients.addAll(clientList);
        tblClients.setItems(clients);
        tblClients.getSelectionModel().selectFirst();
    }

    public void updateClient(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent)SpringFxmlLoader.load("/forms/clients/update-client.fxml"), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Modificar Cliente");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(clientListForm.getScene().getWindow());
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

    public void selectClientPurchase(ActionEvent actionEvent) {
        newPurchaseController.loadSelectedClient(getSelectedClient());
        Stage stage = (Stage) btnSelectClient.getScene().getWindow();
        stage.close();
    }

    public void selectClientEmail(ActionEvent actionEvent) {
        sendEmailController.loadSelectedClient(getSelectedClient());
        Stage stage = (Stage) btnSelectClient.getScene().getWindow();
        stage.close();
        sendEmailController.updateClientCount();
    }

    public void selectClientReport(ActionEvent actionEvent) {
        clientReportController.loadSelectedClient(getSelectedClient());
        Stage stage = (Stage) btnSelectClientReport.getScene().getWindow();
        stage.close();
    }

    public void selectClientProductReport(ActionEvent actionEvent) {
        productReportController.loadSelectedClient(getSelectedClient());
        Stage stage = (Stage) btnSelectClientReport.getScene().getWindow();
        stage.close();
    }

    public void selectClientCategoryReport(ActionEvent actionEvent) {
        categoryReportController.loadSelectedClient(getSelectedClient());
        Stage stage = (Stage) btnSelectClientReport.getScene().getWindow();
        stage.close();
    }

    public void updateObservations(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent)SpringFxmlLoader.load("/forms/clients/observations-client.fxml"), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Actualizar Observaciones");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(clientListForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }
}
