package gui.controller;

import dao.CategoryDAO;
import dao.MaterialDAO;
import dao.MaterialStockDAO;
import domain.Category;
import domain.Client;
import domain.Material;
import domain.MaterialStock;
import gui.util.AlertBuilder;
import gui.util.TextFieldUtils;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
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

@NoArgsConstructor
@Controller
public class UpdateMaterialController implements Initializable {

    public Button btnSelectCategory;
    public Button btnRemoveCategory;
    public Button btnRemoveAllCategories;
    public TextField txtId;
    public TextField txtDescription;
    public TextField txtCost;
    public TextField txtQuantity;
    public TextField txtStoreUnit;
    public Button btnSaveMaterial;
    public Button btnCancel;
    @FXML
    private TableView<Category> tblAllCategories;
    @FXML
    private TableColumn colIdAll;
    @FXML
    private TableColumn colDescriptionAll;
    @FXML
    private TableView<Category> tblSelectedCategories;
    @FXML
    private TableColumn colIdSelected;
    @FXML
    private TableColumn colDescriptionSelected;


    private MaterialDAO materialDAO;
    private MaterialStockDAO materialStockDAO;
    private CategoryDAO categoryDAO;
    private ListMaterialController listMaterialController;
    private MenuController menuController;
    private AlertBuilder alertBuilder;

    private MaterialStock selectedMaterialStock;

    @Autowired
    public UpdateMaterialController(MaterialDAO materialDAO, MaterialStockDAO materialStockDAO,
                                    CategoryDAO categoryDAO, ListMaterialController listMaterialController,
                                    MenuController menuController, AlertBuilder alertBuilder) {

        this.materialDAO = materialDAO;
        this.materialStockDAO = materialStockDAO;
        this.categoryDAO = categoryDAO;
        this.listMaterialController = listMaterialController;
        this.menuController = menuController;
        this.alertBuilder = alertBuilder;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TextFieldUtils.setDecimalOnly(txtCost, txtQuantity);
        initCategoryTable(categoryDAO.findAll());
        colDescriptionSelected.setCellValueFactory(new PropertyValueFactory<Client, String>("description"));
        colIdSelected.setCellValueFactory(new PropertyValueFactory<Client, String>("id"));
        loadSelectedMaterial(listMaterialController.getSelectedMaterialStock());
        TextFieldUtils.editable(false, txtId);
        TextFieldUtils.activated(false, txtId);
    }

    private void loadSelectedMaterial(MaterialStock materialStock) {
        tblSelectedCategories.getItems().addAll(materialStock.getMaterial().getCategories());
        tblSelectedCategories.getSelectionModel().selectFirst();
        tblAllCategories.getItems().removeAll(materialStock.getMaterial().getCategories());
        txtId.setText(String.valueOf(materialStock.getMaterial().getId()));
        txtCost.setText(String.valueOf(materialStock.getMaterial().getCost()));
        txtDescription.setText(materialStock.getMaterial().getDescription());
        txtQuantity.setText(String.valueOf(materialStock.getQuantity()));
        txtStoreUnit.setText(materialStock.getStoreType());
    }

    private void initCategoryTable(List<Category> categoryList) {
        colDescriptionAll.setCellValueFactory(new PropertyValueFactory<Category, String>("description"));
        colIdAll.setCellValueFactory(new PropertyValueFactory<Category, String>("id"));
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.addAll(categoryList);
        tblAllCategories.setItems(categories);
        tblAllCategories.getSelectionModel().selectFirst();
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
    }

    public void saveMaterial(ActionEvent actionEvent) throws IOException {
        if(TextFieldUtils.fieldsFilled(txtDescription, txtCost, txtQuantity, txtStoreUnit)) {
            MaterialStock materialStock = buildMaterialStock();
            Alert alert = alertBuilder.builder()
                    .type(Alert.AlertType.CONFIRMATION)
                    .title("Modificar Material")
                    .headerText("Está por modificar el material: \n" + materialStock.getMaterial().getDescription())
                    .contentText("¿Confirmar modificación?")
                    .build();
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                materialDAO.update(materialStock.getMaterial());
                materialStockDAO.update(materialStock);
                listMaterialController.reloadForm(actionEvent);

                Stage stage = (Stage) btnSaveMaterial.getScene().getWindow();
                stage.close();
            }
        }
        else {
            alertBuilder.builder()
                    .type(Alert.AlertType.INFORMATION)
                    .title("Nuevo Material")
                    .headerText("Datos incompletos")
                    .contentText("Por favor, complete TODOS los datos del material antes de confirmar.")
                    .build()
                    .showAndWait();
        }
    }

    private MaterialStock buildMaterialStock() {
        Set<Category> selectedCategories = new HashSet<>(tblSelectedCategories.getItems());
        Material material = Material.builder()
                .id(Integer.valueOf(txtId.getText()))
                .description(txtDescription.getText())
                .cost(Double.valueOf(txtCost.getText()))
                .categories(selectedCategories)
                .build();
        return MaterialStock.builder()
                .id(Integer.parseInt(txtId.getText()))
                .quantity(Double.valueOf(txtQuantity.getText()))
                .storeType(txtStoreUnit.getText())
                .material(material)
                .build();
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
            listMaterialController.reloadForm(actionEvent);
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }

    public void selectCategoryOnClick(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            selectCategory(null);
        }
    }

    public void removeCategoryOnClick(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            removeCategory(null);
        }
    }
}
