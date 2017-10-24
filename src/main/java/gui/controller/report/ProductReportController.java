package gui.controller.report;

import dao.ClientDAO;
import dao.ProductDAO;
import dao.PurchaseDAO;
import domain.Client;
import domain.Product;
import domain.Purchase;
import domain.PurchaseDetail;
import gui.controller.MenuController;
import gui.form.SpringFxmlLoader;
import gui.util.DatePickerUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@NoArgsConstructor
@Controller
public class ProductReportController implements Initializable {

    @FXML
    private PieChart pieChartPercentage;
    @FXML
    private VBox productReportForm;
    @FXML
    private Button btnSearchProduct;
    @FXML
    private TextField txtProduct;
    @FXML
    private Button btnApplyFilter;
    @FXML
    private Button btnResetFilter;
    @FXML
    private DatePicker dtpFrom;
    @FXML
    private DatePicker dtpTo;
    @FXML
    private TableView<Product> tblProductsByClient;
    @FXML
    private TableColumn colIdByClient;
    @FXML
    private TableColumn colDescriptionByClient;
    @FXML
    private TableColumn<Product, Integer> colQuantityByClient;
    @FXML
    private Button btnSearchClient;
    @FXML
    private TableView<Product> tblQuantity;
    @FXML
    private TableColumn colIdQuantity;
    @FXML
    private TableColumn colDescriptionQuantity;
    @FXML
    private TableColumn<Product, Integer> colQuantity;
    @FXML
    private TableView<Product> tblPercentage;
    @FXML
    private TableColumn colIdPercentage;
    @FXML
    private TableColumn colDescriptionPercentage;
    @FXML
    private TableColumn<Product, Integer> colPercentage;

    private List<Purchase> allPurchases;
    private List<Product> allProducts;
    private List<Product> productsByClient;
    private List<Client> allClients;
    private List<Purchase> purchasesByClient;
    private Product product;
    private ObservableList<PieChart.Data> chartData;

    private PurchaseDAO purchaseDAO;
    private ProductDAO productDAO;
    private MenuController menuController;
    private ClientDAO clientDAO;

    @Autowired
    public ProductReportController(PurchaseDAO purchaseDAO, ProductDAO productDAO,
                                   MenuController menuController, ClientDAO clientDAO) {

        this.purchaseDAO = purchaseDAO;
        this.productDAO = productDAO;
        this.menuController = menuController;
        this.clientDAO = clientDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        product = null;
        chartData = FXCollections.observableArrayList();
        pieChartPercentage.setTitle("Porcentaje de Artículos Vendidos");
        pieChartPercentage.setData(chartData);
        initDatePickers();
        allPurchases = purchaseDAO.findAll();
        allProducts = productDAO.findAll();
        allClients = clientDAO.findAll();
        productsByClient = new ArrayList<>();
        purchasesByClient = new ArrayList<>();
        initQuantityTable(allProducts, LocalDate.now().minusYears(100), LocalDate.now().plusYears(100));
        //initPercentageTable(allProducts, LocalDate.now().minusYears(100), LocalDate.now().plusYears(100));
        initProductsByClientTable(productsByClient, LocalDate.now().minusYears(100), LocalDate.now().plusYears(100));
        initPercentageChart(allProducts, LocalDate.now().minusYears(100), LocalDate.now().plusYears(100));
    }

    private void initDatePickers() {
        DatePickerUtils.initPickers(LocalDate.now().minusYears(15), dtpFrom);
        DatePickerUtils.initPickers(LocalDate.now(), dtpTo);
        DatePickerUtils.editable(false, dtpFrom, dtpTo);
    }

