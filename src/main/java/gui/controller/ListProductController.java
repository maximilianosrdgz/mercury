package gui.controller;

import dao.ProductDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@NoArgsConstructor
@Controller
public class ListProductController {

    @FXML
    private TextField txtFilterId;
    @FXML
    private TextField txtFilterDescription;
    @FXML
    private Button btnFilter;
    @FXML
    private Button btnResetFilter;
    @FXML
    private Button btnUpdateProduct;
    @FXML
    private TableView tblProducts;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colDescription;
    @FXML
    private TableColumn colCost;
    @FXML
    private TableColumn colPrice;
    @FXML
    private TableColumn colQuantity;
    @FXML
    private TableColumn colCategories;
    @FXML
    private TableColumn colMaterials;
    @FXML
    private Button btnFilterByCategory;
    @FXML
    private Button btnFilterByMaterial;
    @FXML
    private Button btnNewProduct;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtQuantity;
    @FXML
    private TextField txtStoreUnit;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnConfirm;

    private ProductDAO productDAO;

    @Autowired
    public ListProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public void filterWithEnter(KeyEvent keyEvent) {
    }

    public void filterProducts(ActionEvent actionEvent) {
    }

    public void reloadForm(ActionEvent actionEvent) {
    }

    public void openFormUpdateProduct(ActionEvent actionEvent) {
    }

    public void filterByCategory(ActionEvent actionEvent) {
    }

    public void filterByMaterial(ActionEvent actionEvent) {
    }

    public void activateFields(ActionEvent actionEvent) {
    }

    public void cancelLoad(ActionEvent actionEvent) {
    }

    public void saveProduct(ActionEvent actionEvent) {
    }
}
