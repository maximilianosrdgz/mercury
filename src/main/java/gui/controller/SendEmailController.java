package gui.controller;

import dao.ClientDAO;
import domain.Category;
import domain.Client;
import domain.Product;
import domain.Province;
import domain.Purchase;
import gui.form.SpringFxmlLoader;
import gui.util.AlertBuilder;
import gui.util.ComboBoxLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Controller
public class SendEmailController implements Initializable {

    @FXML
    private Label lblProductCount;
    @FXML
    private Label lblCategoryCount;
    @FXML
    private Label lblProvinceCount;
    @FXML
    private VBox sendEmailForm;
    @FXML
    private Button btnSearchFile;
    @FXML
    private TextField txtFilePath;
    @FXML
    private ComboBox<String> cmbBuyer;
    @FXML
    private ComboBox<String> cmbConsultant;
    @FXML
    private Button btnOpenFormPickProducts;
    @FXML
    private Button btnOpenFormPickCategories;
    @FXML
    private Button btnOpenFormPickProvinces;
    @FXML
    private Button btnResetFilter;
    @FXML
    private Button btnApplyFilter;
    @FXML
    private TableView<Client> tblClients;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colEmail;
    @FXML
    private Button btnRemoveClient;
    @FXML
    private Button btnAddClient;
    @FXML
    private Button btnSendEmail;

    private ComboBoxLoader comboBoxLoader;
    private ClientDAO clientDAO;
    private MenuController menuController;
    private AlertBuilder alertBuilder;
    private List<Product> selectedProducts;
    private List<Category> selectedCategories;
    private List<Province> selectedProvinces;

    @Autowired
    public SendEmailController(ComboBoxLoader comboBoxLoader, ClientDAO clientDAO,
                               MenuController menuController, AlertBuilder alertBuilder) {

        this.comboBoxLoader = comboBoxLoader;
        this.clientDAO = clientDAO;
        this.menuController = menuController;
        this.alertBuilder = alertBuilder;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBoxLoader.initBooleanStringCombo(cmbBuyer, 0);
        comboBoxLoader.initBooleanStringCombo(cmbConsultant, 0);
        initClientsTable(clientDAO.findAll());
        selectedProducts = new ArrayList<>();
        selectedCategories = new ArrayList<>();
        selectedProvinces = new ArrayList<>();
    }

    private void initClientsTable(List<Client> clientList) {
        colName.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Client, String>("email"));
        ObservableList<Client> clients = FXCollections.observableArrayList();
        clients.addAll(clientList);
        tblClients.setItems(clients);
        tblClients.getSelectionModel().selectFirst();
    }

    public void openPickProductsForm(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/products/pick-products.fxml"), 600, 500);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar Productos");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(sendEmailForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void openPickCategoriesForm(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/products/pick-categories.fxml"), 600, 500);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar Categorías");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(sendEmailForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void openPickProvincesForm(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/clients/pick-provinces.fxml"), 600, 500);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar Provincias");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(sendEmailForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void resetFilter(ActionEvent actionEvent) throws IOException {
        Alert alert = alertBuilder.builder()
                .type(Alert.AlertType.CONFIRMATION)
                .title("Enviar Mail")
                .headerText("Reestablecer selección de clientes")
                .contentText("¿Desea cancelar la operación?")
                .build();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            reloadForm();
        }
    }

    public void applyFilter(ActionEvent actionEvent) {
        boolean buyer = false;
        boolean consultant = false;
        boolean isBuyerSelected = true;
        boolean isConsultantSelected = true;

        if(cmbBuyer.getSelectionModel().getSelectedIndex() == 1) {
            buyer = true;
        }
        else {
            if(cmbBuyer.getSelectionModel().getSelectedIndex() == 2) {
                buyer = false;
            }
            else {
                isBuyerSelected = false;
            }
        }

        if(cmbConsultant.getSelectionModel().getSelectedIndex() == 1) {
            consultant = true;
        }
        else {
            if(cmbConsultant.getSelectionModel().getSelectedIndex() == 2) {
                consultant = false;
            }
            else {
                isConsultantSelected = false;
            }
        }

        List<Client> results = doFilterClients(isBuyerSelected, isConsultantSelected, buyer, consultant,
                selectedProducts, selectedCategories, selectedProvinces);
        initClientsTable(results);
    }

    private List<Client> doFilterClients(boolean isBuyerSelected, boolean isConsultantSelected,
                                 boolean isBuyer, boolean isConsultant,
                                 List<Product> products, List<Category> categories, List<Province> provinces) {

        ObservableList<Client> clients = tblClients.getItems();
        Set<Client> results = new HashSet<>();

        results.addAll(clients.stream()
                .filter(client -> client.getBooleanBuyer() == isBuyer || !isBuyerSelected)
                .collect(Collectors.toList()));
        results.addAll(clients.stream()
                .filter(client -> client.getBooleanConsultant() == isConsultant || !isConsultantSelected)
                .collect(Collectors.toList()));
        results.addAll(clients.stream()
                .filter(client -> !Collections.disjoint(getProductsPurchased(client), products) || products.isEmpty())
                .collect(Collectors.toList()));
        results.addAll(clients.stream()
                .filter(client -> !Collections.disjoint(getCategoriesPurchased(client), categories) || categories.isEmpty())
                .collect(Collectors.toList()));
        results.addAll(clients.stream()
                .filter(client -> provinces.contains(client.getProvince()) || provinces.isEmpty())
                .collect(Collectors.toList()));

        return new ArrayList<>(results);
    }

    private List<Product> getProductsPurchased(Client client) {
        List<Product> results = new ArrayList<>();
        for(Purchase p : client.getPurchases()) {
            p.getPurchaseDetails().forEach(d -> results.add(d.getProduct()));
        }
        return results;
    }

    private List<Category> getCategoriesPurchased(Client client) {
        List<Category> results = new ArrayList<>();
        for(Purchase p : client.getPurchases()) {
            p.getPurchaseDetails().forEach(d -> results.addAll(d.getProduct().getCategories()));
        }
        return results;
    }

    public void removeClient(ActionEvent actionEvent) {
        tblClients.getItems().remove(tblClients.getSelectionModel().getSelectedItem());
    }

    public void openPickClientForm(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/clients/pick-client-email.fxml"), 1130, 600);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar Cliente");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(sendEmailForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void sendEmail(ActionEvent actionEvent) {
    }

    public void loadSelectedClient(Client client) {
        if(!tblClients.getItems().contains(client)) {
            tblClients.getItems().add(client);
            tblClients.getSelectionModel().select(client);
        }
    }

    public void loadSelectedProducts(List<Product> productList) {
        selectedProducts.clear();
        selectedProducts.addAll(productList);
        lblProductCount.setText(String.format("(%d)", selectedProducts.size()));
    }

    public void loadSelectedCategories(List<Category> categoryList) {
        selectedCategories.clear();
        selectedCategories.addAll(categoryList);
        lblCategoryCount.setText(String.format("(%d)", selectedCategories.size()));
    }

    public void loadSelectedProvinces(List<Province> provinceList) {
        selectedProvinces.clear();
        selectedProvinces.addAll(provinceList);
        lblProvinceCount.setText(String.format("(%d)", selectedProvinces.size()));
    }

    public List<Product> getSelectedProducts() {
        return selectedProducts;
    }

    public List<Category> getSelectedCategories() {
        return selectedCategories;
    }

    public List<Province> getSelectedProvinces() {
        return selectedProvinces;
    }

    public void reloadForm() throws IOException {
        menuController.loadSendEmailPane(null);
    }
}
