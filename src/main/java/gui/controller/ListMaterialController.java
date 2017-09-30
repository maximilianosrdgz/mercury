package gui.controller;

import dao.MaterialDAO;
import dao.MaterialStockDAO;
import domain.Category;
import domain.Material;
import domain.MaterialStock;
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
public class ListMaterialController implements Initializable {

    @FXML
    private Button btnResetCategoryFilter;
    @FXML
    private TableColumn colCategoryDescription;
    @FXML
    private TableView<Category> tblCategories;
    @FXML
    private Button btnFilterByCategory;
    @FXML
    private ComboBox<Category> cmbCategories;
    @FXML
    private TableColumn colQuantity;
    @FXML
    private TableColumn colStoreUnit;
    @FXML
    private TextField txtFilterId;
    @FXML
    private TextField txtFilterDescription;
    @FXML
    private Button btnFilter;
    @FXML
    private Button btnResetFilter;
    @FXML
    private TableView<MaterialStock> tblMaterials;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colDescription;
    @FXML
    private TableColumn colCost;
    @FXML
    private TableColumn colCategories;
    @FXML
    private Button btnNewMaterial;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtCost;
    @FXML
    private TextField txtQuantity;
    @FXML
    private TextField txtStoreUnit;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnConfirm;
    @FXML
    private VBox materialListForm;
    @FXML
    private Button btnUpdateMaterial;
    @FXML
    private Button btnSaveMaterial;

    private MaterialDAO materialDAO;
    private MaterialStockDAO materialStockDAO;
    private AlertBuilder alertBuilder;
    private MenuController menuController;
    private ComboBoxLoader comboBoxLoader;

    @Autowired
    public ListMaterialController(MaterialDAO materialDAO, MaterialStockDAO materialStockDAO,
                                  AlertBuilder alertBuilder, MenuController menuController,
                                  ComboBoxLoader comboBoxLoader) {

        this.materialDAO = materialDAO;
        this.materialStockDAO = materialStockDAO;
        this.alertBuilder = alertBuilder;
        this.menuController = menuController;
        this.comboBoxLoader = comboBoxLoader;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colCategoryDescription.setCellValueFactory(new PropertyValueFactory<Category, String>("description"));
        comboBoxLoader.initCategoriesCombo(cmbCategories, -1);
        TextFieldUtils.setDecimalOnly(txtCost, txtQuantity);
        TextFieldUtils.setNumericOnly(txtFilterId);
        initMaterialTable(materialStockDAO.findAll());
        TextFieldUtils.activated(false, txtDescription, txtCost, txtQuantity, txtStoreUnit);
        ButtonUtils.activated(false, btnConfirm, btnCancel);
    }

