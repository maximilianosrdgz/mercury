package gui.controller.report;

import dao.ClientDAO;
import domain.Client;
import domain.PurchaseDetail;
import gui.form.SpringFxmlLoader;
import gui.util.ComboBoxLoader;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@NoArgsConstructor
@Controller
public class MainReportController implements Initializable {

    @FXML
    private TableView<Client> tblPurchasesByClient;
    @FXML
    private TableColumn colClientIdPurchases;
    @FXML
    private TableColumn colClientNamePurchases;
    @FXML
    private TableColumn<Client, Integer> colClientProductsPurchases;
    @FXML
    private VBox mainReportForm;
    @FXML
    private Button btnSearchClient;
    @FXML
    private ComboBox<Integer> cmbClientYears;
    @FXML
    private ComboBox<String> cmbClientMonths;
    @FXML
    private ComboBox<Client> cmbClientClients;

    private Client client;

    private ComboBoxLoader comboBoxLoader;
    private ClientDAO clientDAO;

    @Autowired
    public MainReportController(ComboBoxLoader comboBoxLoader, ClientDAO clientDAO) {
        this.comboBoxLoader = comboBoxLoader;
        this.clientDAO = clientDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ComboBoxLoader.initYearsCombo(cmbClientYears);
        ComboBoxLoader.initMonthCombo(cmbClientMonths, 0);
        //initPurchasesByClientTable(clientDAO.findAll());
    }

    public void loadSelectedClient(Client client) {
        this.client = client;
    }
/*
    private void initPurchasesByClientTable(List<Client> clientList) {
        colClientIdPurchases.setCellValueFactory(new PropertyValueFactory<Client, String>("id"));
        colClientNamePurchases.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        colClientProductsPurchases.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Client,Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Client, Integer> data) {
                return new SimpleIntegerProperty(calculateProductQuantity(data.getValue())).asObject();
            }
        });
        ObservableList<Client> clients = FXCollections.observableArrayList();
        clients.addAll(clientList);
        colClientProductsPurchases.setSortType(TableColumn.SortType.DESCENDING);
        tblPurchasesByClient.setItems(clients);
        tblPurchasesByClient.getSortOrder().add(colClientProductsPurchases);
        tblPurchasesByClient.getSelectionModel().selectFirst();
    }

    private int calculateProductQuantity(Client client) {
        Double quantity = client.getPurchases().stream()
                .mapToDouble(p -> p.getPurchaseDetails().stream()
                        .mapToDouble(PurchaseDetail::getQuantity)
                        .sum())
                .sum();
        return quantity.intValue();
    }*/

    public void openPickClientForm(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/clients/pick-client-report.fxml"), 1130, 600);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar Cliente");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainReportForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }
}
