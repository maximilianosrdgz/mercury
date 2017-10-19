package gui.controller.product;

import dao.ProductDAO;
import domain.Product;
import gui.controller.email.SendEmailController;
import gui.util.AlertBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@NoArgsConstructor
@Controller
public class PickProductsController implements Initializable {

    @FXML
    private TableView<Product> tblAllProducts;
    @FXML
    private TableColumn colIdAll;
    @FXML
    private TableColumn colDescriptionAll;
    @FXML
    private Button btnSelectProduct;
    @FXML
    private Button btnRemoveProduct;
    @FXML
    private Button btnRemoveAllProducts;
    @FXML
    private TableView<Product> tblSelectedProducts;
    @FXML
    private TableColumn colIdSelected;
    @FXML
    private TableColumn colDescriptionSelected;
    @FXML
    private Button btnSelectProducts;
    @FXML
    private Button btnCancel;

    private SendEmailController sendEmailController;
    private ProductDAO productDAO;
    private AlertBuilder alertBuilder;

    @Autowired
    public PickProductsController(SendEmailController sendEmailController, ProductDAO productDAO,
                                  AlertBuilder alertBuilder) {

        this.sendEmailController = sendEmailController;
        this.productDAO = productDAO;
        this.alertBuilder = alertBuilder;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAllProductsTable(productDAO.findAll());
        initSelectedProductsTable(sendEmailController.getSelectedProducts());
    }

    private void initAllProductsTable(List<Product> productList) {
        colIdAll.setCellValueFactory(new PropertyValueFactory<Product, String>("id"));
        colDescriptionAll.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        ObservableList<Product> products = FXCollections.observableArrayList();
        products.addAll(productList);
        tblAllProducts.setItems(products);
        tblAllProducts.getSelectionModel().selectFirst();
    }

    private void initSelectedProductsTable(List<Product> productList) {
        colIdSelected.setCellValueFactory(new PropertyValueFactory<Product, String>("id"));
        colDescriptionSelected.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        ObservableList<Product> products = FXCollections.observableArrayList();
        products.addAll(productList);
        tblSelectedProducts.setItems(products);
        tblSelectedProducts.getSelectionModel().selectFirst();
    }

    public void selectProductOnClick(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            selectProduct(null);
        }
    }

    public void selectProduct(ActionEvent actionEvent) {
        if(!tblAllProducts.getItems().isEmpty()) {
            Product product = tblAllProducts.getSelectionModel().getSelectedItem();
            tblAllProducts.getItems().remove(product);
            tblSelectedProducts.getItems().add(product);
            tblSelectedProducts.getSelectionModel().selectFirst();
        }
    }

    public void removeProduct(ActionEvent actionEvent) {
        if(!tblSelectedProducts.getItems().isEmpty()) {
            Product product = tblSelectedProducts.getSelectionModel().getSelectedItem();
            tblSelectedProducts.getItems().remove(product);
            tblAllProducts.getItems().add(product);
            tblAllProducts.getSelectionModel().selectFirst();
        }
    }

    public void removeAllProducts(ActionEvent actionEvent) {
        ObservableList<Product> selectedProducts = tblSelectedProducts.getItems();
        tblAllProducts.getItems().addAll(selectedProducts);
        tblSelectedProducts.getItems().removeAll(selectedProducts);
        tblAllProducts.getSelectionModel().selectFirst();
    }

    public void removeProductOnClick(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            removeProduct(null);
        }
    }

    public void selectProducts(ActionEvent actionEvent) {
        sendEmailController.loadSelectedProducts(tblSelectedProducts.getItems());
        Stage stage = (Stage) btnSelectProducts.getScene().getWindow();
        stage.close();
    }

    public void cancelLoad(ActionEvent actionEvent) throws IOException {
        Alert alert = alertBuilder.builder()
                .type(Alert.AlertType.CONFIRMATION)
                .title("Cancelar Selección")
                .headerText("Cancelando selección de productos")
                .contentText("¿Desea cancelar selección?")
                .build();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }
}
