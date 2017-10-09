package gui.controller;

import dao.ClientDAO;
import dao.ProductStockDAO;
import domain.Client;
import domain.Product;
import domain.ProductStock;
import domain.Purchase;
import domain.PurchaseDetail;
import gui.form.SpringFxmlLoader;
import gui.util.AlertBuilder;
import gui.util.ComboBoxLoader;
import gui.util.TextFieldUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@NoArgsConstructor
@Controller
public class NewPurchaseController implements Initializable {

    @FXML
    private Label lblStock;
    @FXML
    private VBox newPurchaseForm;
    @FXML
    private TextField txtClient;
    @FXML
    private Button btnOpenFormListClient;
    @FXML
    private DatePicker dtpDate;
    @FXML
    private ComboBox<Product> cmbProducts;
    @FXML
    private TextField txtProductQuantity;
    @FXML
    private Button btnAddPurchaseDetail;
    @FXML
    private Button btnRemovePurchaseDetail;
    @FXML
    private Button btnRemoveAllDetails;
    @FXML
    private TextField txtUnitPrice;
    @FXML
    private TableView<PurchaseDetail> tblSelectedProducts;
    @FXML
    private TableColumn colQuantity;
    @FXML
    private TableColumn colDescription;
    @FXML
    private TableColumn colUnitPrice;
    @FXML
    private TableColumn colTotalPrice;
    @FXML
    private Button btnSavePurchase;
    @FXML
    private Button btnCancel;
    @FXML
    private Label lblTotal;

    private AlertBuilder alertBuilder;
    private Client client;
    private ComboBoxLoader comboBoxLoader;
    private ClientDAO clientDAO;
    private MenuController menuController;
    private ProductStockDAO productStockDAO;

    @Autowired
    public NewPurchaseController(AlertBuilder alertBuilder, ComboBoxLoader comboBoxLoader,
                                 ClientDAO clientDAO, MenuController menuController,
                                 ProductStockDAO productStockDAO) {

        this.alertBuilder = alertBuilder;
        this.comboBoxLoader = comboBoxLoader;
        this.clientDAO = clientDAO;
        this.menuController = menuController;
        this.productStockDAO = productStockDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dtpDate.setValue(LocalDate.now());
        dtpDate.setEditable(false);
        txtClient.setEditable(false);
        comboBoxLoader.initProductsCombo(cmbProducts, -1);
        initProductTable();
        TextFieldUtils.setDecimalOnly(txtProductQuantity, txtUnitPrice);
    }

