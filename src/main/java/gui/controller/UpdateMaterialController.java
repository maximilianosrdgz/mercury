package gui.controller;

import dao.CategoryDAO;
import dao.MaterialDAO;
import dao.MaterialStockDAO;
import domain.Category;
import domain.Client;
import domain.Material;
import domain.MaterialStock;
import gui.util.TextFieldUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
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

    private MaterialStock selectedMaterialStock;

    @Autowired
    public UpdateMaterialController(MaterialDAO materialDAO, MaterialStockDAO materialStockDAO,
                                    CategoryDAO categoryDAO, ListMaterialController listMaterialController,
                                    MenuController menuController) {

        this.materialDAO = materialDAO;
        this.materialStockDAO = materialStockDAO;
        this.categoryDAO = categoryDAO;
        this.listMaterialController = listMaterialController;
        this.menuController = menuController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCategoryTable(categoryDAO.findAll());
        colDescriptionSelected.setCellValueFactory(new PropertyValueFactory<Client, String>("description"));
        colIdSelected.setCellValueFactory(new PropertyValueFactory<Client, String>("id"));
        loadSelectedMaterial(listMaterialController.getSelectedMaterialStock());
        TextFieldUtils.editable(false, txtId);
    }

    private void loadSelectedMaterial(MaterialStock materialStock) {
        tblSelectedCategories.getItems().addAll(materialStock.getMaterial().getCategories());
        tblAllCategories.getItems().removeAll(materialStock.getMaterial().getCategories());
        txtId.setText(String.valueOf(materialStock.getMaterial().getId()));
        txtCost.setText(String.valueOf(materialStock.getMaterial().getCost()));
        txtDescription.setText(materialStock.getMaterial().getDescription());
        txtQuantity.setText(String.valueOf(materialStock.getQuantity()));
        txtStoreUnit.setText(materialStock.getStoreType());
    }

    private void initCategoryTable(List<Category> categoryList) {
        colDescriptionAll.setCellValueFactory(new PropertyValueFactory<Client, String>("description"));
        colIdAll.setCellValueFactory(new PropertyValueFactory<Client, String>("id"));
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.addAll(categoryList);
        tblAllCategories.setItems(categories);
        tblAllCategories.getSelectionModel().selectFirst();
    }

    public void selectCategory(ActionEvent actionEvent) {
        Category category = tblAllCategories.getSelectionModel().getSelectedItem();
        tblAllCategories.getItems().remove(category);
        tblSelectedCategories.getItems().add(category);
    }

    public void removeCategory(ActionEvent actionEvent) {
        Category category = tblSelectedCategories.getSelectionModel().getSelectedItem();
        tblSelectedCategories.getItems().remove(category);
        tblAllCategories.getItems().add(category);
    }

    public void removeAllCategories(ActionEvent actionEvent) {
        ObservableList<Category> selectedCategories = tblSelectedCategories.getItems();
        tblAllCategories.getItems().addAll(selectedCategories);
        tblSelectedCategories.getItems().removeAll(selectedCategories);
    }

    public void saveMaterial(ActionEvent actionEvent) throws IOException {
        MaterialStock materialStock = buildMaterialStock();
        materialDAO.update(materialStock.getMaterial());
        materialStockDAO.update(materialStock);
        menuController.loadNewMaterialPane(actionEvent);

        Stage stage = (Stage) btnSaveMaterial.getScene().getWindow();
        stage.close();
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
}