    private void initMaterialTable(List<MaterialStock> materialStockList) {
        colId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MaterialStock,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MaterialStock, String> data) {
                return new ReadOnlyStringWrapper(String.valueOf(data.getValue().getMaterial().getId()));
            }
        });
        colDescription.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MaterialStock,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MaterialStock, String> data) {
                return new ReadOnlyStringWrapper(String.valueOf(data.getValue().getMaterial().getDescription()));
            }
        });
        colCost.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MaterialStock,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MaterialStock, String> data) {
                return new ReadOnlyStringWrapper(String.valueOf(data.getValue().getMaterial().getCost()));
            }
        });
        colQuantity.setCellValueFactory(new PropertyValueFactory<MaterialStock, String>("quantity"));
        colStoreUnit.setCellValueFactory(new PropertyValueFactory<MaterialStock, String>("storeType"));
        colCategories.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MaterialStock,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MaterialStock, String> data) {
                return new ReadOnlyStringWrapper(String.valueOf(data.getValue().getMaterial().getCategories()));
            }
        });
        ObservableList<MaterialStock> materials = FXCollections.observableArrayList();
        materials.addAll(materialStockList);
        tblMaterials.setItems(materials);
        tblMaterials.getSelectionModel().selectFirst();
    }

    public void saveMaterial(ActionEvent actionEvent) throws IOException {
        TextFieldUtils.setZeroIfPoint(txtCost, txtQuantity);
        if(TextFieldUtils.fieldsFilled(txtDescription, txtCost, txtQuantity, txtStoreUnit)) {
            List<MaterialStock> materialStockList = tblMaterials.getItems();
            MaterialStock materialStock = buildMaterialStock();
            Alert confirm = alertBuilder.builder()
                    .type(Alert.AlertType.CONFIRMATION)
                    .title("Nuevo Material")
                    .headerText("Guardando nuevo material: \n" + materialStock.getMaterial().getDescription())
                    .contentText("¿Confirmar operación?")
                    .build();
            Optional<ButtonType> confirmResult = confirm.showAndWait();
            if (confirmResult.get() == ButtonType.OK) {
                materialStockList.add(materialStock);
                materialDAO.create(materialStock.getMaterial());
                materialStockDAO.create(materialStock);
                initMaterialTable(materialStockList);
                tblMaterials.getSelectionModel().select(materialStock);
                Alert alert = alertBuilder.builder()
                        .type(Alert.AlertType.CONFIRMATION)
                        .title("Nuevo Material")
                        .headerText("Guardando nuevo material: \n" + materialStock.getMaterial().getDescription())
                        .contentText("¿Desea agregar categorías?")
                        .build();
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    openFormUpdateMaterial(actionEvent);
                }
                else {
                    alertBuilder.builder()
                            .type(Alert.AlertType.INFORMATION)
                            .title("Nuevo Material")
                            .headerText("Material guardado exitosamente")
                            .contentText("Se ha guardado el nuevo material: " + materialStock.getMaterial().getDescription())
                            .build()
                            .showAndWait();
                    reloadForm(actionEvent);
                }
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
        Material material = Material.builder()
                .description(txtDescription.getText())
                .cost(Double.valueOf(txtCost.getText()))
                .categories(new HashSet<>())
                .build();
        return MaterialStock.builder()
                .quantity(Double.valueOf(txtQuantity.getText()))
                .storeType(txtStoreUnit.getText())
                .material(material)
                .build();
    }

    public void filterWithEnter(KeyEvent keyEvent) throws IOException {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            filterMaterials(null);
        }
    }

    public void reloadForm(ActionEvent actionEvent) throws IOException {
        menuController.loadNewMaterialPane(actionEvent);
    }

    public void openFormUpdateMaterial(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/update-material.fxml"), 600, 500);
        Stage stage = new Stage();
        stage.setTitle("Modificar Material");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(materialListForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public MaterialStock getSelectedMaterialStock() {
        return tblMaterials.getSelectionModel().getSelectedItem();
    }

    public void activateFields(ActionEvent actionEvent) {
        TextFieldUtils.activated(true, txtDescription, txtCost, txtQuantity, txtStoreUnit);
        ButtonUtils.activated(true, btnConfirm, btnCancel);
        ButtonUtils.activated(false, btnNewMaterial, btnUpdateMaterial);
    }

    public void filterMaterials(ActionEvent actionEvent) {
        int id = 0;
        if(!txtFilterId.getText().equals("")) {
            id = Integer.parseInt(txtFilterId.getText());
        }

        List<MaterialStock> results = doFilterMaterials(id, txtFilterDescription.getText());
        initMaterialTable(results);
    }

    private List<MaterialStock> doFilterMaterials(int id, String description) {
        ObservableList<MaterialStock> materialStocks = tblMaterials.getItems();
        return materialStocks.stream()
                .filter(materialStock -> materialStock.getMaterial().getId() == id || id == 0)
                .filter(materialStock -> StringUtils.containsIgnoreCase(
                        Normalizer.normalize(materialStock.getMaterial().getDescription(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
                        , Normalizer.normalize(description, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")) || description.equals(""))
                .collect(Collectors.toList());
    }

    public void cancelLoad(ActionEvent actionEvent) throws IOException {
        Alert alert = alertBuilder.builder()
                .type(Alert.AlertType.CONFIRMATION)
                .title("Nuevo Material")
                .headerText("Cancelando carga de nuevo material")
                .contentText("¿Desea cancelar la operación?")
                .build();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            reloadForm(actionEvent);
        }
    }

    public void addCategoryToFilter(ActionEvent actionEvent) {
        if(!tblCategories.getItems().contains(cmbCategories.getSelectionModel().getSelectedItem())) {
            tblCategories.getItems().add(cmbCategories.getSelectionModel().getSelectedItem());
        }
    }

    public void clearCategoryTable(ActionEvent actionEvent) {
        tblCategories.getItems().clear();
        cmbCategories.getSelectionModel().select(-1);
        tblCategories.getItems().remove(0);
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

    public void filterByCategory(ActionEvent actionEvent) {
        List<MaterialStock> materialStocks = tblMaterials.getItems();
        ObservableList<MaterialStock> result = FXCollections.observableArrayList();
        List<Category> categories = tblCategories.getItems();

        if(tblCategories.getItems().isEmpty()) {
            for(MaterialStock stock : materialStocks) {
                if(stock.getMaterial().getCategories().isEmpty()) {
                    result.add(stock);
                }
            }
        }
        else {
            for(MaterialStock stock : materialStocks) {
                Set<Category> materialCategories = stock.getMaterial().getCategories();
                for(Category category: categories) {
                    if(materialCategories.contains(category)) {
                        result.add(stock);
                    }
                }
            }
        }
        tblMaterials.setItems(result);
        tblMaterials.getSelectionModel().selectFirst();
    }
}
