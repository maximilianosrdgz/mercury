package gui.controller.product;

import dao.CategoryDAO;
import dao.MaterialQuantityDAO;
import dao.MaterialStockDAO;
import dao.ProductDAO;
import dao.ProductStockDAO;
import domain.Category;
import domain.Client;
import domain.Material;
import domain.MaterialQuantity;
import domain.MaterialStock;
import domain.Product;
import domain.ProductStock;
import gui.util.AlertBuilder;
import gui.util.ComboBoxLoader;
import gui.util.TextFieldUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

@NoArgsConstructor
@Controller
public class UpdateProductController implements Initializable {

    @FXML
    private CheckBox chkUpdateMaterialStock;
    @FXML
    private Label lblStoreUnit;
    @FXML
    private TableView<Category> tblAllCategories;
    @FXML
    private TableColumn colCategoryIdAll;
    @FXML
    private TableColumn colCategoryDescriptionAll;
    @FXML
    private Button btnSelectCategory;
    @FXML
    private Button btnRemoveCategory;
    @FXML
    private Button btnRemoveAllCategories;
    @FXML
    private TableView<Category> tblSelectedCategories;
    @FXML
    private TableColumn colCategoryIdSelected;
    @FXML
    private TableColumn colCategoryDescriptionSelected;
    @FXML
    private ComboBox<Material> cmbMaterials;
    @FXML
    private TextField txtMaterialQuantity;
    @FXML
    private Button btnAddMaterialQuantity;
    @FXML
    private Button btnRemoveMaterialQuantity;
    @FXML
    private Button btnRemoveAllMaterials;
    @FXML
    private TableView<MaterialQuantity> tblSelectedMaterials;
    @FXML
    private TableColumn colMaterialDescriptionSelected;
    @FXML
    private TableColumn colMaterialQuantitySelected;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtDescription;
    @FXML
    private Button btnSaveMaterial;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtQuantity;
    @FXML
    private Button btnCancel;

    private ComboBoxLoader comboBoxLoader;
    private AlertBuilder alertBuilder;
    private ListProductController listProductController;
    private CategoryDAO categoryDAO;
    private MaterialQuantityDAO materialQuantityDAO;
    private MaterialStockDAO materialStockDAO;
    private ProductDAO productDAO;
    private ProductStockDAO productStockDAO;
    private ProductStock selectedProductStock;

