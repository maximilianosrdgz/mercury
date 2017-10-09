package gui.controller;

import dao.CategoryDAO;
import dao.MaterialDAO;
import dao.MaterialQuantityDAO;
import dao.ProductDAO;
import dao.ProductStockDAO;
import domain.Category;
import domain.Material;
import domain.MaterialQuantity;
import domain.Product;
import domain.ProductStock;
import gui.form.SpringFxmlLoader;
import gui.util.AlertBuilder;
import gui.util.ButtonUtils;
import gui.util.TextFieldUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Controller
public class ListProductController implements Initializable {

    @FXML
    private VBox productListForm;
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
    private TableView<ProductStock> tblProducts;
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

    private AlertBuilder alertBuilder;
    private ProductDAO productDAO;
    private ProductStockDAO productStockDAO;
    private MenuController menuController;
    private MaterialDAO materialDAO;
    private CategoryDAO categoryDAO;
    private MaterialQuantityDAO materialQuantityDAO;

    @Autowired
    public ListProductController(AlertBuilder alertBuilder, ProductDAO productDAO,
                                 ProductStockDAO productStockDAO, MenuController menuController,
                                 MaterialDAO materialDAO, CategoryDAO categoryDAO, MaterialQuantityDAO materialQuantityDAO) {

        this.alertBuilder = alertBuilder;
        this.productDAO = productDAO;
        this.productStockDAO = productStockDAO;
        this.menuController = menuController;
        this.materialDAO = materialDAO;
        this.categoryDAO = categoryDAO;
        this.materialQuantityDAO = materialQuantityDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TextFieldUtils.activated(false, txtStoreUnit, txtDescription, txtPrice, txtQuantity);
        TextFieldUtils.setNumericOnly(txtFilterId);
        TextFieldUtils.setDecimalOnly(txtPrice);
        ButtonUtils.activated(false, btnCancel, btnConfirm);
        txtStoreUnit.setText("Unidades");
        txtQuantity.setText("0");
        initProductTable(productStockDAO.findAll());
    }

