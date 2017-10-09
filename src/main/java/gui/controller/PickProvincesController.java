package gui.controller;

import dao.ProvinceDAO;
import domain.Province;
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
public class PickProvincesController implements Initializable {

    @FXML
    private TableView<Province> tblAllProvinces;
    @FXML
    private TableColumn colIdAll;
    @FXML
    private TableColumn colNameAll;
    @FXML
    private Button btnSelectProvince;
    @FXML
    private Button btnRemoveProvince;
    @FXML
    private Button btnRemoveAllProvinces;
    @FXML
    private TableView<Province> tblSelectedProvinces;
    @FXML
    private TableColumn colIdSelected;
    @FXML
    private TableColumn colNameSelected;
    @FXML
    private Button btnSelectProvinces;
    @FXML
    private Button btnCancel;

    private SendEmailController sendEmailController;
    private ProvinceDAO provinceDAO;
    private AlertBuilder alertBuilder;

    @Autowired
    public PickProvincesController(SendEmailController sendEmailController, ProvinceDAO provinceDAO,
                                  AlertBuilder alertBuilder) {

        this.sendEmailController = sendEmailController;
        this.provinceDAO = provinceDAO;
        this.alertBuilder = alertBuilder;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAllProvincesTable(provinceDAO.findAll());
        initSelectedProvincesTable(sendEmailController.getSelectedProvinces());
    }

    private void initAllProvincesTable(List<Province> provinceList) {
        colIdAll.setCellValueFactory(new PropertyValueFactory<Province, String>("id"));
        colNameAll.setCellValueFactory(new PropertyValueFactory<Province, String>("name"));
        ObservableList<Province> provinces = FXCollections.observableArrayList();
        provinces.addAll(provinceList);
        tblAllProvinces.setItems(provinces);
        tblAllProvinces.getSelectionModel().selectFirst();
    }

    private void initSelectedProvincesTable(List<Province> provinceList) {
        colIdSelected.setCellValueFactory(new PropertyValueFactory<Province, String>("id"));
        colNameSelected.setCellValueFactory(new PropertyValueFactory<Province, String>("name"));
        ObservableList<Province> provinces = FXCollections.observableArrayList();
        provinces.addAll(provinceList);
        tblSelectedProvinces.setItems(provinces);
        tblSelectedProvinces.getSelectionModel().selectFirst();
    }

    public void selectProvinceOnClick(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            selectProvince(null);
        }
    }

    public void selectProvince(ActionEvent actionEvent) {
        if(!tblAllProvinces.getItems().isEmpty()) {
            Province province = tblAllProvinces.getSelectionModel().getSelectedItem();
            tblAllProvinces.getItems().remove(province);
            tblSelectedProvinces.getItems().add(province);
            tblSelectedProvinces.getSelectionModel().selectFirst();
        }
    }

    public void removeProvince(ActionEvent actionEvent) {
        if(!tblSelectedProvinces.getItems().isEmpty()) {
            Province province = tblSelectedProvinces.getSelectionModel().getSelectedItem();
            tblSelectedProvinces.getItems().remove(province);
            tblAllProvinces.getItems().add(province);
            tblAllProvinces.getSelectionModel().selectFirst();
        }
    }

    public void removeAllProvinces(ActionEvent actionEvent) {
        ObservableList<Province> selectedProducts = tblSelectedProvinces.getItems();
        tblAllProvinces.getItems().addAll(selectedProducts);
        tblSelectedProvinces.getItems().removeAll(selectedProducts);
        tblAllProvinces.getSelectionModel().selectFirst();
    }

    public void removeProvinceOnClick(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            removeProvince(null);
        }
    }

    public void selectProvinces(ActionEvent actionEvent) {
        sendEmailController.loadSelectedProvinces(tblSelectedProvinces.getItems());
        Stage stage = (Stage) btnSelectProvinces.getScene().getWindow();
        stage.close();
    }

    public void cancelLoad(ActionEvent actionEvent) {
        Alert alert = alertBuilder.builder()
                .type(Alert.AlertType.CONFIRMATION)
                .title("Cancelar Selección")
                .headerText("Cancelando selección de provincias")
                .contentText("¿Desea cancelar selección?")
                .build();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }
}
