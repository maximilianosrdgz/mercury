package gui.controller.supplier;

import dao.SupplierDAO;
import domain.Category;
import domain.Province;
import domain.Supplier;
import gui.controller.MenuController;
import gui.form.SpringFxmlLoader;
import gui.util.AlertBuilder;
import gui.util.ButtonUtils;
import gui.util.ComboBoxLoader;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Controller
public class ListSupplierController implements Initializable {

    @FXML
    private TextField txtFilterId;
    @FXML
    private TextField txtFilterName;
    @FXML
    private Button btnFilter;
    @FXML
    private Button btnResetFilter;
    @FXML
    private Button btnUpdateSupplie;
    @FXML
    private ComboBox<Province> cmbFilterProvinces;
    @FXML
    private TableView<Supplier> tblSuppliers;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colProvince;
    @FXML
    private TableColumn colCategories;
    @FXML
    private Button btnFilterByCategory;
    @FXML
    private ComboBox<Category> cmbCategories;
    @FXML
    private Button btnResetCategoryFilter;
    @FXML
    private TableView<Category> tblCategories;
    @FXML
    private TableColumn colCategoryDescription;
    @FXML
    private Button btnNewSupplier;
    @FXML
    private TextField txtName;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnConfirm;
    @FXML
    private ComboBox<Province> cmbProvinces;
    @FXML
    private VBox supplierListForm;

    private AlertBuilder alertBuilder;
    private ComboBoxLoader comboBoxLoader;
    private SupplierDAO supplierDAO;
    private MenuController menuController;

    @Autowired
    public ListSupplierController(AlertBuilder alertBuilder, ComboBoxLoader comboBoxLoader,
                                  SupplierDAO supplierDAO, MenuController menuController) {

        this.alertBuilder = alertBuilder;
        this.comboBoxLoader = comboBoxLoader;
        this.supplierDAO = supplierDAO;
        this.menuController = menuController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colCategoryDescription.setCellValueFactory(new PropertyValueFactory<Category, String>("description"));
        initSupplierTable(supplierDAO.findAll());
        comboBoxLoader.initCategoriesCombo(cmbCategories, -1);
        comboBoxLoader.initProvinceCombo(cmbProvinces, 0);
        comboBoxLoader.initProvinceCombo(cmbFilterProvinces, -1);
        TextFieldUtils.setNumericOnly(txtFilterId);
        TextFieldUtils.activated(false, txtName);
        ButtonUtils.activated(false, btnCancel, btnConfirm);
        cmbProvinces.setDisable(true);
    }

