package gui.controller;

import dao.CategoryDAO;
import domain.Category;
import domain.Client;
import gui.util.AlertBuilder;
import gui.util.ButtonUtils;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@NoArgsConstructor
@Controller
public class UpdateCategoryController implements Initializable {

    @FXML
    private TextField txtFilterId;
    @FXML
    private TextField txtFilterDescription;
    @FXML
    private Button btnFilter;
    @FXML
    private Button btnResetFilter;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnNewCategory;
    @FXML
    private Button btnUpdateCategory;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtDescription;
    @FXML
    private Button btnConfirm;
    @FXML
    private TableView<Category> tblCategories;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colDescription;

    private MenuController menuController;
    private AlertBuilder alertBuilder;
    private CategoryDAO categoryDAO;
    private boolean isNew = false;
    
    @Autowired
    public UpdateCategoryController(MenuController menuController, AlertBuilder alertBuilder, CategoryDAO categoryDAO) {
        this.menuController = menuController;
        this.alertBuilder = alertBuilder;
        this.categoryDAO = categoryDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TextFieldUtils.setNumericOnly(txtFilterId);
        TextFieldUtils.activated(false, txtId, txtDescription);
        ButtonUtils.activated(false, btnCancel, btnConfirm);
        initCategoryTable(categoryDAO.findAll());
    }

    private void initCategoryTable(List<Category> categoryList) {
        colId.setCellValueFactory(new PropertyValueFactory<Client, String>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<Client, String>("description"));
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.addAll(categoryList);
        tblCategories.setItems(categories);
        tblCategories.getSelectionModel().selectFirst();
    }

    public void enableFields(ActionEvent actionEvent) {
        TextFieldUtils.activated(true, txtDescription);
        ButtonUtils.activated(false, btnUpdateCategory, btnNewCategory);
        ButtonUtils.activated(true, btnConfirm, btnCancel);
        isNew = true;
        txtDescription.requestFocus();
    }

    public void loadCategoryFields(ActionEvent actionEvent) {
        isNew = false;
        Category category = tblCategories.getSelectionModel().getSelectedItem();
        txtId.setText(String.valueOf(category.getId()));
        TextFieldUtils.activated(true, txtDescription);
        ButtonUtils.activated(true, btnConfirm, btnCancel);
        ButtonUtils.activated(false, btnUpdateCategory, btnNewCategory);
        txtDescription.setText(category.getDescription());
        txtDescription.requestFocus();
    }

    public void confirmUpdate(ActionEvent actionEvent) throws IOException {
        if(TextFieldUtils.fieldsFilled(txtDescription)) {
            if(isNew) {
                Alert alert = alertBuilder.builder()
                        .type(Alert.AlertType.CONFIRMATION)
                        .title("Nueva Categoría")
                        .headerText("Está por agregar una nueva categoría: \n" + txtDescription.getText())
                        .contentText("¿Confirmar operación?")
                        .build();
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    categoryDAO.create(buildCategory());
                }
            }
            else {
                Alert alert = alertBuilder.builder()
                        .type(Alert.AlertType.CONFIRMATION)
                        .title("Modificar Categoría")
                        .headerText("Está por modificar la categoría: \n" + tblCategories.getSelectionModel().getSelectedItem())
                        .contentText("¿Confirmar modificación?")
                        .build();
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    categoryDAO.update(buildCategory());
                }
            }
            reloadForm(actionEvent);
        }
        else {
            alertBuilder.builder()
                    .type(Alert.AlertType.INFORMATION)
                    .title("Actualizar Categorías")
                    .headerText("Datos incompletos")
                    .contentText("Por favor, complete TODOS los datos de la categoría antes de confirmar.")
                    .build()
                    .showAndWait();
        }
    }

    public void cancelUpdate(ActionEvent actionEvent) {
        Alert alert = alertBuilder.builder()
                .type(Alert.AlertType.CONFIRMATION)
                .title("Cancelar Modificación")
                .headerText("Cancelar modificación de categoría")
                .contentText("¿Desea cancelar la actualización?")
                .build();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            TextFieldUtils.activated(false, txtId, txtDescription);
            TextFieldUtils.clear(txtId, txtDescription);
            ButtonUtils.activated(false, btnCancel, btnConfirm);
            ButtonUtils.activated(true, btnNewCategory, btnUpdateCategory);
        }
    }

    private Category buildCategory() {
        if(isNew) {
            return Category.builder()
                    .description(txtDescription.getText())
                    .build();
        }
        else {
            return Category.builder()
                    .id(Integer.parseInt(txtId.getText()))
                    .description(txtDescription.getText())
                    .build();
        }
    }

    public void filterCategoryTable(ActionEvent actionEvent) {
        int id = 0;
        if(!txtFilterId.getText().equals("")) {
            id = Integer.parseInt(txtFilterId.getText());
        }

        List<Category> results = doFilterCategories(id, txtFilterDescription.getText());
        initCategoryTable(results);
    }

    public void reloadForm(ActionEvent actionEvent) throws IOException {
        menuController.loadNewCategoryPane(actionEvent);
    }

    private List<Category> doFilterCategories(int id, String description) {
        ObservableList<Category> categories = tblCategories.getItems();

        return categories.stream()
                .filter(category -> category.getId() == id || id == 0)
                .filter(category -> StringUtils.containsIgnoreCase(
                        Normalizer.normalize(category.getDescription(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
                        , Normalizer.normalize(description, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")) || description.equals(""))
                .collect(Collectors.toList());
    }

    public void filterWithEnter(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            filterCategoryTable(null);
        }
    }
}
