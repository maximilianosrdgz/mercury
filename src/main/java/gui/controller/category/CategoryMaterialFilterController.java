package gui.controller.category;

import dao.CategoryDAO;
import dao.MaterialDAO;
import domain.Category;
import domain.Material;
import gui.controller.product.ListProductController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

@NoArgsConstructor
@Controller
public class CategoryMaterialFilterController implements Initializable {

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
    private TableView<Material> tblAllMaterials;
    @FXML
    private TableColumn colMaterialIdAll;
    @FXML
    private TableColumn colMaterialDescriptionAll;
    @FXML
    private Button btnSelectMaterial;
    @FXML
    private Button btnRemoveMaterial;
    @FXML
    private Button btnRemoveAllMaterials;
    @FXML
    private TableView<Material> tblSelectedMaterials;
    @FXML
    private TableColumn colMaterialIdSelected;
    @FXML
    private TableColumn colMaterialDescriptionSelected;
    @FXML
    private Button btnApply;
    @FXML
    private Button btnCancel;

    private CategoryDAO categoryDAO;
    private MaterialDAO materialDAO;
    private ListProductController listProductController;

    @Autowired
    public CategoryMaterialFilterController(CategoryDAO categoryDAO, MaterialDAO materialDAO,
                                            ListProductController listProductController) {
        this.categoryDAO = categoryDAO;
        this.materialDAO = materialDAO;
        this.listProductController = listProductController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCategoryTable(categoryDAO.findAll());
        initMaterialTable(materialDAO.findAll());
        colCategoryIdSelected.setCellValueFactory(new PropertyValueFactory<Category, String>("id"));
        colCategoryDescriptionSelected.setCellValueFactory(new PropertyValueFactory<Category, String>("description"));
        colMaterialIdSelected.setCellValueFactory(new PropertyValueFactory<Material, String>("id"));
        colMaterialDescriptionSelected.setCellValueFactory(new PropertyValueFactory<Material, String>("description"));
    }

    private void initCategoryTable(List<Category> categoryList) {
        colCategoryIdAll.setCellValueFactory(new PropertyValueFactory<Category, String>("id"));
        colCategoryDescriptionAll.setCellValueFactory(new PropertyValueFactory<Category, String>("description"));
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.addAll(categoryList);
        tblAllCategories.setItems(categories);
        tblAllCategories.getSelectionModel().selectFirst();
    }

    private void initMaterialTable(List<Material> materialList) {
        colMaterialIdAll.setCellValueFactory(new PropertyValueFactory<Material, String>("id"));
        colMaterialDescriptionAll.setCellValueFactory(new PropertyValueFactory<Material, String>("description"));
        ObservableList<Material> materials = FXCollections.observableArrayList();
        materials.addAll(materialList);
        tblAllMaterials.setItems(materials);
        tblAllMaterials.getSelectionModel().selectFirst();
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

    public void selectMaterial(ActionEvent actionEvent) {
        if(!tblAllMaterials.getItems().isEmpty()) {
            Material material = tblAllMaterials.getSelectionModel().getSelectedItem();
            tblAllMaterials.getItems().remove(material);
            tblSelectedMaterials.getItems().add(material);
            tblSelectedMaterials.getSelectionModel().selectFirst();
        }
    }

    public void removeMaterial(ActionEvent actionEvent) {
        if(!tblSelectedMaterials.getItems().isEmpty()) {
            Material material = tblSelectedMaterials.getSelectionModel().getSelectedItem();
            tblSelectedMaterials.getItems().remove(material);
            tblAllMaterials.getItems().add(material);
            tblAllMaterials.getSelectionModel().selectFirst();
        }
    }

    public void removeAllMaterials(ActionEvent actionEvent) {
        ObservableList<Material> selectedMaterials = tblSelectedMaterials.getItems();
        tblAllMaterials.getItems().addAll(selectedMaterials);
        tblSelectedMaterials.getItems().removeAll(selectedMaterials);
        tblAllMaterials.getSelectionModel().selectFirst();
    }

    public void applyFilter(ActionEvent actionEvent) {
        listProductController.filterTableByCategoryAndMaterials(
                new HashSet<>(tblSelectedCategories.getItems()), new HashSet<>(tblSelectedMaterials.getItems()));
        Stage stage = (Stage) btnApply.getScene().getWindow();
        stage.close();
    }

    public void cancelFilter(ActionEvent actionEvent) throws IOException {
        listProductController.reloadForm(actionEvent);
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void selectMaterialOnClick(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            selectMaterial(null);
        }
    }

    public void removeMaterialOnClick(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            removeMaterial(null);
        }
    }
}