    public void initProductTable(List<ProductStock> productStockList) {
        colId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProductStock,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ProductStock, String> data) {
                return new ReadOnlyStringWrapper(String.valueOf(data.getValue().getProduct().getId()));
            }
        });
        colDescription.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProductStock,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ProductStock, String> data) {
                return new ReadOnlyStringWrapper(String.valueOf(data.getValue().getProduct().getDescription()));
            }
        });
        colCost.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProductStock,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ProductStock, String> data) {
                double cost = 0;
                if(data.getValue().getProduct().getMaterials() != null) {
                    for(Material material : data.getValue().getProduct().getMaterials()) {
                        ObservableList<MaterialQuantity> quantities = FXCollections.observableArrayList();
                        quantities.addAll(materialQuantityDAO.findByProductId(data.getValue().getProduct().getId()));
                        cost += quantities.stream()
                                .filter(q -> q.getMaterial().getId() == material.getId())
                                .collect(Collectors.toList())
                                .get(0)
                                .getQuantity() * material.getCost();
                    }
                }
                return new ReadOnlyStringWrapper(String.valueOf(cost));
            }
        });
        colPrice.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProductStock,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ProductStock, String> data) {
                return new ReadOnlyStringWrapper(String.valueOf(data.getValue().getProduct().getPrice()));
            }
        });
        colQuantity.setCellValueFactory(new PropertyValueFactory<ProductStock, String>("quantity"));
        colCategories.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProductStock,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ProductStock, String> data) {
                return new ReadOnlyStringWrapper(String.valueOf(data.getValue().getProduct().getCategories()));
            }
        });
        colMaterials.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProductStock,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ProductStock, String> data) {
                return new ReadOnlyStringWrapper(String.valueOf(data.getValue().getProduct().getMaterials()));
            }
        });
        ObservableList<ProductStock> products = FXCollections.observableArrayList();
        products.addAll(productStockList);
        tblProducts.setItems(products);
        tblProducts.getSelectionModel().selectFirst();
    }

    public void filterWithEnter(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            filterProducts(null);
        }
    }

    public void filterProducts(ActionEvent actionEvent) {
        int id = 0;
        if(!txtFilterId.getText().equals("")) {
            id = Integer.parseInt(txtFilterId.getText());
        }

        List<ProductStock> results = doFilterProducts(id, txtFilterDescription.getText());
        initProductTable(results);
    }

    private List<ProductStock> doFilterProducts(int id, String description) {
        ObservableList<ProductStock> productStocks = tblProducts.getItems();
        return productStocks.stream()
                .filter(productStock -> productStock.getProduct().getId() == id || id == 0)
                .filter(productStock -> StringUtils.containsIgnoreCase(
                        Normalizer.normalize(productStock.getProduct().getDescription(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
                        , Normalizer.normalize(description, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")) || description.equals(""))
                .collect(Collectors.toList());
    }

    public void reloadForm(ActionEvent actionEvent) throws IOException {
        menuController.loadNewProductPane(actionEvent);
    }

    public void openFormUpdateProduct(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/products/update-product.fxml"), 600, 600);
        Stage stage = new Stage();
        stage.setTitle("Modificar Producto");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(productListForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void filterByCategoryAndMaterials(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/products/category-material-filter.fxml"), 600, 600);
        Stage stage = new Stage();
        stage.setTitle("Filtro por Categorías y Materiales");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(productListForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void activateFields(ActionEvent actionEvent) {
        TextFieldUtils.activated(true, txtPrice, txtDescription);
        ButtonUtils.activated(true, btnConfirm, btnCancel);
        ButtonUtils.activated(false, btnNewProduct, btnUpdateProduct);
    }

    public void cancelLoad(ActionEvent actionEvent) throws IOException {
        Alert alert = alertBuilder.builder()
                .type(Alert.AlertType.CONFIRMATION)
                .title("Nuevo Producto")
                .headerText("Cancelando carga de nuevo producto")
                .contentText("¿Desea cancelar la operación?")
                .build();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            reloadForm(actionEvent);
        }
    }

    public void saveProduct(ActionEvent actionEvent) throws IOException {
        TextFieldUtils.setZeroIfPoint(txtPrice);
        if(TextFieldUtils.fieldsFilled(txtDescription, txtPrice)) {
            ProductStock productStock = buildProductStock();
            Alert confirm = alertBuilder.builder()
                    .type(Alert.AlertType.CONFIRMATION)
                    .title("Nuevo Producto")
                    .headerText("Guardando nuevo producto: \n" + productStock.getProduct().getDescription())
                    .contentText("¿Confirmar operación?")
                    .build();
            Optional<ButtonType> confirmResult = confirm.showAndWait();
            if (confirmResult.get() == ButtonType.OK) {
                productDAO.create(productStock.getProduct());
                productStockDAO.create(productStock);
                tblProducts.getItems().add(productStock);
                tblProducts.getSelectionModel().select(productStock);
                Alert alert = alertBuilder.builder()
                        .type(Alert.AlertType.CONFIRMATION)
                        .title("Nuevo Producto")
                        .headerText("Guardando nuevo producto: \n" + productStock.getProduct().getDescription())
                        .contentText("¿Desea agregar categorías y/o materiales?")
                        .build();
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    openFormUpdateProduct(actionEvent);
                }
                else {
                    alertBuilder.builder()
                            .type(Alert.AlertType.INFORMATION)
                            .title("Nuevo Producto")
                            .headerText("Producto guardado exitosamente")
                            .contentText("Se ha guardado el nuevo producto: " + productStock.getProduct().getDescription())
                            .build()
                            .showAndWait();
                    reloadForm(actionEvent);
                }
            }
        }
        else {
            alertBuilder.builder()
                    .type(Alert.AlertType.INFORMATION)
                    .title("Nuevo Producto")
                    .headerText("Datos incompletos")
                    .contentText("Por favor, complete TODOS los datos del producto antes de confirmar.")
                    .build()
                    .showAndWait();
        }
    }

    public ProductStock getSelectedProduct() {
        return tblProducts.getSelectionModel().getSelectedItem();
    }

    private ProductStock buildProductStock() {
        Product product = Product.builder()
                .description(txtDescription.getText())
                .price(Double.parseDouble(txtPrice.getText()))
                .categories(new HashSet<>())
                .materials(new HashSet<>())
                .build();
        return ProductStock.builder()
                .quantity(0)
                .storeType(txtStoreUnit.getText())
                .product(product)
                .build();
    }

    public void filterTableByCategoryAndMaterials(Set<Category> categories, Set<Material> materials) {
        List<ProductStock> productStocks = new ArrayList<>(tblProducts.getItems());
        List<ProductStock> results = new ArrayList<>();
        tblProducts.getItems().removeAll(productStocks);
        if(!categories.isEmpty()) {
            for(Category c : categories) {
                results.addAll(productStocks.stream()
                        .filter(ps -> ps.getProduct().getCategories().contains(c))
                        .collect(Collectors.toList()));
            }
        }
        if(!materials.isEmpty()) {
            for(Material m : materials) {
                results.addAll(productStocks.stream()
                        .filter(ps -> ps.getProduct().getMaterials().contains(m))
                        .collect(Collectors.toList()));
            }
        }
        tblProducts.getItems().addAll(new HashSet<>(results));
        tblProducts.getSelectionModel().selectFirst();
    }
}