    private void initSupplierTable(List<Supplier> supplierList) {
        colId.setCellValueFactory(new PropertyValueFactory<Supplier, String>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));
        colProvince.setCellValueFactory(new PropertyValueFactory<Supplier, String>("province"));
        colCategories.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Supplier,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Supplier, String> data) {
                return new ReadOnlyStringWrapper(String.valueOf(data.getValue().getCategories()));
            }
        });
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
        suppliers.addAll(supplierList);
        tblSuppliers.setItems(suppliers);
        tblSuppliers.getSelectionModel().selectFirst();
    }

    public void filterWithEnter(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            filterSuppliers(null);
        }
    }

    public void filterSuppliers(ActionEvent actionEvent) {
        int id = 0;
        if(!txtFilterId.getText().equals("")) {
            id = Integer.parseInt(txtFilterId.getText());
        }

        List<Supplier> results = doFilterSuppliers(id, txtFilterName.getText(), cmbFilterProvinces.getSelectionModel().getSelectedItem());
        initSupplierTable(results);
    }

    private List<Supplier> doFilterSuppliers(int id, String name, Province province) {
        ObservableList<Supplier> suppliers = tblSuppliers.getItems();
        return suppliers.stream()
                .filter(supplier -> supplier.getId() == id || id == 0)
                .filter(supplier -> StringUtils.containsIgnoreCase(
                        Normalizer.normalize(supplier.getName(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
                        , Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")) || name.equals(""))
                .filter(supplier -> supplier.getProvince().equals(province) || province == null)
                .collect(Collectors.toList());
    }

    public void reloadForm(ActionEvent actionEvent) throws IOException {
        menuController.loadListSupplierPane(actionEvent);
    }

    public void openFormUpdateSupplier(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/suppliers/update-supplier.fxml"), 600, 500);
        Stage stage = new Stage();
        stage.setTitle("Modificar Proveedor");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(supplierListForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void filterByCategory(ActionEvent actionEvent) {
        List<Supplier> suppliers = tblSuppliers.getItems();
        ObservableList<Supplier> result = FXCollections.observableArrayList();
        List<Category> categories = tblCategories.getItems();

        if(tblCategories.getItems().isEmpty()) {
            for(Supplier supplier : suppliers) {
                if(supplier.getCategories().isEmpty()) {
                    result.add(supplier);
                }
            }
        }
        else {
            for(Supplier supplier : suppliers) {
                Set<Category> supplierCategories = supplier.getCategories();
                for(Category category: categories) {
                    if(supplierCategories.contains(category)) {
                        result.add(supplier);
                    }
                }
            }
        }
        tblSuppliers.setItems(result);
        tblSuppliers.getSelectionModel().selectFirst();
    }

    public void addCategoryToFilter(ActionEvent actionEvent) {
        if(!tblCategories.getItems().contains(cmbCategories.getSelectionModel().getSelectedItem())) {
            tblCategories.getItems().add(cmbCategories.getSelectionModel().getSelectedItem());
        }
    }

    public void clearCategoryTable(ActionEvent actionEvent) {
        if(!tblCategories.getItems().isEmpty()) {
            tblCategories.getItems().clear();
            cmbCategories.getSelectionModel().select(-1);
            tblCategories.getItems().remove(0);
        }
    }

    public void removeCategory(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            if(tblCategories.getItems().size() > 1) {
                tblCategories.getItems().remove(tblCategories.getSelectionModel().getSelectedItem());
            }
            else {
                clearCategoryTable(null);
            }
            cmbCategories.getSelectionModel().select(-1);
        }
    }

    public void activateFields(ActionEvent actionEvent) {
        ButtonUtils.activated(true, btnConfirm, btnCancel);
        ButtonUtils.activated(false, btnNewSupplier);
        TextFieldUtils.activated(true, txtName);
        cmbProvinces.setDisable(false);
    }

    public void saveSupplier(ActionEvent actionEvent) {
        if(TextFieldUtils.fieldsFilled(txtName) || cmbProvinces.getSelectionModel().getSelectedIndex() >= 0) {
            Supplier supplier = buildSupplier();
            Alert alert = alertBuilder.builder()
                    .type(Alert.AlertType.CONFIRMATION)
                    .title("Nuevo Proveedor")
                    .headerText("Guardando nuevo proveedor: " + supplier.getName())
                    .contentText("¿Confirmar operación?")
                    .build();
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                supplierDAO.create(supplier);
                tblSuppliers.getItems().add(supplier);
                tblSuppliers.getSelectionModel().select(supplier);
                Alert alert2 = alertBuilder.builder()
                        .type(Alert.AlertType.CONFIRMATION)
                        .title("Nuevo Proveedor")
                        .headerText("Proveedor guardado exitosamente")
                        .contentText("¿Desea agregar categorías?")
                        .build();
                Optional<ButtonType> result2 = alert2.showAndWait();
                if (result2.get() == ButtonType.OK) {
                    openFormUpdateSupplier(actionEvent);
                }
            }
        }
        else {
            alertBuilder.builder()
                    .type(Alert.AlertType.INFORMATION)
                    .title("Nuevo Material")
                    .headerText("Datos incompletos")
                    .contentText("Por favor, complete TODOS los datos del proveedor antes de confirmar.")
                    .build()
                    .showAndWait();
        }
    }

    private Supplier buildSupplier() {
        return Supplier.builder()
                .name(txtName.getText())
                .province(cmbProvinces.getSelectionModel().getSelectedItem())
                .categories(new HashSet<>())
                .build();
    }

    public void cancelLoad(ActionEvent actionEvent) throws IOException {
        Alert alert = alertBuilder.builder()
                .type(Alert.AlertType.CONFIRMATION)
                .title("Nuevo Proveedor")
                .headerText("Cancelando carga de nuevo proveedor")
                .contentText("¿Desea cancelar la operación?")
                .build();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            reloadForm(actionEvent);
        }
    }

    public Supplier getSelectedSupplier() {
        return tblSuppliers.getSelectionModel().getSelectedItem();
    }
}
