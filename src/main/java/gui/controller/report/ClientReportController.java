package gui.controller.report;

import dao.ClientDAO;
import dao.ProvinceDAO;
import dao.PurchaseDAO;
import domain.Client;
import domain.Province;
import domain.Purchase;
import domain.PurchaseDetail;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@NoArgsConstructor
@Controller
public class ClientReportController implements Initializable {

    @FXML
    private TableColumn<Province, Integer> colProvincePurchases;
    @FXML
    private TableColumn colClientIdPurchases;
    @FXML
    private TableColumn colClientIdProducts;
    @FXML
    private VBox clientReportForm;
    @FXML
    private Button btnSearchClient;
    @FXML
    private TextField txtClient;
    @FXML
    private Button btnApplyFilter;
    @FXML
    private Button btnResetFilter;
    @FXML
    private DatePicker dtpFrom;
    @FXML
    private DatePicker dtpTo;
    @FXML
    private TableView<Province> tblProvinces;
    @FXML
    private TableColumn colProvinceName;
    @FXML
    private TableColumn<Province, Integer> colProvinceProducts;
    @FXML
    private TableView<Client> tblClientsPurchases;
    @FXML
    private TableColumn colClientNamePurchases;
    @FXML
    private TableColumn<Client, Integer> colClientPurchases;
    @FXML
    private TableView<Client> tblClientsProducts;
    @FXML
    private TableColumn colClientNameProducts;
    @FXML
    private TableColumn<Client, Integer> colClientProducts;

    private List<Client> allClients;
    private List<Province> allProvinces;

    private ProvinceDAO provinceDAO;
    private ClientDAO clientDAO;

    @Autowired
    public ClientReportController(ProvinceDAO provinceDAO, ClientDAO clientDAO) {
        this.provinceDAO = provinceDAO;
        this.clientDAO = clientDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDatePickers();
        allClients = clientDAO.findAll();
        allProvinces = provinceDAO.findAll();
        initProvinceTable(allProvinces, LocalDate.now().minusYears(100), LocalDate.now().plusYears(100));
        initClientsProductsTable(allClients, LocalDate.now().minusYears(100), LocalDate.now().plusYears(100));
        initClientsPurchasesTable(allClients);
    }

    private void initDatePickers() {
        dtpFrom.setValue(LocalDate.now());
        dtpTo.setValue(LocalDate.now());
        dtpFrom.setEditable(false);
        dtpTo.setEditable(false);
    }

    private void initProvinceTable(List<Province> provinceList, LocalDate dateFrom, LocalDate dateTo) {
        tblProvinces.getItems().clear();
        colProvinceName.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        colProvincePurchases.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Province,Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Province, Integer> data) {
                return new SimpleIntegerProperty(calculateProvincePurchases(data.getValue(), dateFrom, dateTo)).asObject();
            }
        });
        colProvinceProducts.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Province,Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Province, Integer> data) {
                return new SimpleIntegerProperty(calculateProvinceProducts(data.getValue(), dateFrom, dateTo)).asObject();
            }
        });
        ObservableList<Province> provinces = FXCollections.observableArrayList();
        provinces.addAll(provinceList);
        colProvincePurchases.setSortType(TableColumn.SortType.DESCENDING);
        tblProvinces.setItems(provinces);
        tblProvinces.getSortOrder().add(colProvincePurchases);
        tblProvinces.getSelectionModel().selectFirst();
    }

    private void initClientsProductsTable(List<Client> clientList, LocalDate dateFrom, LocalDate dateTo) {
        colClientIdProducts.setCellValueFactory(new PropertyValueFactory<Client, String>("id"));
        colClientNameProducts.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        colClientProducts.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Client,Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Client, Integer> data) {
                return new SimpleIntegerProperty(calculateProductQuantity(data.getValue(), dateFrom, dateTo)).asObject();
            }
        });
        ObservableList<Client> clients = FXCollections.observableArrayList();
        clients.addAll(clientList);
        colClientProducts.setSortType(TableColumn.SortType.DESCENDING);
        tblClientsProducts.setItems(clients);
        tblClientsProducts.getSortOrder().add(colClientProducts);
        tblClientsProducts.getSelectionModel().selectFirst();
    }

    private void initClientsPurchasesTable(List<Client> clientList) {
        colClientIdPurchases.setCellValueFactory(new PropertyValueFactory<Client, String>("id"));
        colClientNamePurchases.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        colClientPurchases.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Client,Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Client, Integer> data) {
                return new SimpleIntegerProperty(data.getValue().getPurchases().size()).asObject();
            }
        });
        ObservableList<Client> clients = FXCollections.observableArrayList();
        clients.addAll(clientList);
        colClientPurchases.setSortType(TableColumn.SortType.DESCENDING);
        tblClientsPurchases.setItems(clients);
        tblClientsPurchases.getSortOrder().add(colClientPurchases);
        tblClientsPurchases.getSelectionModel().selectFirst();
    }

    private int calculateProductQuantity(Client client, LocalDate localDateFrom, LocalDate localDateTo) {
        Date dateFrom = java.sql.Date.valueOf(localDateFrom);
        Date dateTo = java.sql.Date.valueOf(localDateTo);
        Double quantity = client.getPurchases().stream()
                .mapToDouble(p -> p.getPurchaseDetails().stream()
                        .mapToDouble(PurchaseDetail::getQuantity)
                        .sum())
                .sum();
        return quantity.intValue();
    }

    private int calculateProvincePurchases(Province province, LocalDate localDateFrom, LocalDate localDateTo) {
        Date dateFrom = java.sql.Date.valueOf(localDateFrom);
        Date dateTo = java.sql.Date.valueOf(localDateTo);
        return allClients.stream()
                .filter(c -> c.getProvince().equals(province))
                .mapToInt(c -> c.getPurchases().stream()
                        .filter(p -> p.getDate().compareTo(dateFrom) >= 0 && p.getDate().compareTo(dateTo) <= 0)
                        .collect(Collectors.toList())
                        .size())
                .sum();
    }

    private int calculateProvinceProducts(Province province, LocalDate localDateFrom, LocalDate localDateTo) {
        Date dateFrom = java.sql.Date.valueOf(localDateFrom);
        Date dateTo = java.sql.Date.valueOf(localDateTo);
        Double quantity = 0d;
        for(Client c : allClients) {
            if(c.getProvince().equals(province)) {
                quantity += c.getPurchases().stream()
                        .filter(p -> p.getDate().compareTo(dateFrom) >= 0 && p.getDate().compareTo(dateTo) <= 0)
                        .mapToDouble(p -> p.getPurchaseDetails().stream()
                                .mapToDouble(PurchaseDetail::getQuantity)
                                .sum())
                        .sum();
            }
        }
        return quantity.intValue();
    }

    public void openFormPickClient(ActionEvent actionEvent) {
    }

    public void applyFilter(ActionEvent actionEvent) {
        initProvinceTable(allProvinces, dtpFrom.getValue(), dtpTo.getValue());
    }

    public void resetFilter(ActionEvent actionEvent) {
    }
}