    private void initQuantityTable(List<Product> productList, LocalDate dateFrom, LocalDate dateTo) {
        tblQuantity.getItems().clear();
        colIdQuantity.setCellValueFactory(new PropertyValueFactory<Product, String>("id"));
        colDescriptionQuantity.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        colQuantity.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product,Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Product, Integer> data) {
                return new SimpleIntegerProperty(calculateProductQuantity(data.getValue(), dateFrom, dateTo)).asObject();
            }
        });
        ObservableList<Product> products = FXCollections.observableArrayList();
        products.addAll(productList);
        colQuantity.setSortType(TableColumn.SortType.DESCENDING);
        tblQuantity.setItems(products);
        tblQuantity.getSortOrder().add(colQuantity);
        tblQuantity.getSelectionModel().selectFirst();
    }
    /*
    private void initPercentageTable(List<Product> productList, LocalDate dateFrom, LocalDate dateTo) {
        tblPercentage.getItems().clear();
        colIdPercentage.setCellValueFactory(new PropertyValueFactory<Product, String>("id"));
        colDescriptionPercentage.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        colPercentage.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product,Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Product, Integer> data) {
                return new SimpleIntegerProperty(calculateProductPercentage(data.getValue(), dateFrom, dateTo)).asObject();
            }
        });
        ObservableList<Product> products = FXCollections.observableArrayList();
        products.addAll(productList);
        colPercentage.setSortType(TableColumn.SortType.DESCENDING);
        tblPercentage.setItems(products);
        tblPercentage.getSortOrder().add(colPercentage);
        tblPercentage.getSelectionModel().selectFirst();
    }*/

    private void initPercentageChart(List<Product> productList, LocalDate dateFrom, LocalDate dateTo) {
        pieChartPercentage.getData().clear();
        productList.forEach(p -> {
            if(calculateProductPercentage(p, dateFrom, dateTo) >= 1) {
                pieChartPercentage.getData().add(
                        new PieChart.Data(p.getDescription(), calculateProductPercentage(p, dateFrom, dateTo))
                );
            }
        });
    }

    private void initProductsByClientTable(List<Product> productList, LocalDate dateFrom, LocalDate dateTo) {
        tblProductsByClient.getItems().clear();
        colIdByClient.setCellValueFactory(new PropertyValueFactory<Product, String>("id"));
        colDescriptionByClient.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        colQuantityByClient.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product,Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Product, Integer> data) {
                return new SimpleIntegerProperty(calculateQuantityByClient(data.getValue(), dateFrom, dateTo)).asObject();
            }
        });
        ObservableList<Product> products = FXCollections.observableArrayList();
        products.addAll(productList);
        tblProductsByClient.setItems(products);
        tblProductsByClient.getSelectionModel().selectFirst();
    }

    private int calculateProductQuantity(Product product, LocalDate localDateFrom, LocalDate localDateTo) {
        Date dateFrom = java.sql.Date.valueOf(localDateFrom);
        Date dateTo = java.sql.Date.valueOf(localDateTo);
        Double quantity = allPurchases.stream()
                .filter(p -> !p.getBooleanCanceled())
                .filter(p -> p.getDate().compareTo(dateFrom) >= 0 && p.getDate().compareTo(dateTo) <= 0)
                .mapToDouble(p -> p.getPurchaseDetails().stream()
                        .filter(d -> d.getProduct().getId() == product.getId())
                        .mapToDouble(PurchaseDetail::getQuantity)
                        .sum())
                .sum();
        return quantity.intValue();
    }

    private int calculateProductPercentage(Product product, LocalDate localDateFrom, LocalDate localDateTo) {
        Date dateFrom = java.sql.Date.valueOf(localDateFrom);
        Date dateTo = java.sql.Date.valueOf(localDateTo);
        double productQuantity = calculateProductQuantity(product, localDateFrom, localDateTo);
        double totalQuantity = allPurchases.stream()
                .filter(p -> !p.getBooleanCanceled())
                .filter(p -> p.getDate().compareTo(dateFrom) >= 0 && p.getDate().compareTo(dateTo) <= 0)
                .mapToDouble(p -> p.getPurchaseDetails().stream()
                        .mapToDouble(PurchaseDetail::getQuantity)
                        .sum())
                .sum();
        return (int) Math.round(productQuantity * 100 / totalQuantity);
    }

    private int calculateQuantityByClient(Product product, LocalDate localDateFrom, LocalDate localDateTo) {
        Date dateFrom = java.sql.Date.valueOf(localDateFrom);
        Date dateTo = java.sql.Date.valueOf(localDateTo);
        Double quantity = purchasesByClient.stream()
                .filter(p -> !p.getBooleanCanceled())
                .filter(p -> p.getDate().compareTo(dateFrom) >= 0 && p.getDate().compareTo(dateTo) <= 0)
                .mapToDouble(p -> p.getPurchaseDetails().stream()
                        .filter(d -> d.getProduct().getId() == product.getId())
                        .mapToDouble(PurchaseDetail::getQuantity)
                        .sum())
                .sum();
        return quantity.intValue();
    }

    public void loadSelectedClient(Client client) {
        purchasesByClient.clear();
        productsByClient.clear();
        tblProductsByClient.getItems().clear();
        allClients.stream()
                .filter(c -> c.getId() == client.getId())
                .forEach(c -> c.getPurchases().forEach(p -> {
                    if(!p.getBooleanCanceled()) {
                        purchasesByClient.add(p);
                        p.getPurchaseDetails()
                                .forEach(d -> {
                                    if(!productsByClient.contains(d.getProduct()))
                                    productsByClient.add(d.getProduct());
                                });
                    }
                }));
        initProductsByClientTable(productsByClient, dtpFrom.getValue(), dtpTo.getValue());
    }

    public void openFormPickProduct(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/products/pick-product.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Seleccionar Producto");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(productReportForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void openFormPickClient(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/clients/pick-client-product-report.fxml"), 1130, 600);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar Cliente");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(productReportForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void applyFilter(ActionEvent actionEvent) {
        initQuantityTable(allProducts, dtpFrom.getValue(), dtpTo.getValue());
        //initPercentageTable(allProducts, dtpFrom.getValue(), dtpTo.getValue());
        initPercentageChart(allProducts, dtpFrom.getValue(), dtpTo.getValue());
        initProductsByClientTable(productsByClient, dtpFrom.getValue(), dtpTo.getValue());
        filterByProduct();
    }

    private void filterByProduct() {
        if(this.product != null) {
            allProducts.remove(product);
            tblQuantity.getItems().removeAll(allProducts);
            tblPercentage.getItems().removeAll(allProducts);
            allProducts.add(product);
        }
    }

    public void loadSelectedProduct(Product product) {
        this.product = product;
        txtProduct.setText(product.getDescription());
    }

    public void resetFilter(ActionEvent actionEvent) throws IOException {
        reloadForm();
    }

    public void setValueToNow(MouseEvent actionEvent) {
        DatePickerUtils.initPickers(LocalDate.now(), dtpFrom);
    }

    private void reloadForm() throws IOException {
        menuController.loadProductReportPane(null);
    }
}