    private void initProductTable() {
        colQuantity.setCellValueFactory(new PropertyValueFactory<PurchaseDetail, String>("quantity"));
        colDescription.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PurchaseDetail,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<PurchaseDetail, String> data) {
                return new ReadOnlyStringWrapper(String.valueOf(data.getValue().getProduct().getDescription()));
            }
        });
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<PurchaseDetail, String>("unitPrice"));
        colTotalPrice.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PurchaseDetail,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<PurchaseDetail, String> data) {
                double total = Double.valueOf(data.getValue().getQuantity()) * Double.valueOf(data.getValue().getUnitPrice());
                return new ReadOnlyStringWrapper(String.valueOf(total));
            }
        });
    }

    public void openFormListClient(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/clients/pick-client-purchase.fxml"), 1130, 600);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar Cliente");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(newPurchaseForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void loadSelectedClient(Client client) {
        this.client = client;
        txtClient.setText(client.getName());
    }

    public void loadProductAttributes(ActionEvent actionEvent) {
        setUnitPrice();
        setStock();
        txtProductQuantity.setText("1");
    }

    private void setUnitPrice() {
        txtUnitPrice.setText(String.valueOf(cmbProducts.getSelectionModel().getSelectedItem().getPrice()));
        txtProductQuantity.requestFocus();
    }

    private void setStock() {
        lblStock.setText(String.valueOf(productStockDAO.findByProductId(
                cmbProducts.getSelectionModel().getSelectedItem().getId()).getQuantity()));
    }

    public void addPurchaseDetail(ActionEvent actionEvent) {
        TextFieldUtils.setZeroIfPoint(txtProductQuantity, txtUnitPrice);
        if(!getSelectedProducts().contains(cmbProducts.getSelectionModel().getSelectedItem())) {
            if(TextFieldUtils.fieldsFilled(txtProductQuantity, txtUnitPrice) &&
                    cmbProducts.getSelectionModel().getSelectedIndex() > -1) {
                if(Double.parseDouble(lblStock.getText()) - Double.parseDouble(txtProductQuantity.getText()) >= 0) {
                    if(Double.parseDouble(txtProductQuantity.getText()) > 0 &&
                            Double.parseDouble(txtUnitPrice.getText()) > 0) {
                        PurchaseDetail detail = PurchaseDetail.builder()
                                .product(cmbProducts.getSelectionModel().getSelectedItem())
                                .quantity(Double.parseDouble(txtProductQuantity.getText()))
                                .unitPrice(Double.parseDouble(txtUnitPrice.getText()))
                                .build();
                        tblSelectedProducts.getItems().add(detail);
                        tblSelectedProducts.getSelectionModel().select(detail);
                        setTotalPrice();
                    }
                    else {
                        alertBuilder.builder()
                                .type(Alert.AlertType.INFORMATION)
                                .title("Nueva Venta")
                                .headerText("Cantidad/Precio incorrecto/s")
                                .contentText("La cantidad/precio ingresado/s no es correcta. " +
                                        "Por favor, ingrese un valor numérico mayor a 0")
                                .build()
                                .showAndWait();
                        txtProductQuantity.requestFocus();
                    }
                }
                else {
                    alertBuilder.builder()
                            .type(Alert.AlertType.INFORMATION)
                            .title("Nueva Venta")
                            .headerText("Stock insuficiente")
                            .contentText("La cantidad vendida seleccionada supera las existencias del producto. " +
                                    "Si esto no es correcto, actualice el stock del producto antes de registrar la venta.")
                            .build()
                            .showAndWait();
                    txtProductQuantity.requestFocus();
                }
            }
            else {
                alertBuilder.builder()
                        .type(Alert.AlertType.INFORMATION)
                        .title("Nueva Venta")
                        .headerText("Datos incompletos")
                        .contentText("Por favor, complete TODOS los datos del detalle antes de confirmar.")
                        .build()
                        .showAndWait();
            }
        }
        else {
            alertBuilder.builder()
                    .type(Alert.AlertType.INFORMATION)
                    .title("Nueva Venta")
                    .headerText("Producto ya seleccionado")
                    .contentText("El producto seleccionado ya se encuentra en la lista de detalles")
                    .build()
                    .showAndWait();
        }
    }

    private List<Product> getSelectedProducts() {
        return tblSelectedProducts.getItems().stream()
                .map(e -> e.getProduct())
                .collect(Collectors.toList());
    }

    private void setTotalPrice() {
        double total = 0;
        total = tblSelectedProducts.getItems().stream()
                .mapToDouble(e -> e.getUnitPrice() * e.getQuantity())
                .sum();
        lblTotal.setText(String.valueOf(total));
    }

    public void removePurchaseDetail(ActionEvent actionEvent) {
        tblSelectedProducts.getItems().remove(tblSelectedProducts.getSelectionModel().getSelectedItem());
        setTotalPrice();
    }

    public void removeAllDetails(ActionEvent actionEvent) {
        tblSelectedProducts.getItems().removeAll(tblSelectedProducts.getItems());
        setTotalPrice();
    }

    public void removeMaterialOnClick(MouseEvent mouseEvent) {
    }

    public void savePurchase(ActionEvent actionEvent) throws IOException {
        TextFieldUtils.setZeroIfPoint(txtProductQuantity, txtUnitPrice);
        if(TextFieldUtils.fieldsFilled(txtClient) && !tblSelectedProducts.getItems().isEmpty()) {
            Alert alert = alertBuilder.builder()
                    .type(Alert.AlertType.CONFIRMATION)
                    .title("Nueva Venta")
                    .headerText("Está por registrar una venta para el cliente: \n" + client.getName())
                    .contentText("¿Confirmar operación?")
                    .build();
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Purchase purchase = Purchase.builder()
                        .date(java.sql.Date.valueOf(dtpDate.getValue()))
                        .purchaseDetails(tblSelectedProducts.getItems())
                        .canceled(false)
                        .build();
                client.getPurchases().add(purchase);
                clientDAO.update(client);
                updateProductsStock(purchase.getPurchaseDetails());
                alertBuilder.builder()
                        .type(Alert.AlertType.INFORMATION)
                        .title("Nueva Venta")
                        .headerText("Venta registrada")
                        .contentText("Se ha registrado una nueva venta exitosamente.")
                        .build()
                        .showAndWait();
                reloadForm();
            }

        }
        else {
            alertBuilder.builder()
                    .type(Alert.AlertType.INFORMATION)
                    .title("Nueva Venta")
                    .headerText("Datos incompletos")
                    .contentText("Por favor, corrobore que haya seleccionado un cliente y agregado los productos al detalle.")
                    .build()
                    .showAndWait();
        }
    }

    private void updateProductsStock(List<PurchaseDetail> details) {
        for(PurchaseDetail detail : details) {
            ProductStock stock = productStockDAO.findByProductId(detail.getProduct().getId());
            stock.setQuantity(stock.getQuantity() - detail.getQuantity());
            productStockDAO.update(stock);
        }
    }

    public void cancelLoad(ActionEvent actionEvent) throws IOException {
        Alert alert = alertBuilder.builder()
                .type(Alert.AlertType.CONFIRMATION)
                .title("Nueva Venta")
                .headerText("Cancelando carga de nueva venta")
                .contentText("¿Desea cancelar la operación?")
                .build();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            reloadForm();
        }
    }

    private void reloadForm() throws IOException {
        menuController.loadNewPurchasePane(null);
    }
}
