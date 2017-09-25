package gui.controller;

import dao.CategoryDAO;
import dao.MaterialDAO;
import dao.MaterialQuantityDAO;
import dao.ProductDAO;
import dao.ProductStockDAO;
import domain.Category;
import domain.Material;
import domain.MaterialQuantity;
import domain.MaterialStock;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
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
        ButtonUtils.activated(false, btnCancel, btnConfirm);
        txtStoreUnit.setText("Unidades");
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
                for(Material material : data.getValue().getProduct().getMaterials()) {
                    ObservableList<MaterialQuantity> quantities = FXCollections.observableArrayList();
                    quantities.addAll(materialQuantityDAO.findByProductId(data.getValue().getProduct().getId()));
                    cost += quantities.stream()
                            .filter(q -> q.getMaterial().getId() == material.getId())
                            .collect(Collectors.toList())
                            .get(0)
                            .getQuantity() * material.getCost();
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
    }

    public void filterProducts(ActionEvent actionEvent) {
    }

    public void reloadForm(ActionEvent actionEvent) throws IOException {
        menuController.loadNewProductPane(actionEvent);
    }

    public void openFormUpdateProduct(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/update-product.fxml"), 600, 600);
        Stage stage = new Stage();
        stage.setTitle("Modificar Producto");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(productListForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void filterByCategory(ActionEvent actionEvent) {
    }

    public void filterByMaterial(ActionEvent actionEvent) {
    }

    public void activateFields(ActionEvent actionEvent) {
        TextFieldUtils.activated(true, txtQuantity, txtPrice, txtDescription);
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

    public void saveTest() {
        Material material = materialDAO.find(2);
        Product product = productDAO.find(1);
        Set<Material> materials = product.getMaterials();
        materials.add(material);
        product.setMaterials(materials);
        productDAO.update(product);
        MaterialQuantity quantity = MaterialQuantity.builder()
                .product(product)
                .material(material)
                .quantity(5)
                .build();
        materialQuantityDAO.create(quantity);
    }

    public void saveTest1() {
        Material material = materialDAO.find(1);
        Category category = categoryDAO.find(1);
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        Set<Material> materials = new HashSet<>();
        materials.add(material);

        Product product = Product.builder()
                .price(10)
                .description("Producto Prueba")
                .categories(categories)
                .materials(materials)
                .build();
        System.out.println("Product created");
        ProductStock stock = ProductStock.builder()
                .product(product)
                .quantity(100)
                .storeType("Unidades")
                .build();
        System.out.println("Stock created");
        productDAO.create(product);
        System.out.println("Product persisted");
        productStockDAO.create(stock);
        System.out.println("Stock persisted");
        MaterialQuantity materialQuantity = MaterialQuantity.builder()
                .quantity(10)
                .material(material)
                .product(productDAO.find(1))
                .build();
        System.out.println("Quantity created");
        materialQuantityDAO.create(materialQuantity);
        System.out.println("Quiantity persisted");
        product = productDAO.find(1);
        System.out.println("Product fetched");
        System.out.println(product);
        productDAO.update(product);
        System.out.println("Product updated persisted");
    }

    public void saveProduct(ActionEvent actionEvent) throws IOException {
        if(TextFieldUtils.fieldsFilled(txtDescription, txtPrice, txtQuantity)) {
            List<ProductStock> productStockList = tblProducts.getItems();
            ProductStock productStock = buildProductStock();
            Alert confirm = alertBuilder.builder()
                    .type(Alert.AlertType.CONFIRMATION)
                    .title("Nuevo Producto")
                    .headerText("Guardando nuevo producto: \n" + productStock.getProduct().getDescription())
                    .contentText("¿Confirmar operación?")
                    .build();
            Optional<ButtonType> confirmResult = confirm.showAndWait();
            if (confirmResult.get() == ButtonType.OK) {
                productStockList.add(productStock);
                productDAO.create(productStock.getProduct());
                productStockDAO.create(productStock);
                initProductTable(productStockList);
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
                .build();
        return ProductStock.builder()
                .quantity(Double.parseDouble(txtQuantity.getText()))
                .storeType(txtStoreUnit.getText())
                .product(product)
                .build();
    }
}
