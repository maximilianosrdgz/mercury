package gui.controller.supplier;

import dao.CategoryDAO;
import dao.SupplierDAO;
import domain.Category;
import domain.Province;
import domain.Supplier;
import gui.util.AlertBuilder;
import gui.util.ComboBoxLoader;
import gui.util.TextFieldUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
public class UpdateSupplierController implements Initializable {

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
    private TextField txtName;
    @FXML
    private Button btnSaveSupplier;
    @FXML
    private Button btnCancel;
    @FXML
    private ComboBox<Province> cmbProvinces;
    @FXML
    private Label lblId;

    private AlertBuilder alertBuilder;
    private ListSupplierController listSupplierController;
    private CategoryDAO categoryDAO;
    private SupplierDAO supplierDAO;
    private ComboBoxLoader comboBoxLoader;

    @Autowired
    public UpdateSupplierController(AlertBuilder alertBuilder, ListSupplierController listSupplierController,
                                    CategoryDAO categoryDAO, SupplierDAO supplierDAO,
                                    ComboBoxLoader comboBoxLoader) {

        this.alertBuilder = alertBuilder;
        this.listSupplierController = listSupplierController;
        this.categoryDAO = categoryDAO;
        this.supplierDAO = supplierDAO;
        this.comboBoxLoader = comboBoxLoader;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCategoryTable(categoryDAO.findAll());
        colDescriptionSelected.setCellValueFactory(new PropertyValueFactory<Supplier, String>("description"));
        colIdSelected.setCellValueFactory(new PropertyValueFactory<Supplier, String>("id"));
        comboBoxLoader.initProvinceCombo(cmbProvinces, 0);
        loadSelectedSupplier(listSupplierController.getSelectedSupplier());
    }

    private void initCategoryTable(List<Category> categoryList) {
        colDescriptionAll.setCellValueFactory(new PropertyValueFactory<Supplier, String>("description"));
        colIdAll.setCellValueFactory(new PropertyValueFactory<Supplier, String>("id"));
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.addAll(categoryList);
        tblAllCategories.setItems(categories);
        tblAllCategories.getSelectionModel().selectFirst();
    }

    private void loadSelectedSupplier(Supplier supplier) {
        tblSelectedCategories.getItems().addAll(supplier.getCategories());
        lblId.setText(String.valueOf(supplier.getId()));
        txtName.setText(supplier.getName());
        cmbProvinces.getSelectionModel().select(supplier.getProvince());
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

    public void saveSupplier(ActionEvent actionEvent) throws IOException {
        if(TextFieldUtils.fieldsFilled(txtName) && cmbProvinces.getSelectionModel().getSelectedIndex() >= 0) {
            Supplier supplier = buildSupplier();
            Alert alert = alertBuilder.builder()
                    .type(Alert.AlertType.CONFIRMATION)
                    .title("Modificar Proveedor")
                    .headerText("Está por modificar el proveedor: \n" + supplier)
                    .contentText("¿Confirmar modificación?")
                    .build();
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                supplierDAO.update(supplier);
                listSupplierController.reloadForm(actionEvent);
                Stage stage = (Stage) btnSaveSupplier.getScene().getWindow();
                stage.close();
            }
        }
        else {
            alertBuilder.builder()
                    .type(Alert.AlertType.INFORMATION)
                    .title("Modificar Proveedor")
                    .headerText("Datos incompletos")
                    .contentText("Por favor, complete TODOS los datos del proveedor antes de confirmar.")
                    .build()
                    .showAndWait();
        }
    }

    private Supplier buildSupplier() {
        Set<Category> selectedCategories = new HashSet<>(tblSelectedCategories.getItems());
        return Supplier.builder()
                .id(Integer.parseInt(lblId.getText()))
                .name(txtName.getText())
                .province(cmbProvinces.getSelectionModel().getSelectedItem())
                .categories(selectedCategories)
                .observations("")
                .build();
    }

    public void cancelLoad(ActionEvent actionEvent) throws IOException {
        Alert alert = alertBuilder.builder()
                .type(Alert.AlertType.CONFIRMATION)
                .title("Cancelar Modificación")
                .headerText("Cancelando modificación de proveedor")
                .contentText("¿Desea cancelar modificación?")
                .build();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            listSupplierController.reloadForm(actionEvent);
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }
}
