package gui.controller.report;

import dao.CategoryDAO;
import dao.ClientDAO;
import dao.PurchaseDAO;
import domain.Category;
import domain.Client;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

@NoArgsConstructor
@Controller
public class CategoryReportController implements Initializable {

    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private BarChart<String, Number> barChartQuantity;
    @FXML
    private VBox categoryReportForm;
    @FXML
    private Button btnSearchCategory;
    @FXML
    private TextField txtCategory;
    @FXML
    private Button btnApplyFilter;
    @FXML
    private Button btnResetFilter;
    @FXML
    private DatePicker dtpFrom;
    @FXML
    private DatePicker dtpTo;
    @FXML
    private TableView<Category> tblCategoriesByClient;
    @FXML
    private TableColumn colIdByClient;
    @FXML
    private TableColumn colDescriptionByClient;
    @FXML
    private TableColumn<Category, Integer> colPercentageByClient;
    @FXML
    private Button btnSearchClient;
    @FXML
    private TableView<Category> tblQuantity;
    @FXML
    private TableColumn colIdQuantity;
    @FXML
    private TableColumn colDescriptionQuantity;
    @FXML
    private TableColumn<Category, Integer> colQuantity;
    @FXML
    private TableView<Category> tblPercentage;
    @FXML
    private TableColumn colIdPercentage;
    @FXML
    private TableColumn colDescriptionPercentage;
    @FXML
    private TableColumn<Category, Integer> colPercentage;

    private Category category;
    private List<Category> allCategories;
    private List<Purchase> allPurchases;
    private List<Purchase> purchasesByClient;
    private Set<Category> categoriesByClient;
    private List<Client> allClients;

    private CategoryDAO categoryDAO;
    private PurchaseDAO purchaseDAO;
    private MenuController menuController;
    private ClientDAO clientDAO;

    @Autowired
    public CategoryReportController(CategoryDAO categoryDAO, PurchaseDAO purchaseDAO,
                                    MenuController menuController, ClientDAO clientDAO) {

        this.categoryDAO = categoryDAO;
        this.purchaseDAO = purchaseDAO;
        this.menuController = menuController;
        this.clientDAO = clientDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        category = null;
        allCategories = categoryDAO.findAll();
        allPurchases = purchaseDAO.findAll();
        allClients = clientDAO.findAll();
        purchasesByClient = new ArrayList<>();
        categoriesByClient = new HashSet<>();
        initDatePickers();
        initQuantityChart(allCategories, LocalDate.now().minusYears(100), LocalDate.now().plusYears(100));
        //initQuantityTable(allCategories, LocalDate.now().minusYears(100), LocalDate.now().plusYears(100));
        initPercentageTable(allCategories, LocalDate.now().minusYears(100), LocalDate.now().plusYears(100));
        initCategoriesByClientTable(new ArrayList<>(categoriesByClient), LocalDate.now().minusYears(100), LocalDate.now().plusYears(100));
    }

    private void initDatePickers() {
        DatePickerUtils.initPickers(LocalDate.now().minusYears(15), dtpFrom);
        DatePickerUtils.initPickers(LocalDate.now(), dtpTo);
        DatePickerUtils.editable(false, dtpFrom, dtpTo);
    }

    private void initQuantityChart(List<Category> categoryList, LocalDate dateFrom, LocalDate dateTo) {
        barChartQuantity.getData().clear();
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        categoryList.forEach(c -> dataSeries.getData().add(
                new XYChart.Data<>(c.getDescription(), calculateCategoryQuantity(c, dateFrom, dateTo))));
        barChartQuantity.getData().add(dataSeries);
        barChartQuantity.setLegendVisible(false);
    }
    /*
    private void initQuantityTable(List<Category> categoryList, LocalDate dateFrom, LocalDate dateTo) {
        tblQuantity.getItems().clear();
        colIdQuantity.setCellValueFactory(new PropertyValueFactory<Category, String>("id"));
        colDescriptionQuantity.setCellValueFactory(new PropertyValueFactory<Category, String>("description"));
        colQuantity.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Category,Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Category, Integer> data) {
                return new SimpleIntegerProperty(calculateCategoryQuantity(data.getValue(), dateFrom, dateTo)).asObject();
            }
        });
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.addAll(categoryList);
        colQuantity.setSortType(TableColumn.SortType.DESCENDING);
        tblQuantity.setItems(categories);
        tblQuantity.getSortOrder().add(colQuantity);
        tblQuantity.getSelectionModel().selectFirst();
    }*/

