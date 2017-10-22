package gui.controller.category;

import dao.CategoryDAO;
import domain.Category;
import gui.controller.report.CategoryReportController;
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
public class PickCategoryController implements Initializable {

    @FXML
    private TableView<Category> tblAllCategories;
    @FXML
    private TableColumn colIdAll;
    @FXML
    private TableColumn colDescriptionAll;
    @FXML
    private Button btnSelectCategory;
    @FXML
    private Button btnCancel;

    private CategoryDAO categoryDAO;
    private CategoryReportController categoryReportController;

    @Autowired
    public PickCategoryController(CategoryDAO categoryDAO, CategoryReportController categoryReportController) {
        this.categoryDAO = categoryDAO;
        this.categoryReportController = categoryReportController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAllCategoriesTable(categoryDAO.findAll());
    }

    private void initAllCategoriesTable(List<Category> productList) {
        colIdAll.setCellValueFactory(new PropertyValueFactory<Category, String>("id"));
        colDescriptionAll.setCellValueFactory(new PropertyValueFactory<Category, String>("description"));
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.addAll(productList);
        tblAllCategories.setItems(categories);
        tblAllCategories.getSelectionModel().selectFirst();
    }

    public void selectCategoryReport(ActionEvent actionEvent) {
        categoryReportController.loadSelectedCategory(tblAllCategories.getSelectionModel().getSelectedItem());
        Stage stage = (Stage) btnSelectCategory.getScene().getWindow();
        stage.close();
    }

    public void cancelLoad(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
