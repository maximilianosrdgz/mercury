package gui.controller;

import dao.CategoryDAO;
import domain.Category;
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

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@NoArgsConstructor
@Controller
public class PickCategoriesController implements Initializable{

    @FXML
    private TableView<Category> tblAllCategories;
    @FXML
    private TableColumn colIdAll;
    @FXML
    private TableColumn colDescriptionAll;
    @FXML
    private Button btnSelectCategory;
    @FXML
    private Button btnRemoveCategory;
    @FXML
    private Button btnRemoveAllCategories;
    @FXML
    private TableView<Category> tblSelectedCategories;
    @FXML
    private TableColumn colIdSelected;
    @FXML
    private TableColumn colDescriptionSelected;
    @FXML
    private Button btnSelectCategories;
    @FXML
    private Button btnCancel;

    private SendEmailController sendEmailController;
    private CategoryDAO categoryDAO;
    private AlertBuilder alertBuilder;

    @Autowired
    public PickCategoriesController(SendEmailController sendEmailController, CategoryDAO categoryDAO,
                                    AlertBuilder alertBuilder) {

        this.sendEmailController = sendEmailController;
        this.categoryDAO = categoryDAO;
        this.alertBuilder = alertBuilder;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAllCategoriesTable(categoryDAO.findAll());
        initSelectedCategoriesTable(sendEmailController.getSelectedCategories());
    }

    private void initAllCategoriesTable(List<Category> productList) {
        colIdAll.setCellValueFactory(new PropertyValueFactory<Category, String>("id"));
        colDescriptionAll.setCellValueFactory(new PropertyValueFactory<Category, String>("description"));
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.addAll(productList);
        tblAllCategories.setItems(categories);
        tblAllCategories.getSelectionModel().selectFirst();
    }

    private void initSelectedCategoriesTable(List<Category> productList) {
        colIdSelected.setCellValueFactory(new PropertyValueFactory<Category, String>("id"));
        colDescriptionSelected.setCellValueFactory(new PropertyValueFactory<Category, String>("description"));
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.addAll(productList);
        tblSelectedCategories.setItems(categories);
        tblSelectedCategories.getSelectionModel().selectFirst();
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

    public void selectCategories(ActionEvent actionEvent) {
        sendEmailController.loadSelectedCategories(tblSelectedCategories.getItems());
        Stage stage = (Stage) btnSelectCategories.getScene().getWindow();
        stage.close();
    }

    public void cancelLoad(ActionEvent actionEvent) {
        Alert alert = alertBuilder.builder()
                .type(Alert.AlertType.CONFIRMATION)
                .title("Cancelar Selección")
                .headerText("Cancelando selección de categorías")
                .contentText("¿Desea cancelar selección?")
                .build();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }
}