    @Autowired
    public UpdateProductController(ComboBoxLoader comboBoxLoader, AlertBuilder alertBuilder,
                                   ListProductController listProductController,
                                   CategoryDAO categoryDAO, MaterialQuantityDAO materialQuantityDAO,
                                   MaterialStockDAO materialStockDAO, ProductDAO productDAO,
                                   ProductStockDAO productStockDAO) {

        this.comboBoxLoader = comboBoxLoader;
        this.alertBuilder = alertBuilder;
        this.listProductController = listProductController;
        this.categoryDAO = categoryDAO;
        this.materialQuantityDAO = materialQuantityDAO;
        this.materialStockDAO = materialStockDAO;
        this.productDAO = productDAO;
        this.productStockDAO = productStockDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TextFieldUtils.setDecimalOnly(txtPrice, txtMaterialQuantity, txtQuantity);
        TextFieldUtils.activated(false, txtId);
        chkUpdateMaterialStock.setSelected(true);
        txtMaterialQuantity.setText("0");
        initCategoryTable(categoryDAO.findAll());
        comboBoxLoader.initMaterialsCombo(cmbMaterials, -1);
        colCategoryDescriptionSelected.setCellValueFactory(new PropertyValueFactory<Client, String>("description"));
        colCategoryIdSelected.setCellValueFactory(new PropertyValueFactory<Client, String>("id"));
        colMaterialDescriptionSelected.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MaterialQuantity,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MaterialQuantity, String> data) {
                return new ReadOnlyStringWrapper(String.valueOf(data.getValue().getMaterial().getDescription()));
            }
        });
        colMaterialQuantitySelected.setCellValueFactory(new PropertyValueFactory<MaterialQuantity, String>("quantity"));
        loadSelectedProduct(listProductController.getSelectedProduct());
    }

    private void loadSelectedProduct(ProductStock productStock) {
        selectedProductStock = productStock;
        tblSelectedMaterials.getItems().addAll(materialQuantityDAO.findByProductId(productStock.getProduct().getId()));
        tblSelectedMaterials.getSelectionModel().selectFirst();
        tblSelectedCategories.getItems().addAll(productStock.getProduct().getCategories());
        tblSelectedCategories.getSelectionModel().selectFirst();
        tblAllCategories.getItems().removeAll(productStock.getProduct().getCategories());
        txtId.setText(String.valueOf(productStock.getProduct().getId()));
        txtDescription.setText(productStock.getProduct().getDescription());
        txtPrice.setText(String.valueOf(productStock.getProduct().getPrice()));
        txtQuantity.setText(String.valueOf(productStock.getQuantity()));
    }

    private void initCategoryTable(List<Category> categoryList) {
        colCategoryDescriptionAll.setCellValueFactory(new PropertyValueFactory<Category, String>("description"));
        colCategoryIdAll.setCellValueFactory(new PropertyValueFactory<Category, String>("id"));
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.addAll(categoryList);
        tblAllCategories.setItems(categories);
        tblAllCategories.getSelectionModel().selectFirst();
    }

    public void selectCategoryOnClick(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            selectCategory(null);
        }
    }

    public void selectCategory(ActionEvent actionEvent) {
        if(!tblAllCategories.getItems().isEmpty()) {
            Category category = tblAllCategories.getSelectionModel().getSelectedItem();
            tblAllCategories.getItems().remove(category);
            tblSelectedCategories.getItems().add(category);
            tblSelectedCategories.getSelectionModel().selectFirst();
        }
    }

    public void removeCategory(ActionEvent actionEvent) {
        if(!tblSelectedCategories.getItems().isEmpty()) {
            Category category = tblSelectedCategories.getSelectionModel().getSelectedItem();
            tblSelectedCategories.getItems().remove(category);
            tblAllCategories.getItems().add(category);
            tblAllCategories.getSelectionModel().selectFirst();
        }
    }

    public void removeAllCategories(ActionEvent actionEvent) {
        ObservableList<Category> selectedCategories = tblSelectedCategories.getItems();
        tblAllCategories.getItems().addAll(selectedCategories);
        tblSelectedCategories.getItems().removeAll(selectedCategories);
        tblAllCategories.getSelectionModel().selectFirst();
    }

    public void removeCategoryOnClick(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            removeCategory(null);
        }
    }

    public void addMaterialQuantity(ActionEvent actionEvent) {
        TextFieldUtils.setZeroIfPoint(txtMaterialQuantity);
        if(Double.parseDouble(txtMaterialQuantity.getText()) > 0) {
            ObservableList<Material> materialsSelected = FXCollections.observableArrayList();
            tblSelectedMaterials.getItems().forEach(m -> materialsSelected.add(m.getMaterial()));
            if(!materialsSelected.contains(cmbMaterials.getSelectionModel().getSelectedItem()) &&
                    cmbMaterials.getSelectionModel().getSelectedIndex() != -1) {
                MaterialQuantity quantity = MaterialQuantity.builder()
                        .material(cmbMaterials.getSelectionModel().getSelectedItem())
                        .quantity(Double.parseDouble(txtMaterialQuantity.getText()))
                        .build();
                tblSelectedMaterials.getItems().add(quantity);
            }
            else {
                if(cmbMaterials.getSelectionModel().getSelectedIndex() == -1) {
                    alertBuilder.builder()
                            .type(Alert.AlertType.INFORMATION)
                            .title("Modificar Producto")
                            .headerText("Material no agregado")
                            .contentText("Por favor, seleccione un material para agregar.")
                            .build()
                            .showAndWait();
                }
                else {
                    alertBuilder.builder()
                            .type(Alert.AlertType.INFORMATION)
                            .title("Modificar Producto")
                            .headerText("Material no agregado")
                            .contentText("El material ya se encuentra en la lista.")
                            .build()
                            .showAndWait();
                }
            }
        }
        else {
            alertBuilder.builder()
                    .type(Alert.AlertType.INFORMATION)
                    .title("Modificar Producto")
                    .headerText("Material no agregado")
                    .contentText("Por favor, elija una cantidad mayor a 0.")
                    .build()
                    .showAndWait();
        }
    }

    public void removeMaterialQuantity(ActionEvent actionEvent) {
        MaterialQuantity materialQuantity = tblSelectedMaterials.getSelectionModel().getSelectedItem();
        tblSelectedMaterials.getItems().remove(materialQuantity);
    }

    public void removeAllMaterials(ActionEvent actionEvent) {
        ObservableList<MaterialQuantity> selectedMaterials = tblSelectedMaterials.getItems();
        tblSelectedMaterials.getItems().removeAll(selectedMaterials);
    }

    public void saveProduct(ActionEvent actionEvent) throws IOException {
        TextFieldUtils.setZeroIfPoint(txtQuantity, txtPrice);
        if(TextFieldUtils.fieldsFilled(txtDescription, txtPrice, txtQuantity)) {
            ProductStock productStock = buildProductStock();
            List<MaterialQuantity> materialQuantities = buildMaterialQuantityList();
            materialQuantities.forEach(quant -> quant.setProduct(productStock.getProduct()));
            double oldQuantity = selectedProductStock.getQuantity();
            double quantityAdded = productStock.getQuantity() - selectedProductStock.getQuantity();
            if(enoughMaterialsInStock(productStock, quantityAdded) || !chkUpdateMaterialStock.isSelected()) {
                Alert alert = alertBuilder.builder()
                        .type(Alert.AlertType.CONFIRMATION)
                        .title("Modificar Producto")
                        .headerText("Está por modificar el producto: \n" + productStock.getProduct().getDescription())
                        .contentText("¿Confirmar modificación?")
                        .build();
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    productDAO.update(productStock.getProduct());
                    productStockDAO.update(productStock);
                    for(MaterialQuantity mq: materialQuantities) {
                        MaterialQuantity existent =
                                materialQuantityDAO.findByProductAndMaterialId(mq.getProduct().getId(), mq.getMaterial().getId());
                        if(existent != null) {
                            mq.setId(existent.getId());
                            materialQuantityDAO.update(mq);
                        }
                        else {
                            materialQuantityDAO.create(mq);
                        }
                    }
                    for(MaterialQuantity mq : materialQuantityDAO.findByProductId(productStock.getProduct().getId())) {
                        if(!materialQuantities.contains(mq)) {
                            materialQuantityDAO.delete(mq);
                        }
                    }
                    if(chkUpdateMaterialStock.isSelected()) {
                        if(productStock.getQuantity() > oldQuantity) {
                            List<MaterialQuantity> materialQuantityList = materialQuantityDAO
                                    .findByProductId(productStock.getProduct().getId());
                            for(MaterialQuantity quantity : materialQuantityList) {
                                MaterialStock stock = materialStockDAO.findByMaterialId(quantity.getMaterial().getId());
                                stock.setQuantity(stock.getQuantity() - (quantity.getQuantity() * quantityAdded));
                                materialStockDAO.update(stock);
                            }
                        }
                    }
                    listProductController.reloadForm(actionEvent);
                    Stage stage = (Stage) btnSaveMaterial.getScene().getWindow();
                    stage.close();
                }
            }
            else {
                alertBuilder.builder()
                        .type(Alert.AlertType.INFORMATION)
                        .title("Nuevo Producto")
                        .headerText("Materiales insuficientes")
                        .contentText("Si esto no es correcto, actualice el stock de materiales antes de cargar el producto.")
                        .build()
                        .showAndWait();
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

    private boolean enoughMaterialsInStock(ProductStock productStock, double quantityAdded) {
        boolean result = true;
        if(productStock.getQuantity() > selectedProductStock.getQuantity()) {
            List<MaterialQuantity> materialQuantityList = tblSelectedMaterials.getItems();
            for(MaterialQuantity quantity : materialQuantityList) {
                MaterialStock stock = materialStockDAO.findByMaterialId(quantity.getMaterial().getId());
                if(stock.getQuantity() < quantity.getQuantity() * quantityAdded) {
                    result = false;
                }
            }
        }

        return result;
    }

    private ProductStock buildProductStock() {
        Set<Material> materials = new HashSet<>();
        tblSelectedMaterials.getItems().forEach(item -> materials.add(item.getMaterial()));
        Set<Category> categories = new HashSet<>(tblSelectedCategories.getItems());
        Product product = Product.builder()
                .id(Integer.parseInt(txtId.getText()))
                .description(txtDescription.getText())
                .price(Double.parseDouble(txtPrice.getText()))
                .materials(materials)
                .categories(categories)
                .build();
        return ProductStock.builder()
                .id(Integer.parseInt(txtId.getText()))
                .quantity(Double.parseDouble(txtQuantity.getText()))
                .storeType("Unidades")
                .product(product)
                .build();
    }

    private List<MaterialQuantity> buildMaterialQuantityList() {
        return new ArrayList<>(tblSelectedMaterials.getItems());
    }

    public void cancelLoad(ActionEvent actionEvent) throws IOException {
        Alert alert = alertBuilder.builder()
                .type(Alert.AlertType.CONFIRMATION)
                .title("Cancelar Modificación")
                .headerText("Cancelando modificación de material")
                .contentText("¿Desea cancelar modificación?")
                .build();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            listProductController.reloadForm(actionEvent);
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }

    public void setStoreUnit(ActionEvent actionEvent) {
        lblStoreUnit.setText(materialStockDAO
                .findByMaterialId(cmbMaterials.getSelectionModel().getSelectedItem().getId()).getStoreType());
    }

    public void removeMaterialOnClick(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            removeMaterialQuantity(null);
        }
    }
}