    private void initPercentageTable(List<Category> categoryList, LocalDate dateFrom, LocalDate dateTo) {
        tblPercentage.getItems().clear();
        colIdPercentage.setCellValueFactory(new PropertyValueFactory<Category, String>("id"));
        colDescriptionPercentage.setCellValueFactory(new PropertyValueFactory<Category, String>("description"));
        colPercentage.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Category,Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Category, Integer> data) {
                return new SimpleIntegerProperty(calculateCategoryPercentage(data.getValue(), dateFrom, dateTo)).asObject();
            }
        });
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.addAll(categoryList);
        colPercentage.setSortType(TableColumn.SortType.DESCENDING);
        tblPercentage.setItems(categories);
        tblPercentage.getSortOrder().add(colPercentage);
        tblPercentage.getSelectionModel().selectFirst();
    }

    private void initCategoriesByClientTable(List<Category> categoryList, LocalDate dateFrom, LocalDate dateTo) {
        tblCategoriesByClient.getItems().clear();
        colIdByClient.setCellValueFactory(new PropertyValueFactory<Category, String>("id"));
        colDescriptionByClient.setCellValueFactory(new PropertyValueFactory<Category, String>("description"));
        colPercentageByClient.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Category,Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Category, Integer> data) {
                return new SimpleIntegerProperty(calculatePercentageByClient(data.getValue(), dateFrom, dateTo)).asObject();
            }
        });
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.addAll(categoryList);
        colPercentageByClient.setSortType(TableColumn.SortType.DESCENDING);
        tblCategoriesByClient.setItems(categories);
        tblCategoriesByClient.getSortOrder().add(colPercentageByClient);
        tblCategoriesByClient.getSelectionModel().selectFirst();
    }

    private int calculateCategoryQuantity(Category category, LocalDate localDateFrom, LocalDate localDateTo) {
        Date dateFrom = java.sql.Date.valueOf(localDateFrom);
        Date dateTo = java.sql.Date.valueOf(localDateTo);
        Double quantity = allPurchases.stream()
                .filter(p -> !p.getBooleanCanceled())
                .filter(p -> p.getDate().compareTo(dateFrom) >= 0 && p.getDate().compareTo(dateTo) <= 0)
                .mapToDouble(p -> p.getPurchaseDetails().stream()
                        .filter(d -> d.getProduct().getCategories().contains(category))
                        .mapToDouble(PurchaseDetail::getQuantity)
                        .sum())
                .sum();
        return quantity.intValue();
    }

    private int calculateCategoryPercentage(Category category, LocalDate localDateFrom, LocalDate localDateTo) {
        Date dateFrom = java.sql.Date.valueOf(localDateFrom);
        Date dateTo = java.sql.Date.valueOf(localDateTo);
        double categoryQuantity = allPurchases.stream()
                .filter(p -> !p.getBooleanCanceled())
                .filter(p -> p.getDate().compareTo(dateFrom) >= 0 && p.getDate().compareTo(dateTo) <= 0)
                .mapToDouble(p -> p.getPurchaseDetails().stream()
                        .filter(d -> d.getProduct().getCategories().contains(category))
                        .mapToDouble(PurchaseDetail::getQuantity)
                        .sum())
                .sum();
        double totalQuantity = allPurchases.stream()
                .filter(p -> !p.getBooleanCanceled())
                .filter(p -> p.getDate().compareTo(dateFrom) >= 0 && p.getDate().compareTo(dateTo) <= 0)
                .mapToDouble(p -> p.getPurchaseDetails().stream()
                        .mapToDouble(PurchaseDetail::getQuantity)
                        .sum())
                .sum();
        return (int) Math.round(categoryQuantity * 100 / totalQuantity);
    }

    private int calculatePercentageByClient(Category category, LocalDate localDateFrom, LocalDate localDateTo) {
        Date dateFrom = java.sql.Date.valueOf(localDateFrom);
        Date dateTo = java.sql.Date.valueOf(localDateTo);
        double categoryQuantity = purchasesByClient.stream()
                .filter(p -> !p.getBooleanCanceled())
                .filter(p -> p.getDate().compareTo(dateFrom) >= 0 && p.getDate().compareTo(dateTo) <= 0)
                .mapToDouble(p -> p.getPurchaseDetails().stream()
                        .filter(d -> d.getProduct().getCategories().contains(category))
                        .mapToDouble(PurchaseDetail::getQuantity)
                        .sum())
                .sum();
        double totalQuantity = purchasesByClient.stream()
                .filter(p -> !p.getBooleanCanceled())
                .filter(p -> p.getDate().compareTo(dateFrom) >= 0 && p.getDate().compareTo(dateTo) <= 0)
                .mapToDouble(p -> p.getPurchaseDetails().stream()
                        .mapToDouble(PurchaseDetail::getQuantity)
                        .sum())
                .sum();
        return (int) Math.round(categoryQuantity * 100 / totalQuantity);
    }

    public void openFormPickCategory(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/products/pick-category.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Seleccionar Categoría");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(categoryReportForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void openFormPickClient(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/clients/pick-client-category-report.fxml"), 1130, 600);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar Cliente");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(categoryReportForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void loadSelectedCategory(Category category) {
        this.category = category;
        txtCategory.setText(category.getDescription());
    }

    private void filterByCategory() {
        if(this.category != null) {
            allCategories.remove(category);
            tblQuantity.getItems().removeAll(allCategories);
            tblPercentage.getItems().removeAll(allCategories);
            allCategories.add(category);
        }
    }

    public void applyFilter(ActionEvent actionEvent) {
        initQuantityChart(allCategories, dtpFrom.getValue(), dtpTo.getValue());
        //initQuantityTable(allCategories, dtpFrom.getValue(), dtpTo.getValue());
        initPercentageTable(allCategories, dtpFrom.getValue(), dtpTo.getValue());
        initCategoriesByClientTable(new ArrayList<>(categoriesByClient), dtpFrom.getValue(), dtpTo.getValue());
        filterByCategory();
    }

    public void resetFilter(ActionEvent actionEvent) throws IOException {
        reloadForm();
    }

    public void loadSelectedClient(Client client) {
        purchasesByClient.clear();
        categoriesByClient.clear();
        tblCategoriesByClient.getItems().clear();
        allClients.stream()
                .filter(c -> c.getId() == client.getId())
                .forEach(c -> c.getPurchases().forEach(p -> {
                    if(!p.getBooleanCanceled()) {
                        purchasesByClient.add(p);
                        p.getPurchaseDetails()
                                .forEach(d -> categoriesByClient.addAll(d.getProduct().getCategories()));
                    }
                }));
        initCategoriesByClientTable(new ArrayList<>(categoriesByClient), dtpFrom.getValue(), dtpTo.getValue());
    }

    private void reloadForm() throws IOException {
        menuController.loadCategoryReportPane(null);
    }

    public void setValueToNow(MouseEvent mouseEvent) {
        DatePickerUtils.initPickers(LocalDate.now(), dtpFrom);
    }
}
