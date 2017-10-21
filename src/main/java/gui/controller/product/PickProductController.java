package gui.controller.product;

import dao.ProductDAO;
import domain.Product;
import gui.controller.report.ProductReportController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@NoArgsConstructor
@Controller
public class PickProductController implements Initializable {

    @FXML
    private TableView<Product> tblAllProducts;
    @FXML
    private TableColumn colIdAll;
    @FXML
    private TableColumn colDescriptionAll;
    @FXML
    private Button btnSelectProducts;
    @FXML
    private Button btnCancel;

    private ProductReportController productReportController;
    private ProductDAO productDAO;

    @Autowired
    public PickProductController(ProductReportController productReportController, ProductDAO productDAO) {
        this.productReportController = productReportController;
        this.productDAO = productDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAllProductsTable(productDAO.findAll());
    }

    private void initAllProductsTable(List<Product> productList) {
        colIdAll.setCellValueFactory(new PropertyValueFactory<Product, String>("id"));
        colDescriptionAll.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        ObservableList<Product> products = FXCollections.observableArrayList();
        products.addAll(productList);
        tblAllProducts.setItems(products);
        tblAllProducts.getSelectionModel().selectFirst();
    }

    public void selectProductReport(ActionEvent actionEvent) {
        productReportController.loadSelectedProduct(tblAllProducts.getSelectionModel().getSelectedItem());
        Stage stage = (Stage) btnSelectProducts.getScene().getWindow();
        stage.close();
    }

    public void cancelLoad(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
